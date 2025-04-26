package app.software.customers.listing;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ActionButton;
import app.components.ListEntry;
import app.db.pojo.customer.Customer;
import app.software.customers.form.CustomerDeleteConfirmation;
import app.software.customers.form.CustomerEditForm;
import app.software.customers.view.CustomerView;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CustomerEntry extends ListEntry {
    public CustomerEntry(CustomerList owner, Customer customer) {
        super(owner, customer);

        setLayout(new MigLayout("wrap, fill"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.SURFACE1.color());
        id.setText("Cst." + customer.getCustomerId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        FlatLabel name = new FlatLabel();
        name.setForeground(Palette.TEXT.color());
        name.setText(customer.getFirstName() + " " + customer.getLastName());
        name.setIcon(new Iconify("user-circle", Palette.MAUVE.color()).derive(name.getFont().getSize() + 4, name.getFont().getSize() + 4));
        name.setIconTextGap(5);
        
        setAction(() -> {
            if (owner.getActiveCustomerEntry() == this) {
                return;
            } 
            
            ActionButton activeButton = owner.getActionPanel().getActiveButton();
            owner.setActiveCustomerEntry(this);

            if (activeButton == owner.getActionPanel().getViewButton()) {
                owner.getOwner().loadActionView(new CustomerView(customer));
            } else if (activeButton == owner.getActionPanel().getAddButton()) {
                owner.getActionPanel().getDefaultButton().doClick();
                doClick();
            } else if (activeButton == owner.getActionPanel().getEditButton()) {
                owner.getOwner().loadActionView(new CustomerEditForm(owner));
            } else if (activeButton == owner.getActionPanel().getDeleteButton()) {
                if (new CustomerDeleteConfirmation(this).isConfirmed()) {
                    owner.getOwner().removeActionView();
                    owner.resetActiveCustomerEntry();
                }
            }
        });

        add(id);
        add(name);
    }
}
