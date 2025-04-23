package app.software.customers.view;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.LabelWrap;
import app.components.Page;

import app.db.pojo.customer.Customer;
import app.utils.Iconify;
import app.utils.Palette;

import net.miginfocom.swing.MigLayout;

public class CustomerView extends Page {
    private TransactionList transactionList;

    public CustomerView(Customer customer) {
        super(new MigLayout("wrap, fillx"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(5);

        Page customerInfo = new Page(new MigLayout("fillx"), false);
        Page customerIcon = new Page(new MigLayout("align 0%"), false);
        Page customerDetails = new Page(new MigLayout("wrap, fillx"), false);

        FlatLabel icon = new FlatLabel();
        icon.setIcon(new Iconify("user-circle", Palette.MAUVE.color()).derive(60, 60));
        
        LabelWrap name = new LabelWrap(customer.getFirstName() + " " + customer.getLastName(), getFont().deriveFont(Font.BOLD, 18));

        FlatLabel id = new FlatLabel();
        id.setText("Customer No. " + customer.getCustomerId());
        id.setForeground(Palette.SURFACE2.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        FlatLabel contact = new FlatLabel();
        contact.setText(customer.getContactNumber());
        contact.setForeground(Palette.SUBTEXT1.color());
        contact.setIcon(new Iconify("phone", Palette.SURFACE2.color()).derive(contact.getFont().getSize() + 4, contact.getFont().getSize() + 4));
        contact.setIconTextGap(8);

        LabelWrap address = new LabelWrap((customer.getStreet() == null ? "" : customer.getStreet()) + ", " + (customer.getBarangay() == null ? "" : customer.getBarangay()) + ", " + (customer.getCity() == null ? "" : customer.getCity()) + ", " + (customer.getProvince() == null ? "" : customer.getProvince()), getFont());
        address.setForeground(Palette.SUBTEXT1.color());

        FlatLabel locationIcon = new FlatLabel();
        locationIcon.setIcon(new Iconify("map-pin", Palette.SURFACE2.color()).derive(address.getFont().getSize() + 4, address.getFont().getSize() + 4));

        Page addressField = new Page(new MigLayout("insets 0, align 0%"), false);

        Page locationIconPage = new Page(new MigLayout("insets 0, align 0%"), false);

        locationIconPage.add(locationIcon);

        addressField.add(locationIconPage);
        addressField.add(address, "width 100%, grow");

        customerDetails.add(name, "span, growx, gapbottom 5, wrap");
        customerDetails.add(id, "span, wrap, gapbottom 15");
        customerDetails.add(contact, "span, wrap");
        customerDetails.add(addressField, "grow, wrap");

        customerIcon.add(icon);

        customerInfo.add(customerIcon, "height 100%");
        customerInfo.add(customerDetails, "height 100%, grow");

        transactionList = new TransactionList(customer);

        add(customerInfo, "growx");
        add(new CustomerStats(customer), "height 85%, grow");
        add(transactionList, "height 100%, grow");
    }
}
