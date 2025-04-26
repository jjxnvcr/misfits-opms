package app.software.cashier.view;

import com.formdev.flatlaf.extras.components.FlatCheckBox;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.customer.CustomerDao;
import app.db.pojo.column.CustomerColumn;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.customer.Customer;
import app.db.pojo.sales.Delivery;
import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class Checkout extends Form {
    private FlatComboBox<String> customerField;
    private FlatCheckBox defaultAddressField;
    private FlatTextField streetField;
    private FlatTextField barangayField;
    private FlatTextField cityField;
    private FlatTextField provinceField;
    private Customer customer;
    private CartView owner;

    public Checkout(CartView owner) {
        super();

        this.owner = owner;

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        setHeader("Checkout");
        getHeader().setForeground(Palette.LAVENDER.color());

        FlatLabel customerLabel = createFieldLabel("Customer");
        customerField = createComboBox();

        try {
            customerField.addItem("Select Customer");
            customerField.setSelectedIndex(0);

            for (Customer customerPojo: CustomerDao.getAllCustomersOrderBy(CustomerColumn.FirstName.getName() + " " + Sort.ASC.getName() + ", " + CustomerColumn.LastName.getName() + " " + Sort.ASC.getName())) {
                customerField.addItem(customerPojo.getFirstName() + "​ " + customerPojo.getLastName());
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Customers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load customers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        defaultAddressField = createCheckBox("Use Default Address");
        defaultAddressField.addChangeListener(e -> {
            if (defaultAddressField.isSelected()) {
                if (customerField.getSelectedIndex() == 0) {
                    PopupDialog error = new PopupDialog("No Selected Customer");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Please select a customer first.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                }

                try {
                    String[] name = customerField.getSelectedItem().toString().split("​");

                    customer = CustomerDao.getCustomerByName(name[0].trim(), name[1].trim());

                    streetField.setPlaceholderText(customer.getStreet());
                    streetField.setEditable(false);

                    barangayField.setPlaceholderText(customer.getBarangay());
                    barangayField.setEditable(false);

                    cityField.setPlaceholderText(customer.getCity());
                    cityField.setEditable(false);

                    provinceField.setPlaceholderText(customer.getProvince());
                    provinceField.setEditable(false);

                } catch (Exception e1) {
                    e1.printStackTrace();
                    PopupDialog error = new PopupDialog("Unable to load Customer");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Unable to load customer\n" + e1.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                }
            } else {
                streetField.setPlaceholderText("");
                streetField.setEditable(true);
                
                barangayField.setPlaceholderText("");
                barangayField.setEditable(true);

                cityField.setPlaceholderText("");
                cityField.setEditable(true);

                provinceField.setPlaceholderText("");
                provinceField.setEditable(true);
            }
        });

        FlatLabel streetLabel = createFieldLabel("Street");
        streetField = createField("");

        FlatLabel barangayLabel = createFieldLabel("Barangay");
        barangayField = createField("");

        FlatLabel cityLabel = createFieldLabel("City");
        cityField = createField("");

        FlatLabel provinceLabel = createFieldLabel("Province");
        provinceField = createField("");

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(customerLabel, "grow, gapleft 5%, wrap");
        add(customerField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(defaultAddressField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
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
            owner.getOwner().getOwner().loadActionView(owner);
        }), "grow, gapbottom 5%, gapleft 5%");

        add(createConfirmButton("Continue", () -> {
            if (!validateComboBox(customerField)) { return; }

            String street = "", barangay = "", city = "", province = "";

            if (!defaultAddressField.isSelected()) {
                if (!validateFields(streetField)) {
                    streetField.requestFocusInWindow();
                    setFeedback("Street is required!");
                } else if (!validateFields(barangayField)) {
                    barangayField.requestFocusInWindow();
                    setFeedback("Barangay is required!");
                } else if (!validateFields(cityField)) {
                    cityField.requestFocusInWindow();
                    setFeedback("City is required!");
                } else if (!validateFields(provinceField)) {
                    provinceField.requestFocusInWindow();
                    setFeedback("Province is required!");
                } else {
                    street = streetField.getText();
                    barangay = barangayField.getText();
                    city = cityField.getText();
                    province = provinceField.getText();
                }
                return;
            } else {
                street = streetField.getPlaceholderText();
                barangay = barangayField.getPlaceholderText();
                city = cityField.getPlaceholderText();
                province = provinceField.getPlaceholderText();
            }

            try {
                Delivery delivery = new Delivery(0, (street + ", " + barangay + ", " + city + ", " + province), null, DeliveryStatus.Pending.name(), null);

                owner.getOwner().getOwner().loadActionView(new CheckoutPayment(this, delivery, customer));
            } catch (Exception e) {
                e.printStackTrace();
                PopupDialog error = new PopupDialog("Unable to Proceed Transaction");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Unable to proceed transaction\n" + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
                return;
            }
        }), "grow, gapbottom 5%, gapright 5%");
    }

    public CartView getOwner() {
        return owner;
    }
}
