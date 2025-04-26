package app.software;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JPanel;

import com.formdev.flatlaf.extras.components.FlatButton;

import app.components.NavButton;
import app.software.cashier.CashierPage;
import app.software.category.CategoryPage;
import app.software.customers.CustomersPage;
import app.software.deliveries.DeliveryPage;
import app.software.glance.GlancePage;
import app.software.inventory.InventoryPage;
import app.software.orders.OrdersPage;
import app.software.sales.SalesPage;
import app.software.suppliers.SupplierPage;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.NavState;
import app.utils.Palette;

import net.miginfocom.swing.MigLayout;

public class Nav extends JPanel {
    private NavState state = NavState.EXPAND;

    private NavButton glanceButton;
    private NavButton customersButton;
    private NavButton categoryButton;
    private NavButton inventoryButton;
    private NavButton salesButton;
    private NavButton deliveriesButton;
    private NavButton ordersButton;
    private NavButton suppliersButton;
    private NavButton cashierButton;
    private NavButton activeButton;
    private NavButton defaultButton;

    private App owner;

    public Nav(App owner) {
        super();

        this.owner = owner;

        setLayout(new MigLayout("wrap, align 0% 10%, fillx", "10[]10", "1.5%[]1.5%"));
        
        ClientProperty.setProperty(
            this, 
            "background", 
            String.format("darken(%s, %s)", Palette.BASE.varString(), "4%")
        );

        ClientProperty.setProperty(
            this, 
            "arc", 
            "10"
        );

        initButtons();
        addButtons();

        defaultButton = glanceButton;
    }

    private void initButtons() {
        glanceButton = new NavButton(this, GlancePage.class.getName(), Palette.PEACH, "asterisk", "Glance");

        customersButton = new NavButton(this, CustomersPage.class.getName(), Palette.MAUVE, "users", "Customers");

        categoryButton = new NavButton(this, CategoryPage.class.getName(), Palette.GREEN, "category", "Category");

        inventoryButton = new NavButton(this, InventoryPage.class.getName(), Palette.BLUE, "basket", "Inventory");

        salesButton = new NavButton(this, SalesPage.class.getName(), Palette.MAROON, "coins", "Sales");

        deliveriesButton = new NavButton(this, DeliveryPage.class.getName(), Palette.YELLOW, "truck", "Deliveries");

        ordersButton = new NavButton(this, OrdersPage.class.getName(), Palette.TEAL, "package", "Orders");

        suppliersButton = new NavButton(this, SupplierPage.class.getName(), Palette.PINK, "address-book", "Suppliers");

        cashierButton = new NavButton(this, CashierPage.class.getName(), Palette.LAVENDER, "cash-register", "Cashier");
    }

    private void addButtons() {
        List<NavButton> buttons = List.of(glanceButton, customersButton, categoryButton, inventoryButton, salesButton, deliveriesButton, ordersButton, suppliersButton, cashierButton);

        for (NavButton button : buttons) {
            add(button);
        }

        add(toggleSidebarButton(), "align 0% 100%, pushy");
    }

    public NavButton getDefaulButton() {
        return defaultButton;
    }

    private FlatButton toggleSidebarButton() {
        FlatButton button = new FlatButton();
        button.setIcon(new Iconify("collapse", Palette.SURFACE2.color()));
        button.setToolTipText("Collapse sidebar");

        ClientProperty.setProperty(button, "arc", "15");

        button.setBackground(Palette.TRANSPARENT.color());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusable(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Palette.SURFACE0.color());
                button.setIcon(new Iconify("collapse", button.getParent().getBackground()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Palette.TRANSPARENT.color());
                button.setIcon(new Iconify("collapse", Palette.SURFACE0.color()));
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == NavState.EXPAND) {
                    state = NavState.COLLAPSE;
                    button.setIcon(new Iconify("expand", button.getParent().getBackground()));
                    button.setToolTipText("Expand sidebar");
                } else {
                    state = NavState.EXPAND;
                    button.setIcon(new Iconify("collapse", button.getParent().getBackground()));
                    button.setToolTipText("Collapse sidebar");
                }

                for (NavButton button : List.of(glanceButton, customersButton, categoryButton, inventoryButton, salesButton, deliveriesButton, ordersButton, suppliersButton, cashierButton)) {
                    button.toggle(state);
                }

                revalidate();
                repaint();
            }
        });

        return button;
    }

    public void setActiveButton(NavButton button) {
        if (activeButton != null) {
            activeButton.changeState(false);    
        }

        activeButton = button;
        activeButton.changeState(true);
    }

    public NavButton getActiveButton() {
        return activeButton;
    }

    public App getOwner() {
        return owner;
    }

    public NavButton getCashierButton() {
        return cashierButton;
    }
}