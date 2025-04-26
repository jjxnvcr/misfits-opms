package app.software.cashier;

import java.awt.Component;

import app.components.Page;
import app.software.App;
import app.software.cashier.listing.CashierList;
import app.software.cashier.view.CartView;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CashierPage extends Page {
    public CashierPage(App owner) {
        super(new MigLayout("fill", "[70%][30%]"));
        
        setArc(15);
        setBackground(Palette.MANTLE);

        CashierList cashierList = new CashierList(this);

        add(cashierList, "grow");
        add(new CartView(cashierList), "height 100%, grow");
    }

    public CartView getCartView() {
        return (CartView) getComponent(1);
    }

    public void loadActionView(Component component) {
        removeActionView();

        add(component, "grow, height 100%");

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
