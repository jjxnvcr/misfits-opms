package app.software.orders;

import app.components.Page;
import app.software.App;

import app.software.orders.listing.OrdersList;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class OrdersPage extends Page {
    public OrdersPage(App owner) {
        super(new MigLayout("fill", "[60%][40%]"));
        
        setArc(15);
        setBackground(Palette.MANTLE);

        add(new OrdersList(this), "grow");
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
