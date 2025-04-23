package app.software.inventory;

import app.components.Page;
import app.software.App;
import app.software.inventory.listing.InventoryList;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class InventoryPage extends Page {
    public InventoryPage(App owner) {
        super(new MigLayout("fill", "[65%][35%]"));
        
        setArc(15);
        setBackground(Palette.MANTLE);

        add(new InventoryList(this), "grow");
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
