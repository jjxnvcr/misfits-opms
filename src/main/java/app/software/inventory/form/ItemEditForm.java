package app.software.inventory.form;

import java.awt.Cursor;
import java.util.List;

import javax.swing.SpinnerNumberModel;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.Page;
import app.components.PopupDialog;
import app.db.dao.production.CategoryDao;
import app.db.dao.production.ItemDao;
import app.db.dao.production.StockDao;
import app.db.dao.sales.SaleItemDao;
import app.db.pojo.column.CategoryColumn;
import app.db.pojo.production.Category;
import app.db.pojo.production.Item;
import app.db.pojo.sales.SaleItem;
import app.software.inventory.listing.InventoryList;

import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class ItemEditForm extends Form {
    private FlatTextField itemNameField;
    private FlatTextField itemDescField;
    private FlatComboBox<String> categoryField;
    private FlatTextField itemColorField;
    private FlatButton activeSizeButton;
    private SaleItem activeSaleItem;
    private FlatLabel priceLabel;
    private FlatLabel stockLabel;
    private FlatSpinner priceField;
    private FlatSpinner stockField;

    public ItemEditForm(InventoryList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Edit Item");
        getHeader().setForeground(Palette.BLUE.color());

        Item item = (Item) owner.getActiveItemEntry().getPojo();

        FlatLabel itemNameLabel = createFieldLabel("Item Name");
        itemNameField = createField(item.getItemName());

        FlatLabel itemDescLabel = createFieldLabel("Item Description");
        itemDescField = createField(item.getItemDescription());

        FlatLabel categoryLabel = createFieldLabel("Category");

        categoryField = createComboBox();

        try {
            for (Category categoryPojo: CategoryDao.getAllCategoriesOrderBy(CategoryColumn.CategoryName.getName() + " " + Sort.ASC.getName())) {
                categoryField.addItem(categoryPojo.getCategoryName());
                if (categoryPojo.getCategoryId() == item.getCategoryId()) {
                    categoryField.setSelectedItem(categoryPojo.getCategoryName());
                }
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
        itemColorField = createField(item.getItemColor());

        Page itemSizesPanel = new Page(new MigLayout(), false);
        FlatLabel itemSizeLabel = createFieldLabel("Sizes");

        itemSizesPanel.add(itemSizeLabel);

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(itemNameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(itemNameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(itemDescLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(itemDescField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(categoryLabel, "grow, gapleft 5%");
        add(itemColorLabel, "wrap, span, grow, gapright 5%");
        add(categoryField, "grow, gapleft 5%, gapbottom .5%");
        add(itemColorField, "wrap, grow, gapright 5%, gapbottom .5%");
        add(itemSizesPanel, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(sizeEditPanel(itemSizesPanel, item), "span, grow, gapleft 5%, gapright 5%, gapbottom 5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");

        add(createConfirmButton("Edit", () -> {
            PopupDialog confirm = new PopupDialog("Edit Item");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to edit this item?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    String itemName = itemNameField.getText().trim();
                    String itemCategory = categoryField.getSelectedItem().toString();
                    String itemDesc = itemDescField.getText().trim();
                    String itemColor = itemColorField.getText().trim();

                    item.setItemName(itemName.isBlank() ? itemNameField.getPlaceholderText() : itemName);
                    item.setItemDescription(itemDesc.isBlank() ? itemDescField.getPlaceholderText() : itemDesc);
                    item.setItemColor(itemColor.isBlank() ? itemColorField.getPlaceholderText() : itemColor);
                    int categoryId = item.getCategoryId();
                    if (categoryId == 0) {
                        categoryId = CategoryDao.getCategoryByName(itemCategory).getCategoryId();
                    } else if (!CategoryDao.getCategoryById(categoryId).getCategoryName().equals(itemCategory)) {
                        categoryId = CategoryDao.getCategoryByName(itemCategory).getCategoryId();
                    }
                    item.setCategoryId(categoryId);

                    if (priceField != null) {
                        if (
                            ((double) priceField.getValue()) != activeSaleItem.getItemPrice() ||
                            ((int) stockField.getValue()) != activeSaleItem.getItemQuantity()) {
                            activeSaleItem.setItemPrice((double) priceField.getValue());
                            activeSaleItem.setItemQuantity((int) stockField.getValue());
    
                            SaleItemDao.updateSaleItem(activeSaleItem);
                        }
                    }

                    ItemDao.updateItem(item);

                    confirm.dispose();

                    PopupDialog notif = new PopupDialog("Item Edited");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Item has been successfully edited!");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();

                    owner.loadInventory();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Edit Item");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Unable to edit item\n" + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                }
            });

            confirm.display();
        }), "grow, gapright 5%, gapbottom 5%");
    }
    
    private Page sizeEditPanel(Page sizesContainer, Item item) {
        Page page = new Page(new MigLayout("align 50%, fill", "[50%]3%[50%]"), false);

        try {
            List<SaleItem> saleItems = SaleItemDao.getAllSaleItemsByItemId(item.getItemId());

            for (SaleItem saleItem: saleItems) {
                FlatButton size = new FlatButton();
                size.setBackground(Palette.CRUST.color());
                size.setForeground(Palette.SUBTEXT1.color());
                size.setCursor(new Cursor(Cursor.HAND_CURSOR));

                size.addActionListener(e -> {
                    if (activeSizeButton != null) {
                        activeSizeButton.setSelected(false);

                        page.remove(priceLabel);
                        page.remove(priceField);
                        page.remove(stockLabel);
                        page.remove(stockField);
                    } 
                    if (activeSizeButton == size) {
                        activeSizeButton = null;

                        page.revalidate();
                        page.repaint();
                        return;
                    }

                    activeSizeButton = size;
                    activeSizeButton.setSelected(true);
                    activeSizeButton.requestFocusInWindow();

                    priceLabel = createFieldLabel("Price");

                    priceField = createSpinner(new SpinnerNumberModel(1, 1, 100000, .5));
                    priceField.setValue(saleItem.getItemPrice());

                    stockLabel = createFieldLabel("Stock");

                    stockField = createSpinner(new SpinnerNumberModel(1, 0, 100, 1));
                    stockField.setValue(saleItem.getItemQuantity());
                    stockField.addChangeListener(c -> {
                        try {
                            if (Integer.parseInt(stockField.getValue().toString()) > StockDao.getItemAvailableStock(item.getItemId())) {
                                setFeedback("Stock exceeds the available stock!");
                                stockField.setValue(stockField.getPreviousValue());
                                return;
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });

                    activeSaleItem = saleItem;

                    page.add(priceLabel);
                    page.add(stockLabel, "wrap");
                    page.add(priceField, "grow");
                    page.add(stockField, "grow, wrap");

                    page.revalidate();
                    page.repaint();
                });

                if (saleItems.size() == 1) {
                    size.doClick();
                }
                
                if (saleItem.getMeasurementSystem() == null) {
                    size.setText("One Size");
                } else {
                    if (!saleItem.getMeasurementSystem().equals("Alpha")) {
                        size.setText(saleItem.getMeasurementSystem() + " " + saleItem.getNumericSize());
                    } else {
                        size.setText(saleItem.getAlphaSize());
                    }
                }

                sizesContainer.add(size);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return page;
    }
}
