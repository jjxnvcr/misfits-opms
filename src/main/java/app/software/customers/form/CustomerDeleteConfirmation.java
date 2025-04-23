package app.software.customers.form;

import java.sql.SQLException;

import app.components.PopupDialog;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.customer.Customer;
import app.software.customers.listing.CustomerEntry;
import app.software.customers.listing.CustomerList;
import app.utils.DialogType;

public class CustomerDeleteConfirmation extends PopupDialog {
    public CustomerDeleteConfirmation(CustomerEntry customerEntry) {
        super("Delete Customer");
        setDialogType(DialogType.CONFIRMATION);
        setMessage("Would you like to delete this customer?");
        setCloseButtonAction(() -> dispose());
        setConfirmButtonAction(() -> {
            dispose();
            try {
                CustomerDao.deleteCustomer(((Customer) customerEntry.getPojo()).getCustomerId());

                PopupDialog notif = new PopupDialog("Customer Deleted");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Customer has been successfully deleted!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();

                ((CustomerList) customerEntry.getOwner()).loadCustomers();

            } catch (SQLException e) {
                PopupDialog error = new PopupDialog("Unable to Delete Customer");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Something unexpected happened. Unable to delete customer.\n\n Error: " + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
            }
        });

        display();
    }
}
