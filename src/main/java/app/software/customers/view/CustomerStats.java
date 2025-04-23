package app.software.customers.view;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.ScrollList;
import app.components.ScrollView;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.customer.Customer;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CustomerStats extends Page {
    private ScrollView view = new ScrollView(3);

    public CustomerStats(Object pojo) {
        super(new MigLayout("fillx", "[grow]"), false);

        FlatLabel label = new FlatLabel();
        label.setText("Customer Statistics");
        label.setForeground(Palette.SUBTEXT0.color());
        
        Customer customer = (Customer) pojo;

        add(label, "wrap");
        add(new ScrollList(view), "grow");

        try {
            view.add(createCustomerStatisticsCard("Total Spending", String.format("₱ %,.2f", CustomerDao.getCustomerTotalSpending(customer.getCustomerId()))), "growx");

            view.add(createCustomerStatisticsCard("Refund Total", String.format("₱ %,.2f", CustomerDao.getCustomerTotalSpending(customer.getCustomerId(), "Failed"))), "growx");

            view.add(createCustomerStatisticsCard("Complete Orders", String.format("%,d", CustomerDao.getCustomerDeliveryCount(customer.getCustomerId(), "Delivered"))), "growx");

            view.add(createCustomerStatisticsCard("Shipped Orders", String.format("%,d", CustomerDao.getCustomerDeliveryCount(customer.getCustomerId(), "Shipped"))), "growx");

            view.add(createCustomerStatisticsCard("Out for Delivery Orders", String.format("%,d", CustomerDao.getCustomerDeliveryCount(customer.getCustomerId(), "Out for Delivery"))), "growx");

            view.add(createCustomerStatisticsCard("Failed Orders", String.format("%,d", CustomerDao.getCustomerDeliveryCount(customer.getCustomerId(), "Failed"))), "growx");

            view.revalidate();
            view.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Page createCustomerStatisticsCard(String text, String value) {
        Page page = new Page(new MigLayout("fillx, wrap"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel textLabel = new FlatLabel();
        textLabel.setText(text);
        textLabel.setForeground(Palette.SURFACE2.color());
        textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getSize() + 2));
        textLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        FlatLabel valueLabel = new FlatLabel();
        valueLabel.setText(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16));
        valueLabel.setForeground(Palette.GREEN.color());
        valueLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        page.add(textLabel, "width 100%");
        page.add(valueLabel, "width 100%");

        return page;
    }
}
