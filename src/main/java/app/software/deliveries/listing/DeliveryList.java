package app.software.deliveries.listing;

import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.components.ScrollView;
import app.db.dao.customer.CustomerDao;
import app.db.dao.sales.DeliveryDao;
import app.db.pojo.column.Column;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.sales.Delivery;
import app.software.deliveries.DeliveryPage;
import app.software.deliveries.form.DeliveryDeleteConfirmation;
import app.software.deliveries.form.DeliveryEditForm;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class DeliveryList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private String sortCustomerFirstName = "All";
    private String sortCustomerLastName = "All";
    private String sortStatus = "All";
    private ScrollView scrollView = new ScrollView(5);
    private DeliveryActionPanel actionPanel;
    private DeliveryPage owner;
    private DeliveryEntry activeDeliveryEntry;

    public DeliveryList(DeliveryPage owner) {
        super(new MigLayout("fillx", "[grow][grow]"), false);

        this.owner = owner;
        actionPanel = new DeliveryActionPanel();

        initActionPanelButtons();

        add(new DeliverySortPanel(this));   
        add(actionPanel, "align 100%, pushx, wrap");
        add(new ScrollList(scrollView), "span, grow");

        loadDeliveries();
    }

    private void initActionPanelButtons() {
        actionPanel.setButtonAction(
            actionPanel.getViewButton(), 
            () -> {
                resetActiveDeliveryEntry();
                actionPanel.setActiveButton(actionPanel.getViewButton());
                owner.removeActionView();
            });

        actionPanel.setButtonAction(actionPanel.getEditButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getEditButton());
            if (activeDeliveryEntry != null) {
                PopupDialog confirm = new PopupDialog("Edit Delivery in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A delivery is currently in view. Would you like to edit it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveDeliveryEntry();

                    PopupDialog notif = new PopupDialog("Select a Delivery to Edit");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a delivery to edit.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();

                    Delivery activeDelivery = (Delivery) activeDeliveryEntry.getPojo();

                    if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Cancelled.name())) {
                        PopupDialog error = new PopupDialog("Unable to Edit Cancelled Delivery");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("You cannot edit a cancelled delivery.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                        return;
                    } else if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Delivered.name())) {
                        PopupDialog error = new PopupDialog("Unable to Edit Delivered Delivery");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("You cannot edit a delivered delivery.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                        return;
                    }
                    
                    owner.loadActionView(new DeliveryEditForm(this));
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Delivery to Edit");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a delivery to edit.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });

         actionPanel.setButtonAction(actionPanel.getDeleteButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getDeleteButton());

            if (activeDeliveryEntry != null) {
                PopupDialog confirm = new PopupDialog("Delete Delivery in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A delivery is currently in view. Would you like to delete it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveDeliveryEntry();

                    PopupDialog notif = new PopupDialog("Select a Delivery to Delete");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a delivery to delete.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();

                    Delivery activeDelivery = (Delivery) activeDeliveryEntry.getPojo();
                    
                    if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Shipped.name())) {
                        PopupDialog error = new PopupDialog("Unable to Delete Shipped Delivery");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("You cannot delete a shipped delivery.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                        return;
                    } else if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Delivered.name())) {
                        PopupDialog error = new PopupDialog("Unable to Delete Delivered Delivery");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("You cannot delete a delivered delivery.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                        return;
                    } else if (activeDelivery.getDeliveryStatus().equals("Out for Delivery")) {
                        PopupDialog error = new PopupDialog("Unable to Delete Delivery in Progress");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("You cannot delete a delivery that is underway.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                        return;
                    }

                    if (new DeliveryDeleteConfirmation(activeDeliveryEntry).isConfirmed()) {
                        owner.removeActionView();
                        resetActiveDeliveryEntry();
                    }
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Delivery to Delete");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a delivery to delete.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });
    }

    private void reloadActiveDeliveryEntry() {
        for (Component entry: scrollView.getComponents()) {
            DeliveryEntry deliveryEntry = (DeliveryEntry) entry;

            if (activeDeliveryEntry != null && ((Delivery) activeDeliveryEntry.getPojo()).getDeliveryId() == ((Delivery) deliveryEntry.getPojo()).getDeliveryId()) {
                activeDeliveryEntry = deliveryEntry;
                activeDeliveryEntry.setSelected(true);
                activeDeliveryEntry.requestFocusInWindow();
            }
        }
    }

    public void loadDeliveries() {
        scrollView.removeAll();

        try {
            for (Delivery delivery : stringifySortMap().isBlank() ? DeliveryDao.getAllDeliveries() : DeliveryDao.getAllDeliveriesOrderBy(stringifySortMap())) {
                scrollView.add(new DeliveryEntry(this, delivery), "grow");
            } 
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Deliveries");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load deliveries\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveDeliveryEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadCategorizedDeliveries() {
        scrollView.removeAll();

        List<Delivery> deliveries = new ArrayList<>();

        try {
            if (sortCustomerFirstName.equals("All")) {
                if (sortStatus.equals("All")) {
                    deliveries = stringifySortMap().isBlank() ? DeliveryDao.getAllDeliveries() : DeliveryDao.getAllDeliveriesOrderBy(stringifySortMap());
                } else {
                    deliveries = stringifySortMap().isBlank() ? DeliveryDao.getDeliveriesByDeliveryStatus(sortStatus) : DeliveryDao.getDeliveriesByDeliveryStatusOrderBy(sortStatus, stringifySortMap());
                }
            } else {
                int customerId = CustomerDao.getCustomerByName(sortCustomerFirstName, sortCustomerLastName).getCustomerId();

                if (sortStatus.equals("All")) {
                    deliveries = stringifySortMap().isBlank() ? DeliveryDao.getDeliveriesByCustomerId(customerId) : DeliveryDao.getDeliveriesByCustomerIdOrderBy(customerId, stringifySortMap());
                } else {
                    deliveries = stringifySortMap().isBlank() ? DeliveryDao.getDeliveriesByCustomerIdByDeliveryStatus(customerId, sortStatus) : DeliveryDao.getDeliveriesByCustomerIdByDeliveryStatusOrderBy(customerId, sortStatus, stringifySortMap());
                }
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Deliveries");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load deliveries\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        for (Delivery delivery: deliveries) {
            scrollView.add(new DeliveryEntry(this, delivery), "grow");
        }

        reloadActiveDeliveryEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }
    
    public void setSortCustomer(String firstName, String lastName) {
        this.sortCustomerFirstName = firstName;
        this.sortCustomerLastName = lastName;
    }

    public String getSortCustomerFirstName() {
        return sortCustomerFirstName;
    }

    public String getSortCustomerLastName() {
        return sortCustomerLastName;
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

    public DeliveryPage getOwner() {
        return owner;
    }

    public DeliveryActionPanel getActionPanel() {
        return actionPanel;
    }

    public DeliveryEntry getActiveDeliveryEntry() {
        return activeDeliveryEntry;
    }

    public void setactiveDeliveryEntry(DeliveryEntry activeDeliveryEntry) {
        if (this.activeDeliveryEntry != null) {
            this.activeDeliveryEntry.setSelected(false);
        }

        this.activeDeliveryEntry = activeDeliveryEntry;
        this.activeDeliveryEntry.setSelected(true);
        this.activeDeliveryEntry.requestFocusInWindow();
    }

    public void resetActiveDeliveryEntry() {
        if (this.activeDeliveryEntry == null) { return; }
        this.activeDeliveryEntry.setSelected(false);
        transferFocus();
        this.activeDeliveryEntry = null;
    }

}
