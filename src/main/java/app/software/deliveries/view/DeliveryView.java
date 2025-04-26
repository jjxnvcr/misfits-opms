package app.software.deliveries.view;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.LabelWrap;
import app.components.Page;
import app.components.StatsCard;
import app.db.dao.customer.CustomerDao;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.column.PaymentMethod;
import app.db.pojo.customer.Customer;
import app.db.pojo.sales.Delivery;
import app.db.pojo.sales.SalesTransaction;
import app.software.deliveries.listing.DeliveryList;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class DeliveryView extends Page {
    public DeliveryView(DeliveryList owner, Delivery delivery) {
        super(new MigLayout("fillx, wrap"));
        
        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        Page deliveryInfo = new Page(new MigLayout("insets 0, align 0%, fillx"), false);
        Page iconContainer = new Page(new MigLayout("insets 0, align 0%"), false);
        Page deliveryDetails = new Page(new MigLayout("fillx, wrap"), false);
        Page customerField = new Page(new MigLayout("insets 0, align 0%"), false);
        Page customerIconPage = new Page(new MigLayout("insets 0, align 0%"), false);
        Page addressField = new Page(new MigLayout("insets 0, align 0%"), false);
        Page addressIconPage = new Page(new MigLayout("insets 0, align 0%"), false);

        FlatLabel icon = new FlatLabel();
        icon.setIcon(new Iconify("truck", Palette.YELLOW.color()).derive(60, 60));

        FlatLabel id = new FlatLabel();
        id.setText("Delivery No. " + delivery.getDeliveryId());
        id.setForeground(Palette.TEXT.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD, 18));

        FlatLabel transactionId = new FlatLabel();
        transactionId.setText("Transaction No. " + delivery.getTransactionId());
        transactionId.setForeground(Palette.SUBTEXT1.color());

        Customer customerObj = null;
        try {
            customerObj = CustomerDao.getCustomerById(SalesTransactionDao.getSalesTransaction(delivery.getTransactionId()).getCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        FlatLabel customerIcon = new FlatLabel();
        customerIcon.setIcon(new Iconify("user-circle", Palette.SURFACE2.color()).derive(customerIcon.getFont().getSize() + 4, customerIcon.getFont().getSize() + 4));

        LabelWrap customer = new LabelWrap(customerObj == null ? "Unknown" : customerObj.getFirstName() + " " + customerObj.getLastName(), transactionId.getFont().deriveFont(Font.PLAIN));
        customer.setForeground(Palette.SUBTEXT1.color());

        FlatLabel status = new FlatLabel();
        status.setText(delivery.getDeliveryStatus());
        status.setForeground(Palette.SUBTEXT1.color());
        status.setIcon(new Iconify("progress", Palette.SURFACE2.color()).derive(status.getFont().getSize() + 4, status.getFont().getSize() + 4));
        status.setIconTextGap(5);

        FlatLabel addressIcon = new FlatLabel();
        addressIcon.setIcon(new Iconify("map-pin", Palette.SURFACE2.color()).derive(addressIcon.getFont().getSize() + 4, addressIcon.getFont().getSize() + 4));

        LabelWrap address = new LabelWrap(delivery.getDeliveryAddress(), addressIcon.getFont().deriveFont(Font.PLAIN));

        addressIconPage.add(addressIcon);
        customerIconPage.add(customerIcon);

        addressField.add(addressIconPage);
        addressField.add(address, "width 100%, grow");

        customerField.add(customerIconPage);
        customerField.add(customer, "width 100%, grow");

        deliveryDetails.add(id, "growx, gapbottom 5, wrap");
        deliveryDetails.add(transactionId, "growx, gapbottom 15, wrap");
        deliveryDetails.add(customerField, "growx, wrap");
        deliveryDetails.add(addressField, "wrap");
        deliveryDetails.add(status, "wrap");

        iconContainer.add(icon);
        
        deliveryInfo.add(iconContainer, "height 100%");
        deliveryInfo.add(deliveryDetails, "height 100%, grow");

        StatsCard stats = new StatsCard(2);

        try {
            SalesTransaction sale = SalesTransactionDao.getSalesTransaction(delivery.getTransactionId());

            stats.addCard("Payment Method", sale.getPaymentType(), Palette.PEACH.color());

            stats.addCard("Payment Amount", String.format("₱ %,.2f", (sale.getPaymentType().equals(PaymentMethod.Cash.name()) ? delivery.getPaidAmount() : SalesTransactionDao.getTransactionTotal(sale.getTransactionId()))), Palette.MAUVE.color());
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(deliveryInfo, "growx");
        add(stats, "height 100%, grow");
    }
}
