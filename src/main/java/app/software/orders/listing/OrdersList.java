package app.software.orders.listing;

import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import app.components.ActionPanel;
import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.components.ScrollView;
import app.db.dao.production.SupplierDao;
import app.db.dao.production.SupplyOrderDao;
import app.db.pojo.column.Column;
import app.db.pojo.column.OrderStatus;
import app.db.pojo.production.SupplyOrder;
import app.software.orders.OrdersPage;
import app.software.orders.form.OrderAddForm;
import app.software.orders.form.OrderDeleteConfirmation;
import app.software.orders.form.OrderEditForm;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class OrdersList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private String sortSupplier = "All";
    private String sortStatus = "All";
    private ScrollView scrollView = new ScrollView(5);
    private ActionPanel actionPanel;
    private OrdersPage owner;
    private OrderEntry activeOrderEntry;

    public OrdersList(OrdersPage owner) {
        super(new MigLayout("fillx", "[grow][grow]"), false);

        this.owner = owner;
        actionPanel = new ActionPanel();

        initActionPanelButtons();

        add(new OrdersSortPanel(this));   
        add(actionPanel, "align 100%, pushx, wrap");
        add(new ScrollList(scrollView), "span, grow");

        loadSupplyOrders();
    }

    private void initActionPanelButtons() {
        actionPanel.setButtonAction(
            actionPanel.getViewButton(), 
            () -> {
                resetActiveOrderEntry();
                actionPanel.setActiveButton(actionPanel.getViewButton());
                owner.removeActionView();
            });

        actionPanel.setButtonAction(
            actionPanel.getAddButton(), 
            () -> {
                resetActiveOrderEntry();
                actionPanel.setActiveButton(actionPanel.getAddButton());
                owner.loadActionView(new OrderAddForm(this));
            });

        actionPanel.setButtonAction(actionPanel.getEditButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getEditButton());
            if (activeOrderEntry != null) {
                PopupDialog confirm = new PopupDialog("Edit Supply Order in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A supply order is currently in view. Would you like to edit it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveOrderEntry();

                    PopupDialog notif = new PopupDialog("Select a Supply Order to Edit");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a supply order to edit.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();

                    SupplyOrder activeOrder = (SupplyOrder) activeOrderEntry.getPojo();

                    if (activeOrder.getOrderStatus().equals(OrderStatus.Cancelled.name())) {
                        PopupDialog error = new PopupDialog("Unable to Edit Cancelled Order");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("You cannot edit a cancelled supply order.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                        return;
                    } else if (activeOrder.getOrderStatus().equals(OrderStatus.Delivered.name())) {
                        PopupDialog error = new PopupDialog("Unable to Edit Delivered Order");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("You cannot edit a delivered supply order.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                        return;
                    }
                    
                    owner.loadActionView(new OrderEditForm(this));
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Supply Order to Edit");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a supply order to edit.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });

         actionPanel.setButtonAction(actionPanel.getDeleteButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getDeleteButton());
            
            if (activeOrderEntry != null) {
                PopupDialog confirm = new PopupDialog("Delete Supply Order in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A supply order is currently in view. Would you like to delete it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveOrderEntry();

                    PopupDialog notif = new PopupDialog("Select a Supply Order to Delete");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a supply order to delete.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();

                    SupplyOrder activeOrder = (SupplyOrder) activeOrderEntry.getPojo();

                    if (activeOrder.getOrderStatus().equals(OrderStatus.Delivered.name())) {
                        PopupDialog error = new PopupDialog("Unable to Delete Supply Order");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("This supply order has already been delivered and cannot be deleted.");
                        error.setCloseButtonAction(() -> {error.dispose(); resetActiveOrderEntry();});
                        error.display();
                    } else if (new OrderDeleteConfirmation(activeOrderEntry).isConfirmed()) {
                        owner.removeActionView();
                        resetActiveOrderEntry();
                    }
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Supply Order to Delete");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a supply order to delete.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });
    }

    private void reloadActiveOrderEntry() {
        for (Component entry: scrollView.getComponents()) {
            OrderEntry orderEntry = (OrderEntry) entry;

            if (activeOrderEntry != null && ((SupplyOrder) activeOrderEntry.getPojo()).getOrderId() == ((SupplyOrder) orderEntry.getPojo()).getOrderId()) {
                activeOrderEntry = orderEntry;
                activeOrderEntry.setSelected(true);
                activeOrderEntry.requestFocusInWindow();
            }
        }
    }

    public void loadSupplyOrders() {
        scrollView.removeAll();

        try {
            for (SupplyOrder supplyOrder : stringifySortMap().isBlank() ? SupplyOrderDao.getAllSupplyOrders() : SupplyOrderDao.getAllSupplyOrdersOrderBy(stringifySortMap())) {
                scrollView.add(new OrderEntry(this, supplyOrder), "grow");
            } 
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Orders");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load orders\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveOrderEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadCategorizedSupplyOrders() {
        scrollView.removeAll();

        List<SupplyOrder> supplyOrders = new ArrayList<>();

        try {
            if (sortSupplier.equals("All")) {
                if (sortStatus.equals("All")) {
                    supplyOrders = stringifySortMap().isBlank() ? SupplyOrderDao.getAllSupplyOrders() : SupplyOrderDao.getAllSupplyOrdersOrderBy(stringifySortMap());
                } else {
                    supplyOrders = stringifySortMap().isBlank() ? SupplyOrderDao.getSupplyOrdersByOrderStatus(sortStatus) : SupplyOrderDao.getSupplyOrdersByOrderStatusOrderBy(sortStatus, stringifySortMap());
                }
            } else {
                int supplierId = SupplierDao.getSupplierByName(sortSupplier).getSupplierId();

                if (sortStatus.equals("All")) {
                    supplyOrders = stringifySortMap().isBlank() ? SupplyOrderDao.getSupplyOrdersBySupplierId(supplierId) : SupplyOrderDao.getSupplyOrdersBySupplierIdOrderBy(supplierId, stringifySortMap());
                } else {
                    supplyOrders = stringifySortMap().isBlank() ? SupplyOrderDao.getSupplyOrdersBySupplierIdByOrderStatus(supplierId, sortStatus) : SupplyOrderDao.getSupplyOrdersBySupplierIdByOrderStatusOrderBy(supplierId, sortStatus, stringifySortMap());
                }
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Orders");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load orders\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        for (SupplyOrder supplyOrder: supplyOrders) {
            scrollView.add(new OrderEntry(this, supplyOrder), "grow");
        }

        reloadActiveOrderEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void setSortSupplier(String supplier) {
        sortSupplier = supplier;
    }

    public String getSortSupplier() {
        return sortSupplier;
    }

    public void setSortStatus(String status) {
        this.sortStatus = status;
    }

    public String getSortStatus() {
        return sortStatus;
    }

    public void addSort(Column column, Sort sort) {
        sortMap.put(column, sort);
    }

    public void removeSort(Column column) {
        sortMap.remove(column);
    }

    public String stringifySortMap() {
        return sortMap.entrySet().stream()
        .map(entry -> entry.getKey().getName() + " " + entry.getValue().getName())
        .collect(Collectors.joining(", "));
    }

    public OrdersPage getOwner() {
        return owner;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public OrderEntry getActiveOrderEntry() {
        return activeOrderEntry;
    }

    public void setActiveOrderEntry(OrderEntry activeOrderEntry) {
        if (this.activeOrderEntry != null) {
            this.activeOrderEntry.setSelected(false);
        }

        this.activeOrderEntry = activeOrderEntry;
        this.activeOrderEntry.setSelected(true);
        this.activeOrderEntry.requestFocusInWindow();
    }

    public void resetActiveOrderEntry() {
        if (this.activeOrderEntry == null) { return; }
        this.activeOrderEntry.setSelected(false);
        transferFocus();
        this.activeOrderEntry = null;
    }

}
