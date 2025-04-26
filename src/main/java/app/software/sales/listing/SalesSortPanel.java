package app.software.sales.listing;

import java.awt.Cursor;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.PopupDialog;
import app.components.SortButton;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.column.CustomerColumn;
import app.db.pojo.column.SalesTransactionColumn;
import app.db.pojo.customer.Customer;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class SalesSortPanel extends Page {
    private SalesList owner;

    public SalesSortPanel(SalesList owner) {
        super(new MigLayout(""), false);

        this.owner = owner;

        FlatLabel label = new FlatLabel();
        label.setText("Sort by:");
        label.setForeground(Palette.SUBTEXT0.color());

        SortButton idSortButton = new SortButton(SalesTransactionColumn.TransactionID, "ID");
        idSortButton.setAction(sortAction(idSortButton));

        SortButton dateSortButton = new SortButton(SalesTransactionColumn.TransactionDate, "Date");
        dateSortButton.setAction(sortAction(dateSortButton));

        FlatLabel customer = new FlatLabel();
        customer.setText("Customer:");
        customer.setForeground(Palette.SUBTEXT0.color());

        FlatComboBox<String> customerComboBox = new FlatComboBox<>();
        customerComboBox.setOpaque(false);
        customerComboBox.setBackground(Palette.SURFACE0.color());
        customerComboBox.setForeground(Palette.SUBTEXT1.color());
        customerComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ClientProperty.setProperty(customerComboBox, "popupBackground", Palette.SURFACE0.varString());
        ClientProperty.setProperty(customerComboBox, "buttonBackground", null);
        ClientProperty.setProperty(customerComboBox, "buttonStyle", "none");
        ClientProperty.setProperty(customerComboBox, "arrowType", "triangle");
        ClientProperty.setProperty(customerComboBox, "maximumRowCount", "5");

        customerComboBox.addActionListener(e -> {
            String[] name = customerComboBox.getSelectedItem().toString().split("​");
            String firstName = name[0].equals("All") ? "All" : name[0].trim();
            String lastName = name[0].equals("All") ? "All" : name[1].trim();

            owner.setSortCustomer(firstName, lastName);

            owner.loadCustomerSaleTransactions();
        });

        try {
            customerComboBox.addItem("All");
            customerComboBox.setSelectedIndex(0);

            for (Customer customerPojo: CustomerDao.getAllCustomersOrderBy(CustomerColumn.FirstName.getName() + " " + Sort.ASC.getName() + ", " + CustomerColumn.LastName.getName() + " " + Sort.ASC.getName())) {
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

        add(customer);
        add(customerComboBox, "span, grow, wrap");

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
            
            owner.loadCustomerSaleTransactions();;
        };
    }
}
