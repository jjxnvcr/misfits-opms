package app.software.inventory.listing;

import java.awt.Cursor;
import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.PopupDialog;
import app.components.SortButton;
import app.db.dao.production.CategoryDao;
import app.db.pojo.column.CategoryColumn;
import app.db.pojo.column.ItemColumn;
import app.db.pojo.production.Category;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class InventorySortPanel extends Page {
    private InventoryList owner;
    
    public InventorySortPanel(InventoryList owner) {
        super(new MigLayout(""), false);

        this.owner = owner;

        FlatLabel label = new FlatLabel();
        label.setText("Sort by:");
        label.setForeground(Palette.SUBTEXT0.color());

        SortButton idSortButton = new SortButton(ItemColumn.ItemID, "ID");
        idSortButton.setAction(sortAction(idSortButton));

        SortButton itemNameSortButton = new SortButton(ItemColumn.ItemName, "Name");
        itemNameSortButton.setAction(sortAction(itemNameSortButton));

        FlatLabel category = new FlatLabel();
        category.setText("Category:");
        category.setForeground(Palette.SUBTEXT0.color());

        FlatComboBox<String> categoryComboBox = new FlatComboBox<>();
        categoryComboBox.setOpaque(false);
        categoryComboBox.setBackground(Palette.SURFACE0.color());
        categoryComboBox.setForeground(Palette.SUBTEXT1.color());
        categoryComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ClientProperty.setProperty(categoryComboBox, "popupBackground", Palette.SURFACE0.varString());
        ClientProperty.setProperty(categoryComboBox, "buttonBackground", null);
        ClientProperty.setProperty(categoryComboBox, "buttonStyle", "none");
        ClientProperty.setProperty(categoryComboBox, "arrowType", "triangle");
        ClientProperty.setProperty(categoryComboBox, "maximumRowCount", "5");
        categoryComboBox.addActionListener(e -> {
            owner.setSortCategory(categoryComboBox.getSelectedItem().toString());

            if (owner.getSearchPanel().isSearching()) {
                owner.loadCategoryInventory(owner.getSearchPanel().getSearchBar().getText());
            } else {
                owner.loadCategoryInventory();
            }
        });

        try {
            categoryComboBox.addItem("All");
            categoryComboBox.setSelectedIndex(0);

            for (Category categoryPojo: CategoryDao.getAllCategoriesOrderBy(CategoryColumn.CategoryName.getName() + " " + Sort.ASC.getName())) {
                categoryComboBox.addItem(categoryPojo.getCategoryName());
            }
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Categories");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load categories\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        add(category);
        add(categoryComboBox, "span, grow, wrap");

        add(label);
        add(idSortButton);
        add(itemNameSortButton);
    }

    private Runnable sortAction(SortButton button) {
        return () -> {
            if (button.getClicks() == 0) {
                owner.removeSort(button.getColumn());
            } else {
                owner.addSort(button.getColumn(), button.getClicks() == 1 ? Sort.ASC : Sort.DESC);
            }
            
            if (owner.getSearchPanel().isSearching()) {
                owner.loadCategoryInventory(owner.getSearchPanel().getSearchBar().getText());
            } else {
                owner.loadCategoryInventory();
            }
        };
    }
}
