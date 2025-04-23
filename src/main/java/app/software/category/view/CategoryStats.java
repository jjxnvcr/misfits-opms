package app.software.category.view;

import java.awt.Font;
import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.db.dao.production.CategoryDao;
import app.db.pojo.production.Category;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CategoryStats extends Page {
    public CategoryStats(Object pojo) {
        super(new MigLayout("wrap 2, fillx"), false);

        FlatLabel label = new FlatLabel();
        label.setText("Category Statistics");
        label.setForeground(Palette.SUBTEXT0.color());

        Category category = (Category) pojo;

        add(label, "wrap");
        try {
            add(createCategoryStatisticsCard("Category Value", String.format("₱ %,.2f", CategoryDao.getCategoryItemTotalPrice(category.getCategoryId()))), "growx");

            add(createCategoryStatisticsCard("Item Count", String.format("%,d", CategoryDao.getCategoryItemsCount(category.getCategoryId()))), "growx");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Page createCategoryStatisticsCard(String text, String value) {
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
        valueLabel.setText(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16));
        valueLabel.setForeground(Palette.GREEN.color());
        valueLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        page.add(textLabel, "width 100%");
        page.add(valueLabel, "width 100%");

        return page;
    }
}
