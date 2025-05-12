package app.software.customers.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.customer.Customer;
import app.software.customers.listing.CustomerEntry;
import app.software.customers.listing.CustomerList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CustomerAddForm extends Form {
    private FlatTextField firstNameField;
    private FlatTextField lastNameField;
    private FlatTextField contactNumberField;
    private FlatTextField streetField;
    private FlatTextField barangayField;
    private FlatTextField cityField;
    private FlatTextField provinceField;

    public CustomerAddForm(CustomerList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Add Customer");
        getHeader().setForeground(Palette.MAUVE.color());

        FlatLabel firstNameLabel = createFieldLabel("First Name");
        firstNameField = createField("");

        FlatLabel lastNameLabel = createFieldLabel("Last Name");
        lastNameField = createField("");

        FlatLabel contactNumberLabel = createFieldLabel("Contact Number");
        contactNumberField = createField("");

        FlatLabel streetLabel = createFieldLabel("Street");
        streetField = createField("");

        FlatLabel barangayLabel = createFieldLabel("Barangay");
        barangayField = createField("");

        FlatLabel cityLabel = createFieldLabel("City");
        cityField = createField("");

        FlatLabel provinceLabel = createFieldLabel("Province");
        provinceField = createField("");

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(firstNameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(firstNameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(lastNameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(lastNameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(contactNumberLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(contactNumberField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(streetLabel, "grow, gapleft 5%");
        add(barangayLabel, "wrap, grow, gapright 5%");
        add(streetField, "grow, gapleft 5%, gapbottom .5%");
        add(barangayField, "wrap, grow, gapright 5%, gapbottom .5%");
        add(cityLabel, "grow, gapleft 5%");
        add(provinceLabel, "wrap, grow, gapright 5%");
        add(cityField, "grow, gapleft 5%, gapbottom 2.5%");
        add(provinceField, "wrap, grow, gapright 5%, gapbottom 2.5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");

        add(createConfirmButton("Add", () -> {
            if (!validateFields(firstNameField, lastNameField, contactNumberField, streetField, barangayField, cityField, provinceField)) { return; }

            if (!contactNumberField.getText().matches("\\d+")) {
                contactNumberField.requestFocusInWindow();
                setFeedback("Invalid contact number!");
            } else if (contactNumberField.getText().length() != 11) {
                contactNumberField.requestFocusInWindow();
                setFeedback("Contact number must be 11 digits!");
            } else if (!contactNumberField.getText().startsWith("09")) {
                contactNumberField.requestFocusInWindow();
                setFeedback("Contact number must start with 09 (+63)!");
            } else {
                PopupDialog confirm = new PopupDialog("Add Customer");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("Are you sure you want to add this customer?");
                confirm.setCloseButtonAction(() -> confirm.dispose());
                confirm.setConfirmButtonAction(() -> 
                {
                    try {
                        Customer customer = new Customer(
                            firstNameField.getText().trim(),
                            lastNameField.getText().trim(),
                            contactNumberField.getText().trim(),
                            streetField.getText().trim(),
                            barangayField.getText().trim(),
                            cityField.getText().trim(),
                            provinceField.getText().trim()
                        );

                        CustomerDao.addCustomer(customer);

                        confirm.dispose();

                        PopupDialog notif = new PopupDialog("Customer Added");
                        notif.setDialogType(DialogType.NOTIFICATION);
                        notif.setMessage("Customer has been successfully added!");
                        notif.setCloseButtonAction(() -> notif.dispose());
                        notif.display();

                        Customer addedCustomer = CustomerDao.getLatestCustomer();
                        CustomerEntry customerEntry = new CustomerEntry(owner, addedCustomer);
                        
                        owner.setActiveCustomerEntry(customerEntry);
                        owner.loadCustomers();
                        owner.getOwner().removeActionView();
                    } catch (SQLException e) {
                        PopupDialog error = new PopupDialog("Unable to Add Customer");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Something unexpected happened. Unable to add customer.\n\nError: " + e.getMessage());
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                });

                confirm.display();
            }
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
