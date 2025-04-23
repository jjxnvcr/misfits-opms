package app.software.inventory.view;

import java.awt.Font;
import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.db.dao.production.ItemDao;
import app.db.dao.sales.SaleItemDao;
import app.db.pojo.production.Item;
import app.db.pojo.sales.SaleItem;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class ItemStats extends Page {
    public ItemStats(Object pojo) {
        super(new MigLayout("wrap 2, fillx"), false);

        swapPojo(pojo);
    }

    private Page createItemStatisticsCard(String text, String value) {
        Page page = new Page(new MigLayout("fillx, wrap"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel textLabel = new FlatLabel();
        textLabel.setText(text);
        textLabel.setForeground(Palette.SURFACE2.color());
        textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getSize() + 2));
        textLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        FlatLabel valueLabel = new FlatLabel();
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16));
        valueLabel.setForeground(Palette.GREEN.color());
        valueLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        valueLabel.setText(value);

        page.add(textLabel, "width 100%");
        page.add(valueLabel, "width 100%");

        return page;
    }

    public void swapPojo(Object pojo) {
        removeAll();

        FlatLabel label = new FlatLabel();
        label.setText("Item Statistics");
        label.setForeground(Palette.SUBTEXT0.color());

        add(label, "wrap");
        
        if (pojo instanceof Item) {
            try {
                add(createItemStatisticsCard("Total Transactions", String.format("%,d", ItemDao.getTotalTransactions(((Item) pojo).getItemId()))), "growx");
                add(createItemStatisticsCard("Total Purchases", String.format("%,d", ItemDao.getTotalPurchases(((Item) pojo).getItemId()))), "growx");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (pojo instanceof SaleItem) {
            try {
                add(createItemStatisticsCard("Total Transactions", String.format("%,d", SaleItemDao.getTotalTransactions(((SaleItem) pojo).getSaleItemId()))), "growx");
                add(createItemStatisticsCard("Total Purchases", String.format("%,d", SaleItemDao.getTotalPurchases(((SaleItem) pojo).getSaleItemId()))), "growx");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        revalidate();
        repaint();
    }
}
