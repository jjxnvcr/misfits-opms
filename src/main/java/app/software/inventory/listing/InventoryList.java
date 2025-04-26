package app.software.inventory.listing;

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
import app.components.search.SearchPanel;
import app.db.dao.production.CategoryDao;
import app.db.dao.production.ItemDao;

import app.db.pojo.column.Column;
import app.db.pojo.column.ItemColumn;

import app.db.pojo.production.Item;
import app.software.inventory.InventoryPage;
import app.software.inventory.form.ItemAddForm;
import app.software.inventory.form.ItemDeleteForm;
import app.software.inventory.form.ItemEditForm;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class InventoryList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private String sortCategory;
    private ScrollView scrollView = new ScrollView(3);
    private SearchPanel searchPanel;
    private ActionPanel actionPanel;
    private InventoryPage owner;
    private ItemEntry activeItemEntry;

    public InventoryList(InventoryPage owner) {
        super(new MigLayout("fillx", "[grow][grow]"), false);

        this.owner = owner;
        searchPanel = buildSearchPanel();
        actionPanel = new ActionPanel();

        initActionPanelButtons();

        add(searchPanel, "span, grow, wrap");
        add(new InventorySortPanel(this));   
        add(actionPanel, "align 100%, pushx, wrap");
        add(new ScrollList(scrollView), "span, grow");

        loadCategoryInventory();
    }

    private void initActionPanelButtons() {
        actionPanel.setButtonAction(
            actionPanel.getViewButton(), 
            () -> {
                resetActiveItemEntry();
                actionPanel.setActiveButton(actionPanel.getViewButton());
                owner.removeActionView();
            });

        actionPanel.setButtonAction(
            actionPanel.getAddButton(), 
            () -> {
                resetActiveItemEntry();
                actionPanel.setActiveButton(actionPanel.getAddButton());
                owner.loadActionView(new ItemAddForm(this));
            });

        actionPanel.setButtonAction(actionPanel.getEditButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getEditButton());

            if (activeItemEntry != null) {
                PopupDialog confirm = new PopupDialog("Edit Item in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("An item is currently in view. Would you like to edit it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveItemEntry();

                    PopupDialog notif = new PopupDialog("Select an Item to Edit");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select an item to edit.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    owner.loadActionView(new ItemEditForm(this));
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select an Item to Edit");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select an item to edit.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });

         actionPanel.setButtonAction(actionPanel.getDeleteButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getDeleteButton());
            
            if (activeItemEntry != null) {
                PopupDialog confirm = new PopupDialog("Delete Item in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("An item is currently in view. Would you like to delete it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveItemEntry();

                    PopupDialog notif = new PopupDialog("Select an Item to Delete");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select an item to delete.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();

                    owner.loadActionView(new ItemDeleteForm(this));
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select an Item to Delete");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select an item to delete.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });
    }

    private SearchPanel buildSearchPanel() {
        SearchPanel searchPanel = new SearchPanel(this);

        Map<Column, String> columns = new LinkedHashMap<>();
        columns.put(ItemColumn.ItemID, "ID");
        columns.put(ItemColumn.ItemName, "Name");
        columns.put(ItemColumn.ItemDescription, "Color");

        searchPanel.addSearchColumns(columns);

        searchPanel.setDefaultSearch(() -> {
            loadCategoryInventory();
        });
        searchPanel.setSearchAction(() -> loadCategoryInventory(searchPanel.getSearchBar().getText()));

        return searchPanel;
    }

    private void reloadActiveItemEntry() {
        for (Component entry: scrollView.getComponents()) {
            ItemEntry itemEntry = (ItemEntry) entry;

            if (activeItemEntry != null && ((Item) activeItemEntry.getPojo()).getItemId() == ((Item) itemEntry.getPojo()).getItemId()) {
                activeItemEntry = itemEntry;
                activeItemEntry.setSelected(true);
                activeItemEntry.requestFocusInWindow();
            }
        }
    }

    public void loadInventory() {
        scrollView.removeAll();

        try {
            for (Item item : stringifySortMap().isBlank() ? ItemDao.getAllItems() : ItemDao.getAllItemsOrderBy(stringifySortMap())) {
                scrollView.add(new ItemEntry(this, item), "grow");
            } 
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Items");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load items\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveItemEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadInventory(String search) {
        scrollView.removeAll();

        try {
            for (Item item : stringifySortMap().isBlank() ? ItemDao.getAllItemsSearchBy(search) : ItemDao.getAllItemsSearchByOrderBy(search, stringifySortMap())) {
                scrollView.add(new ItemEntry(this, item), "grow");
            }

        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Items");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load items\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveItemEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadCategoryInventory() {
        scrollView.removeAll();

        List<Item> items = new ArrayList<>();

        try {
            if (sortCategory.equals("All")) {
                items = stringifySortMap().isBlank() ? ItemDao.getAllItems() : ItemDao.getAllItemsOrderBy(stringifySortMap());
            } else {
                int categoryId = CategoryDao.getCategoryByName(sortCategory).getCategoryId();

                items = stringifySortMap().isBlank() ? ItemDao.getAllItemsByCategory(categoryId) : ItemDao.getAllItemsByCategoryOrderBy(categoryId, stringifySortMap());
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Items");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load items\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        for (Item item: items) {
            scrollView.add(new ItemEntry(this, item), "grow");
        }

        reloadActiveItemEntry();

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadCategoryInventory(String search) {
        scrollView.removeAll();
        
        List<Item> items = new ArrayList<>();
        
        try {
            if (sortCategory.equals("All")) {
                items = stringifySortMap().isBlank() ? ItemDao.getAllItemsSearchBy(search) : ItemDao.getAllItemsSearchByOrderBy(search, stringifySortMap());
            } else {
                int categoryId = CategoryDao.getCategoryByName(sortCategory).getCategoryId();
                
                items = stringifySortMap().isBlank() ? ItemDao.getAllItemsByCategorySearchBy(categoryId, search) : ItemDao.getAllItemsByCategorySearchByOrderBy(categoryId, search, stringifySortMap());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            PopupDialog error = new PopupDialog("Unable to load Items");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load items\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        for (Item item: items) {
            scrollView.add(new ItemEntry(this, item), "grow");
        }

        reloadActiveItemEntry();

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

    public void setSortCategory(String sortCategory) {
        this.sortCategory = sortCategory;
    }

    public String getSortCategory() {
        return sortCategory;
    }

    public InventoryPage getOwner() {
        return owner;
    }
    
    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public ItemEntry getActiveItemEntry() {
        return activeItemEntry;
    }

    public void setActiveItemEntry(ItemEntry activeItemEntry) {
        if (this.activeItemEntry != null) {
            this.activeItemEntry.setSelected(false);
        }

        this.activeItemEntry = activeItemEntry;
        this.activeItemEntry.setSelected(true);
        this.activeItemEntry.requestFocusInWindow();
    }

    public void resetActiveItemEntry() {
        if (this.activeItemEntry == null) { return; }
        this.activeItemEntry.setSelected(false);
        transferFocus();
        this.activeItemEntry = null;
    }
}
