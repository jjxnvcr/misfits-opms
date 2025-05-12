package app.software.sales.listing;

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
import app.db.dao.customer.CustomerDao;
import app.db.dao.sales.DeliveryDao;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.column.Column;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.sales.SalesTransaction;
import app.software.sales.SalesPage;
import app.software.sales.form.SaleDeleteConfirmation;
import app.software.sales.form.SaleEditForm;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class SalesList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private String sortCustomerFirstName = "All";
    private String sortCustomerLastName = "All";
    private ScrollView scrollView = new ScrollView(5);
    private ActionPanel actionPanel;
    private SalesPage owner;
    private SaleEntry activeSaleEntry;

    public SalesList(SalesPage owner) {
        super(new MigLayout("fillx", "[grow][grow]"), false);

        this.owner = owner;
        actionPanel = new ActionPanel();

        initActionPanelButtons();

        add(new SalesSortPanel(this));   
        add(actionPanel, "align 100%, pushx, wrap");
        add(new ScrollList(scrollView), "height 100%, span, grow");

        loadSaleTransactions();
    }

    private void initActionPanelButtons() {
        actionPanel.setButtonAction(
            actionPanel.getViewButton(), 
            () -> {
                resetActiveSaleEntry();
                actionPanel.setActiveButton(actionPanel.getViewButton());
                owner.removeActionView();
            });

        actionPanel.setButtonAction(
            actionPanel.getAddButton(), 
            () -> {
                resetActiveSaleEntry();
                owner.getOwner().getNav().getCashierButton().doClick();
                owner.getOwner().getNav().getCashierButton().requestFocusInWindow();
            });

        actionPanel.setButtonAction(actionPanel.getEditButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getEditButton());
            try {
                if (activeSaleEntry != null) {
                    if (DeliveryDao.getDeliveryByTransactionId(((SalesTransaction) activeSaleEntry.getPojo()).getTransactionId()).getDeliveryStatus().equals(DeliveryStatus.Pending.name())) {
                        PopupDialog confirm = new PopupDialog("Edit Sale Transaction in View");
                        confirm.setDialogType(DialogType.CONFIRMATION);
                        confirm.setMessage("A sale transaction is currently in view. Would you like to edit it?");
                        confirm.setCloseButtonAction(() -> {
                            confirm.dispose();
                            owner.removeActionView();
                            resetActiveSaleEntry();
        
                            PopupDialog notif = new PopupDialog("Select a Sale Transaction to Edit");
                            notif.setDialogType(DialogType.NOTIFICATION);
                            notif.setMessage("Please select a sale transaction to edit.");
                            notif.setCloseButtonAction(() -> notif.dispose());
                            notif.display();
                        });
                        
                        confirm.setConfirmButtonAction(() -> {
                            confirm.dispose();
                            owner.removeActionView();
                            owner.loadActionView(new SaleEditForm(this));
                        });
                        confirm.display();
                        } 
                    else {
                        PopupDialog error = new PopupDialog("Unable to Edit Sale Transaction");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Sale transaction is already being processed. Unable to edit.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                
                } else {
                    PopupDialog notif = new PopupDialog("Select a Sale Transaction to Edit");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a sale transaction to edit.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                }
            } catch (Exception e) {
                PopupDialog error = new PopupDialog("Unable to Edit Sale Transaction");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("An error occured. Please try again.\n\n" + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display(); 
            }
         });

         actionPanel.setButtonAction(actionPanel.getDeleteButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getDeleteButton());
            
            try {
                if (activeSaleEntry != null) {
                    if (DeliveryDao.getDeliveryByTransactionId(((SalesTransaction) activeSaleEntry.getPojo()).getTransactionId()).getDeliveryStatus().equals(DeliveryStatus.Pending.name())) {
                        PopupDialog confirm = new PopupDialog("Delete Sale Transaction in View");
                        confirm.setDialogType(DialogType.CONFIRMATION);
                        confirm.setMessage("A sale transaction is currently in view. Would you like to delete it?");
                        confirm.setCloseButtonAction(() -> {
                            confirm.dispose();
                            owner.removeActionView();
                            resetActiveSaleEntry();
        
                            PopupDialog notif = new PopupDialog("Select a Sale Transaction to Delete");
                            notif.setDialogType(DialogType.NOTIFICATION);
                            notif.setMessage("Please select a sale transaction to delete.");
                            notif.setCloseButtonAction(() -> notif.dispose());
                            notif.display();
                        });
                        confirm.setConfirmButtonAction(() -> {
                            confirm.dispose();
                            if (new SaleDeleteConfirmation(activeSaleEntry).isConfirmed()) {
                            owner.removeActionView();
                            resetActiveSaleEntry();
                        }
                        });
                        confirm.display();
                    } else {
                        PopupDialog error = new PopupDialog("Unable to Delete Sale Transaction");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Sale transaction is already being processed. Unable to delete.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                } else {
                    PopupDialog notif = new PopupDialog("Select a Sale Transaction to Delete");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a sale transaction to delete.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                }
            } catch (Exception e) {
                PopupDialog error = new PopupDialog("Unable to Delete Sale Transaction");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("An error occured. Please try again.\n\n" + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
            }
         });
    }

    private void reloadActiveSaleEntry() {
        for (Component entry: scrollView.getComponents()) {
            SaleEntry saleEntry = (SaleEntry) entry;

            if (activeSaleEntry != null && ((SalesTransaction) activeSaleEntry.getPojo()).getTransactionId() == ((SalesTransaction) saleEntry.getPojo()).getTransactionId()) {
                activeSaleEntry = saleEntry;
                activeSaleEntry.setSelected(true);
                activeSaleEntry.requestFocusInWindow();
            }
        }
    }

    public void loadSaleTransactions() {
        scrollView.removeAll();

        try {
            for (SalesTransaction sale : stringifySortMap().isBlank() ? SalesTransactionDao.getAllSalesTransactions() : SalesTransactionDao.getAllSalesTransactionsOrderBy(stringifySortMap())) {
                scrollView.add(new SaleEntry(this, sale), "grow");
            } 
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Sale Transactions");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load sale transactions\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveSaleEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadCustomerSaleTransactions() {
        scrollView.removeAll();

        List<SalesTransaction> sales = new ArrayList<>();

        try {
            if (sortCustomerFirstName.equals("All")) {
                sales = stringifySortMap().isBlank() ? SalesTransactionDao.getAllSalesTransactions() : SalesTransactionDao.getAllSalesTransactionsOrderBy(stringifySortMap()); 
            } else {
                int customerId = CustomerDao.getCustomerByName(sortCustomerFirstName, sortCustomerLastName).getCustomerId();

                sales = stringifySortMap().isBlank() ? SalesTransactionDao.getSalesTransactionsByCustomerId(customerId) : SalesTransactionDao.getSalesTransactionsByCustomerIdOrderBy(customerId, stringifySortMap());
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Sale Transactions");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load sale transactions\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        for (SalesTransaction sale : sales) {
            scrollView.add(new SaleEntry(this, sale), "grow");
        }

        reloadActiveSaleEntry();

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

    public SalesPage getOwner() {
        return owner;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public SaleEntry getActiveSaleEntry() {
        return activeSaleEntry;
    }

    public void setActiveSaleEntry(SaleEntry activeSaleEntry) {
        if (this.activeSaleEntry != null) {
            this.activeSaleEntry.setSelected(false);
        }

        this.activeSaleEntry = activeSaleEntry;
        this.activeSaleEntry.setSelected(true);
        this.activeSaleEntry.requestFocusInWindow();
    }

    public void resetActiveSaleEntry() {
        if (this.activeSaleEntry == null) { return; }
        this.activeSaleEntry.setSelected(false);
        transferFocus();
        this.activeSaleEntry = null;
    }
}
