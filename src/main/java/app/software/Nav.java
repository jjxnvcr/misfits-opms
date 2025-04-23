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
import app.software.category.CategoryPage;
import app.software.customers.CustomersPage;
import app.software.inventory.InventoryPage;
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

    private NavButton defaultButton;
    private NavButton activeButton;

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
        setActiveButton(defaultButton);
    }

    private void initButtons() {
        glanceButton = new NavButton(this, "", Palette.PEACH, "asterisk", "Glance");

        customersButton = new NavButton(this, CustomersPage.class.getName(), Palette.MAUVE, "users", "Customers");

        categoryButton = new NavButton(this, CategoryPage.class.getName(), Palette.GREEN, "category", "Category");

        inventoryButton = new NavButton(this, InventoryPage.class.getName(), Palette.BLUE, "basket", "Inventory");
        salesButton = new NavButton(this, "", Palette.MAROON, "coins", "Sales");

        deliveriesButton = new NavButton(this, "", Palette.YELLOW, "truck", "Deliveries");

        ordersButton = new NavButton(this, "", Palette.TEAL, "package", "Orders");

        suppliersButton = new NavButton(this, SupplierPage.class.getName(), Palette.PINK, "address-book", "Suppliers");
    }

    private void addButtons() {
        List<NavButton> buttons = List.of(glanceButton, customersButton, categoryButton, inventoryButton, salesButton, deliveriesButton, ordersButton, suppliersButton);

        for (NavButton button : buttons) {
            add(button);
        }

        add(toggleSidebarButton(), "align 0% 100%, pushy");
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

                for (NavButton button : List.of(glanceButton, customersButton, categoryButton, inventoryButton, salesButton, deliveriesButton, ordersButton, suppliersButton)) {
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
}