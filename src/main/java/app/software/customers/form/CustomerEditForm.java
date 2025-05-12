package app.software.customers.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.customer.Customer;

import app.software.customers.listing.CustomerList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CustomerEditForm extends Form {
    private FlatTextField firstNameField;
    private FlatTextField lastNameField;
    private FlatTextField contactNumberField;
    private FlatTextField streetField;
    private FlatTextField barangayField;
    private FlatTextField cityField;
    private FlatTextField provinceField;

    public CustomerEditForm(CustomerList owner) {
        super();

        setLayout(new MigLayout("align 50% 50%, fillx", "[50%]3%[50%]"));

        setHeader("Edit Customer");
        getHeader().setForeground(Palette.MAUVE.color());

        Customer customer = (Customer) owner.getActiveCustomerEntry().getPojo();

        FlatLabel firstNameLabel = createFieldLabel("First Name");
        firstNameField = createField(customer.getFirstName());

        FlatLabel lastNameLabel = createFieldLabel("Last Name");
        lastNameField = createField(customer.getLastName());

        FlatLabel contactNumberLabel = createFieldLabel("Contact Number");
        contactNumberField = createField(customer.getContactNumber());

        FlatLabel streetLabel = createFieldLabel("Street");
        streetField = createField(customer.getStreet());

        FlatLabel barangayLabel = createFieldLabel("Barangay");
        barangayField = createField(customer.getBarangay());

        FlatLabel cityLabel = createFieldLabel("City");
        cityField = createField(customer.getCity());

        FlatLabel provinceLabel = createFieldLabel("Province");
        provinceField = createField(customer.getProvince());

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

        add(createConfirmButton("Edit", () -> {
            if (!contactNumberField.getText().isBlank() && !contactNumberField.getText().matches("\\d+")) {
                contactNumberField.requestFocusInWindow();
                setFeedback("Invalid contact number!");
            } else if (!contactNumberField.getText().isBlank() && contactNumberField.getText().length() != 11) {
                contactNumberField.requestFocusInWindow();
                setFeedback("Contact number must be 11 digits!");
            } else if (!contactNumberField.getText().isBlank() && !contactNumberField.getText().startsWith("09")) {
                contactNumberField.requestFocusInWindow();
                setFeedback("Contact number must start with 09 (+63)!");
            } else {
                PopupDialog confirm = new PopupDialog("Edit Customer");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("Are you sure you want to edit this customer?");
                confirm.setCloseButtonAction(() -> confirm.dispose());
                confirm.setConfirmButtonAction(() -> 
                {
                    try {
                        String firstName = firstNameField.getText().trim();
                        String lastName = lastNameField.getText().trim();
                        String contact = contactNumberField.getText().trim();
                        String street = streetField.getText().trim();
                        String barangay = barangayField.getText().trim();
                        String city = cityField.getText().trim();
                        String province = provinceField.getText().trim();

                        customer.setFirstName(firstName.isBlank() ? firstNameField.getPlaceholderText() : firstName);
                        customer.setLastName(lastName.isBlank() ? lastNameField.getPlaceholderText() : lastName);
                        customer.setContactNumber(contact.isBlank() ? contactNumberField.getPlaceholderText() : contact);
                        customer.setStreet(street.isBlank() ? streetField.getPlaceholderText() : street);
                        customer.setBarangay(barangay.isBlank() ? barangayField.getPlaceholderText() : barangay);
                        customer.setCity(city.isBlank() ? cityField.getPlaceholderText() : city);
                        customer.setProvince(province.isBlank() ? provinceField.getPlaceholderText() : province);

                        CustomerDao.updateCustomer(customer);

                        confirm.dispose();

                        PopupDialog notif = new PopupDialog("Customer Edited");
                        notif.setDialogType(DialogType.NOTIFICATION);
                        notif.setMessage("Customer has been successfully edited!");
                        notif.setCloseButtonAction(() -> notif.dispose());
                        notif.display();

                        owner.loadCustomers();
                        owner.getOwner().removeActionView();
                    } catch (SQLException e) {
                        PopupDialog error = new PopupDialog("Unable to Edit Customer");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Something unexpected happened. Unable to edit customer.\n\nError: " + e.getMessage());
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                });

                confirm.display();
            }
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
