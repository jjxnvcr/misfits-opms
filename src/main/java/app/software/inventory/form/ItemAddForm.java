package app.software.inventory.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.production.CategoryDao;
import app.db.dao.production.ItemDao;
import app.db.pojo.column.CategoryColumn;
import app.db.pojo.production.Category;
import app.db.pojo.production.Item;
import app.software.inventory.listing.InventoryList;
import app.software.inventory.listing.ItemEntry;

import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class ItemAddForm extends Form {
    private FlatTextField itemNameField;
    private FlatTextField itemDescField;
    private FlatComboBox<String> categoryField;
    private FlatTextField itemColorField;

    public ItemAddForm(InventoryList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Add Item");
        getHeader().setForeground(Palette.BLUE.color());

        FlatLabel itemNameLabel = createFieldLabel("Item Name");
        itemNameField = createField("");

        FlatLabel itemDescLabel = createFieldLabel("Item Description");
        itemDescField = createField("");

        FlatLabel categoryLabel = createFieldLabel("Category");

        categoryField = createComboBox();

        try {
            categoryField.addItem("Select Category");
            categoryField.setSelectedIndex(0);

            for (Category categoryPojo: CategoryDao.getAllCategoriesOrderBy(CategoryColumn.CategoryName.getName() + " " + Sort.ASC.getName())) {
                categoryField.addItem(categoryPojo.getCategoryName());
            }
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Categories");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load categories\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        FlatLabel itemColorLabel = createFieldLabel("Item Color");
        itemColorField = createField("");

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(itemNameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(itemNameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(itemDescLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(itemDescField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(categoryLabel, "grow, gapleft 5%");
        add(itemColorLabel, "wrap, span, grow, gapright 5%");
        add(categoryField, "grow, gapleft 5%, gapbottom 5%");
        add(itemColorField, "wrap, grow, gapright 5%, gapbottom 5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");

        add(createConfirmButton("Add", () -> {
            if (!validateFields(itemNameField, itemDescField, itemColorField) || !validateComboBox(categoryField)) {
                return;
        }
            PopupDialog confirm = new PopupDialog("Add Item");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to add this item?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    Item item = new Item(
                        CategoryDao.getCategoryByName(categoryField.getSelectedItem().toString()).getCategoryId(),
                        itemNameField.getText().trim(), 
                        itemDescField.getText().trim(), 
                        itemColorField.getText().trim()
                    );

                    ItemDao.addItem(item);

                    confirm.dispose();

                    PopupDialog notif = new PopupDialog("Item Added");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Item has been successfully added!");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();

                    Item addedItem = ItemDao.getLatestItem();
                    ItemEntry itemEntry = new ItemEntry(owner, addedItem);

                    owner.setActiveItemEntry(itemEntry);
                    owner.loadInventory();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Add Item");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to add item.\n\n Error: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    e.printStackTrace();
                }
            });
            confirm.display();
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
