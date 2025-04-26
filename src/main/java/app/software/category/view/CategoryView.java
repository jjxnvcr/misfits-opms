package app.software.category.view;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.LabelWrap;
import app.components.Page;
import app.components.StatsCard;
import app.db.dao.production.CategoryDao;
import app.db.pojo.production.Category;
import app.software.category.listing.CategoryList;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CategoryView extends Page {
    public CategoryView(CategoryList owner, Category category) {
        super(new MigLayout("fillx, wrap"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        Page categoryInfo = new Page(new MigLayout("fillx"), false);

        FlatLabel icon = new FlatLabel();
        icon.setIcon(new Iconify("category", Palette.GREEN.color()).derive(60, 60));

        LabelWrap name = new LabelWrap(category.getCategoryName(), getFont().deriveFont(Font.BOLD, 18));

        FlatLabel id = new FlatLabel();
        id.setText("Category No. " + category.getCategoryId());
        id.setForeground(Palette.SURFACE2.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        categoryInfo.add(icon, "wrap");
        categoryInfo.add(name, "growx, wrap");
        categoryInfo.add(id, "wrap");

        StatsCard stats = new StatsCard(3);
        
        try {
            stats.addCard("Category Value", String.format("₱ %,.2f", CategoryDao.getCategoryItemTotalPrice(category.getCategoryId())), Palette.PEACH.color());

            stats.addCard("Item Count", String.format("%,d", CategoryDao.getCategoryItemsCount(category.getCategoryId())), Palette.MAUVE.color());
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(categoryInfo, "growx");
        add(stats, "growx");
    }
}
