package app.software.customers.listing;

import java.awt.Component;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import app.components.ActionPanel;
import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.components.ScrollView;
import app.components.search.SearchPanel;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.column.Column;
import app.db.pojo.column.CustomerColumn;
import app.db.pojo.customer.Customer;
import app.software.customers.CustomersPage;
import app.software.customers.form.CustomerAddForm;
import app.software.customers.form.CustomerDeleteConfirmation;
import app.software.customers.form.CustomerEditForm;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class CustomerList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private ScrollView scrollView = new ScrollView(3);
    private SearchPanel searchPanel;
    private ActionPanel actionPanel;
    private CustomersPage owner;
    private CustomerEntry activeCustomerEntry;

    public CustomerList(CustomersPage owner) {
        super(new MigLayout("fillx", "[grow][grow]"), false);

        this.owner = owner;
        searchPanel = buildSearchPanel();
        actionPanel = new ActionPanel();

        initActionPanelButtons();

        add(searchPanel, "span, grow, wrap");
        add(new CustomerSortPanel(this));   
        add(actionPanel, "align 100%, pushx, wrap");
        add(new ScrollList(scrollView), "span, grow");

        loadCustomers();
    }

    private void initActionPanelButtons() {
        actionPanel.setButtonAction(
            actionPanel.getViewButton(), 
            () -> {
                resetActiveCustomerEntry();
                actionPanel.setActiveButton(actionPanel.getViewButton());
                owner.removeActionView();
            });

        actionPanel.setButtonAction(
            actionPanel.getAddButton(), 
            () -> {
                resetActiveCustomerEntry();
                actionPanel.setActiveButton(actionPanel.getAddButton());
                owner.loadActionView(new CustomerAddForm(this));
            });

        actionPanel.setButtonAction(actionPanel.getEditButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getEditButton());

            if (activeCustomerEntry != null) {
                PopupDialog confirm = new PopupDialog("Edit Customer in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A customer is currently in view. Would you like to edit it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveCustomerEntry();

                    PopupDialog notif = new PopupDialog("Select a Customer to Edit");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a customer to edit.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    owner.loadActionView(new CustomerEditForm(this));
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Customer to Edit");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a customer to edit.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });

         actionPanel.setButtonAction(actionPanel.getDeleteButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getDeleteButton());
            
            if (activeCustomerEntry != null) {
                PopupDialog confirm = new PopupDialog("Delete Customer in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A customer is currently in view. Would you like to delete it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveCustomerEntry();

                    PopupDialog notif = new PopupDialog("Select a Customer to Delete");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a customer to delete.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();
                    if (new CustomerDeleteConfirmation(activeCustomerEntry).isConfirmed()) {
                        owner.removeActionView();
                        resetActiveCustomerEntry();
                    }
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Customer to Delete");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a customer to delete.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });
    }

    private SearchPanel buildSearchPanel() {
        SearchPanel searchPanel = new SearchPanel(this);

        Map<Column, String> columns = new LinkedHashMap<>();
        columns.put(CustomerColumn.CustomerID, "Customer ID");
        columns.put(CustomerColumn.FirstName, "First Name");
        columns.put(CustomerColumn.LastName, "Last Name");
        columns.put(CustomerColumn.ContactNumber, "Contact Number");
        columns.put(CustomerColumn.Street, "Street");
        columns.put(CustomerColumn.Barangay, "Barangay");
        columns.put(CustomerColumn.City, "City");
        columns.put(CustomerColumn.Province, "Province");

        searchPanel.addSearchColumns(columns);

        searchPanel.setDefaultSearch(() -> loadCustomers());
        searchPanel.setSearchAction(() -> loadCustomers(searchPanel.getSearchBar().getText()));

        return searchPanel;
    }

    private void reloadActiveCustomerEntry() {
        for (Component entry: scrollView.getComponents()) {
            CustomerEntry customerEntry = (CustomerEntry) entry;

            if (activeCustomerEntry != null && ((Customer) activeCustomerEntry.getPojo()).getCustomerId() == ((Customer) customerEntry.getPojo()).getCustomerId()) {
                activeCustomerEntry = customerEntry;
                activeCustomerEntry.doClick();
            }
        }
    }

    public void loadCustomers() {
        scrollView.removeAll();

        for (Customer customer : stringifySortMap().isBlank() ? CustomerDao.getAllCustomers() : CustomerDao.getAllCustomersOrderBy(stringifySortMap())) {
            scrollView.add(new CustomerEntry(this, customer), "grow");
        } 

        reloadActiveCustomerEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadCustomers(String search) {
        scrollView.removeAll();

        try {
            for (Customer customer : stringifySortMap().isBlank() ? CustomerDao.getAllCustomersSearchBy(search) : CustomerDao.getAllCustomersSearchByOrderBy(search, stringifySortMap())) {
                scrollView.add(new CustomerEntry(this, customer), "grow");
            }
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Customers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load Customers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveCustomerEntry();

        scrollView.revalidate();
        scrollView.repaint();
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

    public CustomersPage getOwner() {
        return owner;
    }
    
    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public CustomerEntry getActiveCustomerEntry() {
        return activeCustomerEntry;
    }

    public void setActiveCustomerEntry(CustomerEntry activeCustomerEntry) {
        if (this.activeCustomerEntry != null) {
            this.activeCustomerEntry.setSelected(false);
        }

        this.activeCustomerEntry = activeCustomerEntry;
        this.activeCustomerEntry.setSelected(true);
        this.activeCustomerEntry.requestFocusInWindow();
    }

    public void resetActiveCustomerEntry() {
        if (this.activeCustomerEntry == null) { return; }
        this.activeCustomerEntry.setSelected(false);
        transferFocus();
        this.activeCustomerEntry = null;
    }
}
