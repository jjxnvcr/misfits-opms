package app.software.sales.view;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.StatsCard;
import app.db.dao.customer.CustomerDao;
import app.db.dao.sales.DeliveryDao;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.column.PaymentMethod;
import app.db.pojo.customer.Customer;
import app.db.pojo.sales.SalesTransaction;
import app.db.pojo.sales.TransactionItem;
import app.software.sales.listing.SalesList;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SaleView extends Page {
    public SaleView(SalesList owner, SalesTransaction sale) {
        super(new MigLayout("fillx, wrap"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        Page saleInfo = new Page(new MigLayout("insets 0, align 0%, fillx"), false);
        Page iconContainer = new Page(new MigLayout("insets 0, align 0%"), false);
        Page saleDetails = new Page(new MigLayout("fillx, wrap"), false);
        
        FlatLabel icon = new FlatLabel();
        icon.setIcon(new Iconify("cash-register", Palette.MAROON.color()).derive(60, 60));

        FlatLabel id = new FlatLabel();
        id.setText("Sale Transaction No. " + sale.getTransactionId());
        id.setForeground(Palette.TEXT.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD, 18));

        FlatLabel customer = new FlatLabel();
        customer.setForeground(Palette.SUBTEXT1.color());
        customer.setIcon(new Iconify("user-circle", Palette.SURFACE2.color()).derive(customer.getFont().getSize() + 4, customer.getFont().getSize() + 4));
        customer.setIconTextGap(5);
        
        try {
            Customer c = CustomerDao.getCustomerById(sale.getCustomerId());
            customer.setText(c.getFirstName() + " " + c.getLastName());
        } catch (Exception e) {
            e.printStackTrace();
            customer.setText("Unknown");
        }

        FlatLabel date = new FlatLabel();
        date.setText(new SimpleDateFormat("MMM dd, YYYY").format(new Date(sale.getTransactionDate().getTime())));
        date.setForeground(Palette.SUBTEXT1.color());
        date.setIcon(new Iconify("calendar", Palette.SURFACE2.color()).derive(date.getFont().getSize() + 4, customer.getFont().getSize() + 4));
        date.setIconTextGap(5);

        FlatLabel payment = new FlatLabel();
        payment.setText(sale.getPaymentType() + (sale.getPaymentType().equals(PaymentMethod.Cash.name()) ? "" : " - " + (
            sale.getPaymentType().equals(PaymentMethod.EWallet.name()) ? 
            sale.geteWalletType() : 
            sale.getBankName()
            ))
        );
        payment.setForeground(Palette.SUBTEXT1.color());
        payment.setIcon(new Iconify("cash", Palette.SURFACE2.color()).derive(payment.getFont().getSize() + 4, payment.getFont().getSize() + 4));
        payment.setIconTextGap(5);

        iconContainer.add(icon);

        saleDetails.add(id, "span, gapbottom 5");
        saleDetails.add(customer, "span, gapbottom 15");
        saleDetails.add(date, "span");
        saleDetails.add(payment, "span");

        if (!sale.getPaymentType().equals(PaymentMethod.Cash.name())) {
            FlatLabel reference = new FlatLabel();
            reference.setText(sale.getReferenceNumber());
            reference.setForeground(Palette.SUBTEXT1.color());
            reference.setIcon(new Iconify("receipt", Palette.SURFACE2.color()).derive(reference.getFont().getSize() + 4, reference.getFont().getSize() + 4));
            reference.setIconTextGap(5);

            saleDetails.add(reference, "span");
        }

        FlatLabel status = new FlatLabel();
        status.setForeground(Palette.SUBTEXT1.color());
        status.setIcon(new Iconify("truck", Palette.SURFACE2.color()).derive(status.getFont().getSize() + 4, status.getFont().getSize() + 4));

        status.setIconTextGap(5);
        try {
            status.setText(DeliveryDao.getDeliveryByTransactionId(sale.getTransactionId()).getDeliveryStatus());
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Unknown");
        }

        saleDetails.add(status, "span");

        saleInfo.add(iconContainer, "height 100%");
        saleInfo.add(saleDetails, "width 100%, grow");

        StatsCard stats = new StatsCard(2);
        List<TransactionItem> items = new ArrayList<>();

        try {
            items = SalesTransactionDao.getSalesTransactionItemsById(sale.getTransactionId());

            stats.addCard("Transaction Value", String.format("₱ %,.2f", SalesTransactionDao.getTransactionTotal(sale.getTransactionId())), Palette.PEACH.color());

            stats.addCard("Item Count", String.format("%,d", items.size()), Palette.MAUVE.color());

            stats.addCard("Item Purchases", String.format("%,d", SalesTransactionDao.getTransactionItemPurchases(sale.getTransactionId())), Palette.GREEN.color());
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(saleInfo, "growx");
        add(stats, "height 51%, grow");
        add(new SaleItemList(items), "grow");
    }
}
