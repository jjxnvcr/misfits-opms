package app.software.category.listing;

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
import app.db.dao.production.CategoryDao;
import app.db.pojo.column.Column;
import app.db.pojo.production.Category;
import app.software.category.CategoryPage;
import app.software.category.form.CategoryAddForm;
import app.software.category.form.CategoryDeleteConfirmation;
import app.software.category.form.CategoryEditForm;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class CategoryList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private CategoryPage owner;
    private ScrollView scrollView = new ScrollView(3);
    private ActionPanel actionPanel;
    private CategoryEntry activeCategoryEntry;

    public CategoryList(CategoryPage owner) {
        super(new MigLayout("fillx", "[grow][grow]"), false);

        this.owner = owner;

        actionPanel = new ActionPanel();

        initActionPanelButtons();

        add(new CategorySortPanel(this));
        add(actionPanel, "align 100%, pushx, wrap");
        add(new ScrollList(scrollView), "height 100%, span, grow");

        loadCategory();
    }

    private void initActionPanelButtons() {
        actionPanel.setButtonAction(
            actionPanel.getViewButton(), 
            () -> {
                resetActiveCategoryEntry();
                actionPanel.setActiveButton(actionPanel.getViewButton());
                owner.removeActionView();
            });

        actionPanel.setButtonAction(
            actionPanel.getAddButton(), 
            () -> {
                resetActiveCategoryEntry();
                actionPanel.setActiveButton(actionPanel.getAddButton());
                owner.loadActionView(new CategoryAddForm(this));
            });

        actionPanel.setButtonAction(actionPanel.getEditButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getEditButton());

            if (activeCategoryEntry != null) {
                PopupDialog confirm = new PopupDialog("Edit Category in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A category is currently in view. Would you like to edit it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveCategoryEntry();

                    PopupDialog notif = new PopupDialog("Select a Category to Edit");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a category to edit.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    owner.loadActionView(new CategoryEditForm(this));
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Category to Edit");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a category to edit.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });

         actionPanel.setButtonAction(actionPanel.getDeleteButton(),
         () -> {
            actionPanel.setActiveButton(actionPanel.getDeleteButton());
            
            if (activeCategoryEntry != null) {
                PopupDialog confirm = new PopupDialog("Delete Category in View");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("A category is currently in view. Would you like to delete it?");
                confirm.setCloseButtonAction(() -> {
                    confirm.dispose();
                    owner.removeActionView();
                    resetActiveCategoryEntry();

                    PopupDialog notif = new PopupDialog("Select a Category to Delete");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Please select a category to delete.");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();
                });
                confirm.setConfirmButtonAction(() -> {
                    confirm.dispose();

                    if (new CategoryDeleteConfirmation(activeCategoryEntry).isConfirmed()) {
                        owner.removeActionView();
                        resetActiveCategoryEntry();
                    }
                });
                confirm.display();
            } else {
                PopupDialog notif = new PopupDialog("Select a Category to Delete");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Please select a category to delete.");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
            }
         });
    }

    private void reloadActiveCategoryEntry() {
        for (Component entry: scrollView.getComponents()) {
            CategoryEntry categoryEntry = (CategoryEntry) entry;

            if (activeCategoryEntry != null && ((Category) activeCategoryEntry.getPojo()).getCategoryId() == ((Category) categoryEntry.getPojo()).getCategoryId()) {
                activeCategoryEntry = categoryEntry;
                activeCategoryEntry.setSelected(true);
                activeCategoryEntry.requestFocusInWindow();
            }
        }
    }

    public void loadCategory() {
        scrollView.removeAll();

        try {
            for (Category category : stringifySortMap().isBlank() ? CategoryDao.getAllCategories() : CategoryDao.getAllCategoriesOrderBy(stringifySortMap())) {
                scrollView.add(new CategoryEntry(this, category), "grow");
            } 
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Categories");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load categories\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        reloadActiveCategoryEntry();

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

    public CategoryPage getOwner() {
        return owner;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public CategoryEntry getActiveCategoryEntry() {
        return activeCategoryEntry;
    }

    public void setActiveCategoryEntry(CategoryEntry activeCategoryEntry) {
        if (this.activeCategoryEntry != null) {
            this.activeCategoryEntry.setSelected(false);
        }

        this.activeCategoryEntry = activeCategoryEntry;
        this.activeCategoryEntry.setSelected(true);
        this.activeCategoryEntry.requestFocusInWindow();
    }

    public void resetActiveCategoryEntry() {
        if (this.activeCategoryEntry == null) { return; }
        this.activeCategoryEntry.setSelected(false);
        transferFocus();
        this.activeCategoryEntry = null;
    }
}
