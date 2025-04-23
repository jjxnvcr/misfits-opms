package app.software.customers;

import app.components.Page;
import app.software.App;
import app.software.customers.listing.CustomerList;

import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CustomersPage extends Page {
    public CustomersPage(App owner) {
        super(new MigLayout("fill", "[65%][35%]"));
        
        setArc(15);
        setBackground(Palette.MANTLE);

        add(new CustomerList(this), "grow");
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
