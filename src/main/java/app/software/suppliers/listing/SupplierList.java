package app.software.suppliers.listing;

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
import app.db.dao.production.SupplierDao;
import app.db.pojo.column.Column;

import app.db.pojo.column.SupplierColumn;
import app.db.pojo.production.Supplier;
import app.software.suppliers.SupplierPage;
import app.software.suppliers.form.SupplierAddForm;
import app.software.suppliers.form.SupplierDeleteConfirmation;
import app.software.suppliers.form.SupplierEditForm;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class SupplierList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private SupplierPage owner;
    private ScrollView scrollView = new ScrollView(3);
    private ActionPanel actionPanel;
    private SearchPanel searchPanel;
    private SupplierEntry activeSupplierEntry;

    public SupplierList(SupplierPage owner) {
        super(new MigLayout("fillx", "[grow][grow]"), false);

        this.owner = owner;

        actionPanel = new ActionPanel();
        searchPanel = buildSearchPanel();

        initActionPanelButtons();

        add(searchPanel, "span, grow, wrap");
        add(new SupplierSortPanel(this));
        add(actionPanel, "align 100%, pushx, wrap");
        add(new ScrollList(scrollView), "height 100%, span, grow");

        loadSupplier();
    }

    private void initActionPanelButtons() {
        actionPanel.setButtonAction(
            actionPanel.getViewButton(), 
            () -> {
                resetActiveSupplierEntry();
                actionPanel.setActiveButton(actionPanel.getViewButton());
                owner.removeActionView();
            });

        actionPanel.setButtonAction(
            actionPanel.getAddButton(), 
            () -> {
                resetActiveSupplierEntry();
                actionPanel.setActiveButton(actionPanel.getAddButton());
                owner.loadActionView(new SupplierAddForm(this));
            });

        actionPanel.setButtonAction(actionPanel.getEditButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getEditButton());

            if (activeSupplierEntry != null) {
                PopupDialog confirm = new PopupDialog("Edit Supplier in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A supplier is currently in view. Would you like to edit it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveSupplierEntry();

                    PopupDialog notif = new PopupDialog("Select a Supplier to Edit");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a supplier to edit.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    owner.loadActionView(new SupplierEditForm(this));
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Supplier to Edit");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a supplier to edit.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });

         actionPanel.setButtonAction(actionPanel.getDeleteButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getDeleteButton());
            
            if (activeSupplierEntry != null) {
                PopupDialog confirm = new PopupDialog("Delete Supplier in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A supplier is currently in view. Would you like to delete it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveSupplierEntry();

                    PopupDialog notif = new PopupDialog("Select a Supplier to Delete");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a supplier to delete.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();

                    if (new SupplierDeleteConfirmation(activeSupplierEntry).isConfirmed()) {
                        owner.removeActionView();
                        resetActiveSupplierEntry();
                    }
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Supplier to Delete");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a supplier to delete.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });
    }

    private SearchPanel buildSearchPanel() {
        SearchPanel searchPanel = new SearchPanel(this);

        Map<Column, String> columns = new LinkedHashMap<>();
        columns.put(SupplierColumn.SupplierID, "Supplier ID");
        columns.put(SupplierColumn.SupplierName, "Supplier Name");
        columns.put(SupplierColumn.ContactNumber, "Contact Number");

        searchPanel.addSearchColumns(columns);

        searchPanel.setDefaultSearch(() -> loadSupplier());
        searchPanel.setSearchAction(() -> loadSupplier(searchPanel.getSearchBar().getText()));

        return searchPanel;
    }

    private void reloadActiveSupplierEntry() {
        for (Component entry: scrollView.getComponents()) {
            SupplierEntry categoryEntry = (SupplierEntry) entry;

            if (activeSupplierEntry != null && ((Supplier) activeSupplierEntry.getPojo()).getSupplierId() == ((Supplier) categoryEntry.getPojo()).getSupplierId()) {
                activeSupplierEntry = categoryEntry;
                activeSupplierEntry.setSelected(true);
                activeSupplierEntry.requestFocusInWindow();
            }
        }
    }

    public void loadSupplier() {
        scrollView.removeAll();

        try {
            for (Supplier supplier : stringifySortMap().isBlank() ? SupplierDao.getAllSuppliers() : SupplierDao.getAllSuppliersOrderBy(stringifySortMap())) {
                scrollView.add(new SupplierEntry(this, supplier), "grow");
            } 
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Suppliers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load suppliers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveSupplierEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadSupplier(String search) {
        scrollView.removeAll();

        try {
            for (Supplier supplier : stringifySortMap().isBlank() ? SupplierDao.getAllSuppliersSearchBy(search) : SupplierDao.getAllSuppliersSearchByOrderBy(search, stringifySortMap())) {
                scrollView.add(new SupplierEntry(this, supplier), "grow");
            } 
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Suppliers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load suppliers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveSupplierEntry();

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

    public SupplierPage getOwner() {
        return owner;
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public SupplierEntry getActiveSupplierEntry() {
        return activeSupplierEntry;
    }

    public void setActiveSupplierEntry(SupplierEntry activeSupplierEntry) {
        if (this.activeSupplierEntry != null) {
            this.activeSupplierEntry.setSelected(false);
        }

        this.activeSupplierEntry = activeSupplierEntry;
        this.activeSupplierEntry.setSelected(true);
        this.activeSupplierEntry.requestFocusInWindow();
    }

    public void resetActiveSupplierEntry() {
        if (this.activeSupplierEntry == null) { return; }
        this.activeSupplierEntry.setSelected(false);
        transferFocus();
        this.activeSupplierEntry = null;
    }
}

