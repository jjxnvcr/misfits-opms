package app.software.deliveries;

import app.components.Page;
import app.software.App;
import app.software.deliveries.listing.DeliveryList;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class DeliveryPage extends Page {
    public DeliveryPage(App owner) {
        super(new MigLayout("fill", "[60%][40%]"));

        setArc(15);
        setBackground(Palette.MANTLE);

        add(new DeliveryList(this), "grow");
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
