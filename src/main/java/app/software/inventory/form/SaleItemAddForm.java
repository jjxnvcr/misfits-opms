package app.software.inventory.form;

import java.sql.SQLException;

import javax.swing.SpinnerNumberModel;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.Page;
import app.components.PopupDialog;

import app.db.dao.production.ItemDao;
import app.db.dao.sales.SaleItemDao;
import app.db.pojo.column.AlphaSize;
import app.db.pojo.column.Measurement;
import app.db.pojo.production.Item;
import app.db.pojo.sales.SaleItem;
import app.software.inventory.listing.InventoryList;
import app.software.inventory.listing.ItemEntry;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SaleItemAddForm extends Form {
    private FlatComboBox<String> alphaSizeField;
    private FlatSpinner numericSizeField;
    private FlatSpinner priceField;
    private FlatSpinner stockField;
    private Page details;

    public SaleItemAddForm(InventoryList owner, Item item) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Add Item Variant");
        getHeader().setForeground(Palette.BLUE.color());

        FlatLabel itemNameLabel = createFieldLabel("Item Name");
        FlatTextField itemNameField = createField(item.getItemName());
        itemNameField.setEditable(false);

        FlatLabel measurementLabel = createFieldLabel("Measurement");
        FlatComboBox<String> measurementField = createComboBox();

        FlatLabel sizeLabel = createFieldLabel("Size");

        measurementField.addItem("One Size");
        for (Measurement measurement: Measurement.values()) {
            measurementField.addItem(measurement.name());
        }
        details = new Page(new MigLayout("fillx", "[50%]3%[50%]"), false);

        measurementField.addActionListener(e -> {
            details.removeAll();

            boolean oneSize = measurementField.getSelectedItem().toString().equals("One Size");

            if (!oneSize && measurementField.getSelectedItem().toString().equals(Measurement.Alpha.name())) {
                details.add(sizeLabel, "grow");

                alphaSizeField = createComboBox();

                for (AlphaSize alphaSize: AlphaSize.values()) {
                    alphaSizeField.addItem(alphaSize.name());
                }
                alphaSizeField.setSelectedItem(AlphaSize.M.name());
                
                
                details.add(alphaSizeField, "wrap, grow");
            } else {
                if (!oneSize) {
                    details.add(sizeLabel, "grow");

                    numericSizeField = createSpinner(new SpinnerNumberModel(7, 1, 100, 1));

                    details.add(numericSizeField, "wrap, grow");
                }
            }

            FlatLabel priceLabel = createFieldLabel("Price");

            priceField = createSpinner(new SpinnerNumberModel(1, 1, 100000, .5));

            FlatLabel stockLabel = createFieldLabel("Stock");

            stockField = createSpinner(new SpinnerNumberModel(1, 1, 100, 1));

            details.add(priceLabel, "grow");
            details.add(priceField, "wrap, grow");
            details.add(stockLabel, "grow");
            details.add(stockField, "wrap, grow");

            details.revalidate();
            details.repaint();
        });

        measurementField.setSelectedIndex(0);

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(itemNameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(itemNameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(measurementLabel, "grow, gapleft 5%");
        add(measurementField, "wrap, grow, gapright 5%");
        add(details, "wrap, grow, span, gapleft 3%, gapright 3%, gapbottom 5%");
        add(createCancelButton(() -> {
            ItemEntry entry = owner.getActiveItemEntry();
            owner.getActionPanel().getDefaultButton().doClick();
            entry.doClick();
        }), "grow, gapbottom 5%, gapleft 5%");
        add(createConfirmButton("Add", () -> {
            PopupDialog confirm = new PopupDialog("Add Item Variant");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to add this item variant?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    int numericSize = measurementField.getSelectedItem().toString().equals("One Size") || measurementField.getSelectedItem().toString().equals(Measurement.Alpha.name()) ? 0 : (int) numericSizeField.getValue();

                    SaleItem newSaleItem = new SaleItem(
                        item.getItemId(),
                        (double) priceField.getValue(),
                        (int) stockField.getValue(),
                        measurementField.getSelectedItem().toString().equals("One Size") ? null : measurementField.getSelectedItem().toString(),
                        numericSize,
                        numericSize != 0 ? alphaSizeField.getSelectedItem().toString() : null
                        );

                    SaleItemDao.addSaleItem(newSaleItem);
                    
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
                    PopupDialog error = new PopupDialog("Unable to Add Item Variant");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to add item variant.\n\n Error: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    e.printStackTrace();
                }
            });
            confirm.display();
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
