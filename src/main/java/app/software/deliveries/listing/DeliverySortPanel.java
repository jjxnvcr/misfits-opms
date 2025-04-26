package app.software.deliveries.listing;

import java.awt.Cursor;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.PopupDialog;
import app.components.SortButton;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.column.CustomerColumn;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.column.SupplyOrderColumn;
import app.db.pojo.customer.Customer;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class DeliverySortPanel extends Page {
    private DeliveryList owner;

    public DeliverySortPanel(DeliveryList owner) {
        super(new MigLayout(""), false);

        this.owner = owner;

        FlatLabel label = new FlatLabel();
        label.setText("Sort by:");
        label.setForeground(Palette.SUBTEXT0.color());

        SortButton idSortButton = new SortButton(SupplyOrderColumn.OrderID, "ID");
        idSortButton.setAction(sortAction(idSortButton));

        SortButton dateSortButton = new SortButton(SupplyOrderColumn.OrderDate, "Date");
        dateSortButton.setAction(sortAction(dateSortButton));

        FlatLabel customerLabel = new FlatLabel();
        customerLabel.setText("Customer:");
        customerLabel.setForeground(Palette.SUBTEXT0.color());

        FlatComboBox<String> customerComboBox = new FlatComboBox<>();
        customerComboBox.setOpaque(false);
        customerComboBox.setBackground(Palette.SURFACE0.color());
        customerComboBox.setForeground(Palette.SUBTEXT1.color());
        customerComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        FlatLabel statusLabel = new FlatLabel();
        statusLabel.setText("Status:");
        statusLabel.setForeground(Palette.SUBTEXT0.color());

        FlatComboBox<String> statusComboBox = new FlatComboBox<>();
        statusComboBox.setOpaque(false);
        statusComboBox.setBackground(Palette.SURFACE0.color());
        statusComboBox.setForeground(Palette.SUBTEXT1.color());
        statusComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        ClientProperty.setProperty(customerComboBox, "popupBackground", Palette.SURFACE0.varString());
        ClientProperty.setProperty(customerComboBox, "buttonBackground", null);
        ClientProperty.setProperty(customerComboBox, "buttonStyle", "none");
        ClientProperty.setProperty(customerComboBox, "arrowType", "triangle");
        ClientProperty.setProperty(customerComboBox, "maximumRowCount", "5");

        ClientProperty.setProperty(statusComboBox, "popupBackground", Palette.SURFACE0.varString());
        ClientProperty.setProperty(statusComboBox, "buttonBackground", null);
        ClientProperty.setProperty(statusComboBox, "buttonStyle", "none");
        ClientProperty.setProperty(statusComboBox, "arrowType", "triangle");
        ClientProperty.setProperty(statusComboBox, "maximumRowCount", "5");

        customerComboBox.addActionListener(e -> {
            String[] name = customerComboBox.getSelectedItem().toString().split("​");
            String firstName = name[0].equals("All") ? "All" : name[0].trim();
            String lastName = name[0].equals("All") ? "All" : name[1].trim();

            owner.setSortCustomer(firstName, lastName);

            owner.loadCategorizedDeliveries();
        });

        try {
            customerComboBox.addItem("All");
            customerComboBox.setSelectedIndex(0);

            for (Customer customerPojo: CustomerDao.getAllCustomersOrderBy(CustomerColumn.FirstName.getName() + ", " + CustomerColumn.LastName.getName() + " " + Sort.ASC.getName())) {
                customerComboBox.addItem(customerPojo.getFirstName() + "​ " + customerPojo.getLastName());
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Customers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load customers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        statusComboBox.addActionListener(e -> {
            owner.setSortStatus(statusComboBox.getSelectedItem().toString());

            owner.loadCategorizedDeliveries();
        });

        statusComboBox.addItem("All");
        statusComboBox.setSelectedIndex(0);

        for (DeliveryStatus deliveryStatus: DeliveryStatus.values()) {
            if (deliveryStatus == DeliveryStatus.OutForDelivery) {
                statusComboBox.addItem("Out for Delivery");
                continue;
            }

            statusComboBox.addItem(deliveryStatus.name());
        }

        add(customerLabel);
        add(customerComboBox, "span, grow, wrap");

        add(statusLabel);
        add(statusComboBox, "span, grow, wrap");

        add(label);
        add(idSortButton);
        add(dateSortButton);
    }

    private Runnable sortAction(SortButton button) {
        return () -> {
            if (button.getClicks() == 0) {
                owner.removeSort(button.getColumn());
            } else {
                owner.addSort(button.getColumn(), button.getClicks() == 1 ? Sort.ASC : Sort.DESC);
            }
            
            owner.loadCategorizedDeliveries();
        };
    }
}
