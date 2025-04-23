package app.software.suppliers;

import app.components.Page;
import app.software.App;
import app.software.suppliers.listing.SupplierList;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SupplierPage extends Page {
    public SupplierPage(App owner) {
        super(new MigLayout("fill", "[65%][35%]"));

        setArc(15);
        setBackground(Palette.MANTLE);
        
        add(new SupplierList(this), "grow");
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
