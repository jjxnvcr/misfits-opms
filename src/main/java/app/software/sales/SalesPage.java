package app.software.sales;

import app.components.Page;
import app.software.App;
import app.software.sales.listing.SalesList;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SalesPage extends Page {
    private App owner;

    public SalesPage(App owner) {
        super(new MigLayout("fill", "[60%][40%]"));

        this.owner = owner;

        setArc(15);
        setBackground(Palette.MANTLE);

        add(new SalesList(this), "grow");
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

    public App getOwner() {
        return owner;
    }
}
