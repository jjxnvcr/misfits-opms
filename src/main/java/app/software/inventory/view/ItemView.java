package app.software.inventory.view;

import java.awt.Cursor;
import java.awt.Font;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.LabelWrap;
import app.components.Page;
import app.components.PopupDialog;
import app.components.StatsCard;
import app.db.dao.production.CategoryDao;
import app.db.dao.production.ItemDao;
import app.db.dao.production.StockDao;
import app.db.dao.sales.SaleItemDao;
import app.db.pojo.production.Item;
import app.db.pojo.sales.SaleItem;

import app.software.inventory.form.SaleItemAddForm;
import app.software.inventory.listing.InventoryList;
import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class ItemView extends Page {
    private List<SaleItem> saleItems;
    private StatsCard stats;
    private FlatButton activeSizeButton;

    public ItemView(InventoryList owner, Item item) {
        super(new MigLayout("fillx, wrap"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        Page itemView = new Page(new MigLayout("fillx", "[90%][10%]"), false);

        Page itemInfo = new Page(new MigLayout("fillx", "[50%][50%]"), false);

        FlatLabel icon = new FlatLabel();
        icon.setIcon(new Iconify("hanger", Palette.BLUE.color()).derive(60, 60));

        LabelWrap name = new LabelWrap(item.getItemName(), getFont().deriveFont(Font.BOLD, 18));

        FlatLabel id = new FlatLabel();
        id.setText("Item No. " + item.getItemId());
        id.setForeground(Palette.SURFACE2.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        LabelWrap description = new LabelWrap(item.getItemDescription(), getFont());
        description.setForeground(Palette.SUBTEXT0.color());

        FlatLabel category = new FlatLabel();
        category.setIcon(new Iconify("list-letters", Palette.SURFACE2.color()).derive(category.getFont().getSize() + 4, category.getFont().getSize() + 4));
        category.setIconTextGap(5);
        category.setForeground(Palette.SUBTEXT1.color());
        try {
            category.setText(CategoryDao.getCategoryById(item.getCategoryId()).getCategoryName());
        } catch (Exception e) {
            category.setText("Unknown");

            PopupDialog error = new PopupDialog("Unable to load Item Category");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load item category\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
        }

        FlatLabel price = new FlatLabel();
        price.setIconTextGap(5);
        price.setForeground(Palette.SUBTEXT0.color());

        FlatLabel color = new FlatLabel();
        color.setIcon(new Iconify("color-swatch", Palette.SURFACE2.color()).derive(color.getFont().getSize() + 4, color.getFont().getSize() + 4));
        color.setIconTextGap(5);
        color.setForeground(Palette.SUBTEXT1.color());
        color.setText(item.getItemColor());

        FlatLabel stock = new FlatLabel();
        stock.setIconTextGap(5);
        stock.setForeground(Palette.SUBTEXT0.color());

        itemInfo.add(icon, "span, wrap");
        itemInfo.add(name, "span, growx, gapbottom 5, wrap");
        itemInfo.add(id, "span, gapbottom 5, wrap");
        itemInfo.add(description, "span, growx, gapbottom 15, wrap");
        itemInfo.add(category);
        itemInfo.add(price, "wrap");
        itemInfo.add(color);
        itemInfo.add(stock, "wrap");

        itemView.add(itemInfo, "grow");
            
        stats = new StatsCard(2);
        reloadStats(item);

        try {
            saleItems = SaleItemDao.getAllSaleItemsByItemId(item.getItemId());

            Page sizePanel = new Page(new MigLayout("wrap, fillx"), false);

            FlatLabel sizeLabel = new FlatLabel();
            sizeLabel.setText("Sizes");
            sizeLabel.setForeground(Palette.SUBTEXT0.color());
            sizeLabel.setIcon(new Iconify("ruler", Palette.SURFACE2.color()).derive(sizeLabel.getFont().getSize() + 4, sizeLabel.getFont().getSize() + 4));
            
            FlatButton addSize = new FlatButton();
            addSize.setBackground(Palette.CRUST.color());
            addSize.setForeground(Palette.SUBTEXT1.color());
            addSize.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addSize.setText("Add");
            addSize.addActionListener(e -> {
                owner.getActionPanel().setActiveButton(owner.getActionPanel().getAddButton());
                owner.getOwner().loadActionView(new SaleItemAddForm(owner, item));
            });

            sizePanel.add(sizeLabel, "align 50%, gapbottom 15");
            sizePanel.add(addSize, "gapbottom 15");

            for (SaleItem saleItem : saleItems) {
                FlatButton size = new FlatButton();
                size.setBackground(Palette.CRUST.color());
                size.setForeground(Palette.SUBTEXT1.color());
                size.setCursor(new Cursor(Cursor.HAND_CURSOR));

                if (saleItem.getMeasurementSystem() == null) {
                    activeSizeButton = size;
                    activeSizeButton.setSelected(true);
                    activeSizeButton.requestFocusInWindow();

                    size.setText("One Size");

                    price.setText(String.format("%,.2f", saleItem.getItemPrice()));
                    price.setIcon(new Iconify("peso", Palette.SURFACE2.color()).derive(price.getFont().getSize() + 4, price.getFont().getSize() + 4));

                    stock.setText(String.format("%,d left", saleItem.getItemQuantity()));
                    stock.setIcon(new Iconify("package", Palette.SURFACE2.color()).derive(stock.getFont().getSize() + 4, stock.getFont().getSize() + 4));
                } else {
                    if (!saleItem.getMeasurementSystem().equals("Alpha")) {
                        DecimalFormat sizeFormat = new DecimalFormat();
                        sizeFormat.setMaximumFractionDigits(1);
                        sizeFormat.setMinimumFractionDigits(0);

                        size.setText(saleItem.getMeasurementSystem() + " " + sizeFormat.format(saleItem.getNumericSize()));
                    } else {
                        size.setText(saleItem.getAlphaSize());
                    }

                    size.addActionListener(e -> {
                        if (activeSizeButton != null) {
                            activeSizeButton.setSelected(false);
                        } 
                        if (activeSizeButton == size) {
                            activeSizeButton = null;

                            name.setText(item.getItemName());

                            price.setText("");
                            price.setIcon(null);
                            
                            stock.setText("");
                            stock.setIcon(null);

                            reloadStats(item);
                            return;
                        } 

                        activeSizeButton = size;
                        activeSizeButton.setSelected(true);
                        activeSizeButton.requestFocusInWindow();

                        name.setText(item.getItemName() + " (" + size.getText() + ")");

                        price.setText(String.format("%,.2f", saleItem.getItemPrice()));
                        price.setIcon(new Iconify("peso", Palette.SURFACE2.color()).derive(price.getFont().getSize() + 4, price.getFont().getSize() + 4));
                        
                        stock.setText(String.format("%,d left", saleItem.getItemQuantity()));
                        stock.setIcon(new Iconify("package", Palette.SURFACE2.color()).derive(stock.getFont().getSize() + 4, stock.getFont().getSize() + 4));

                        reloadStats(saleItem);
                    });
                }

                sizePanel.add(size, "growx");
            }

            itemView.add(sizePanel, "grow, wrap");
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Item Variation");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load item variations\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            e.printStackTrace();
            return;
        }

        add(itemView, "grow");
        add(stats, "grow");
    }

    private void reloadStats(Object item) {
        stats.removeCards();

        if (item instanceof SaleItem) {
            try {
                stats.addCard("Total Sales", String.format("%,d", SaleItemDao.getTotalTransactions(((SaleItem) item).getSaleItemId())), Palette.PEACH.color());
    
                stats.addCard("Total Purchases", String.format("%,d", SaleItemDao.getTotalPurchases(((SaleItem) item).getSaleItemId())), Palette.MAUVE.color());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                stats.addCard("Total Stock", String.format("%,d", StockDao.getItemTotalStock(((Item) item).getItemId())), Palette.PEACH.color());

                stats.addCard("Remaining Stock", String.format("%,d", StockDao.getItemAvailableStock(((Item) item).getItemId())), Palette.MAUVE.color());

                stats.addCard("Total Sales", String.format("%,d", ItemDao.getTotalTransactions(((Item) item).getItemId())), Palette.GREEN.color());
    
                stats.addCard("Total Purchases", String.format("%,d", ItemDao.getTotalPurchases(((Item) item).getItemId())), Palette.BLUE.color());
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }

        stats.reloadCards();
    }
}
