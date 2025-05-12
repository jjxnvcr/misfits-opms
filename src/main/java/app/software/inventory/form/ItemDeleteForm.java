package app.software.inventory.form;

import java.sql.SQLException;
import java.util.List;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;

import app.components.PopupDialog;
import app.db.dao.production.CategoryDao;
import app.db.dao.production.ItemDao;
import app.db.dao.sales.SaleItemDao;
import app.db.pojo.column.Measurement;
import app.db.pojo.production.Item;
import app.db.pojo.sales.SaleItem;
import app.software.inventory.listing.InventoryList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class ItemDeleteForm extends Form {
    public ItemDeleteForm(InventoryList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Delete Item");
        getHeader().setForeground(Palette.BLUE.color());

        Item item = (Item) owner.getActiveItemEntry().getPojo();

        FlatLabel itemNameLabel = createFieldLabel("Item Name");
        FlatTextField itemNameField = createField(item.getItemName());
        itemNameField.setEditable(false);

        FlatLabel itemCategoryLabel = createFieldLabel("Category");
        FlatTextField itemCategoryField = new FlatTextField();

        try {
            itemCategoryField = createField(CategoryDao.getCategoryById(item.getCategoryId()).getCategoryName());
        } catch (Exception e) {
            itemCategoryField = createField("Unknown");
        }
        itemCategoryField.setEditable(false);

        FlatLabel itemSizeLabel = createFieldLabel("Sizes");

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(itemNameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(itemNameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(itemCategoryLabel, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(itemCategoryField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(itemSizeLabel, "wrap, span, grow, gapleft 5%, gapright 5%");

        try {
            List<SaleItem> saleItems = SaleItemDao.getAllSaleItemsByItemId(item.getItemId());

            for (SaleItem saleItem: saleItems) {
                String text = saleItem.getMeasurementSystem() == null ? "One Size" :saleItem.getMeasurementSystem().equals(Measurement.Alpha.name()) ? saleItem.getAlphaSize() : saleItem.getMeasurementSystem() + " " + saleItem.getNumericSize();

                FlatTextField size = createField(text);
                size.setEditable(false);

                FlatButton delButton = createConfirmButton("Delete", () -> {
                    PopupDialog confirm = new PopupDialog("Delete Item Variant");
                    confirm.setDialogType(DialogType.CONFIRMATION);
                    confirm.setMessage("Would you like to delete this item variant?");
                    confirm.setCloseButtonAction(() -> confirm.dispose());
                    confirm.setConfirmButtonAction(() -> {
                        confirm.dispose();
                        try {
                            SaleItemDao.deleteSaleItem(saleItem.getSaleItemId());
                            PopupDialog notif = new PopupDialog("Item Variant Deleted");
                            notif.setDialogType(DialogType.NOTIFICATION);
                            notif.setMessage("Item variant has been successfully deleted!");
                            notif.setCloseButtonAction(() -> {
                                notif.dispose();
                                revalidate();
                                repaint();
                            });
                            notif.display();
                        } catch (SQLException e) {
                            PopupDialog error = new PopupDialog("Unable to Delete Item Variant");
                            error.setDialogType(DialogType.ERROR);
                            error.setMessage("Something unexpected happened. Unable to delete item variant.\n\n Error: " + e.getMessage());
                            error.setCloseButtonAction(() -> error.dispose());
                            error.display();
                        }
                    });
                    confirm.display();
                });
                delButton.setBackground(Palette.SURFACE0.color());
                delButton.setForeground(Palette.SUBTEXT0.color());

                add(size, "grow, gapleft 5%");
                add(delButton, "wrap, grow, gapright 5%");
            }
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to Delete Item Variant");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Something unexpected happened. Unable to delete item variant.\n\n Error: " + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
        }

        add(createCancelButton(() -> {owner.getActionPanel().getDefaultButton().doClick();}), "grow, gapleft 5%, gaptop 5%, gapbottom 5%");

        add(createConfirmButton("Delete", () -> {
            PopupDialog confirm = new PopupDialog("Delete Item");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Would you like to delete this item?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                confirm.dispose();
                try {
                    ItemDao.deleteItem(item.getItemId());
                    PopupDialog notif = new PopupDialog("Item Deleted");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Item has been successfully deleted!");
                    notif.setCloseButtonAction(() -> {
                        notif.dispose();
                    });
                    notif.display();

                    owner.resetActiveItemEntry();
                    owner.getActionPanel().getDefaultButton().doClick();
                    owner.loadInventory();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Delete Item");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to delete item.\n\n Error: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                }
            });
            confirm.display();
        }), "grow, gapright 5%, gaptop 5%, gapbottom 5%");
    }
}
