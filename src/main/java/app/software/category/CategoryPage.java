package app.software.category;

import app.components.Page;
import app.software.App;
import app.software.category.listing.CategoryList;

import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CategoryPage extends Page {
    public CategoryPage(App owner) {
        super(new MigLayout("fill", "[65%][35%]"));
        
        setArc(15);
        setBackground(Palette.MANTLE);

        add(new CategoryList(this), "grow");
    }

    public void loadActionView(Page view) {
        removeActionView();
        
        add(view, "grow, height 100%");
        revalidate();
        repaint();
    }

    public void removeActionView() {
        if (getComponentCount() > 1) {
            remove(1);
            revalidate();
            repaint();
        }
    }
}
