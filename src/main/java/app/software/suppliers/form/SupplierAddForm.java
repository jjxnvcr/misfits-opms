package app.software.suppliers.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.production.SupplierDao;
import app.db.pojo.production.Supplier;
import app.software.suppliers.listing.SupplierEntry;
import app.software.suppliers.listing.SupplierList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SupplierAddForm extends Form {
    private FlatTextField nameField;
    private FlatTextField contactField;

    public SupplierAddForm(SupplierList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Add Supplier");
        getHeader().setForeground(Palette.GREEN.color());

        FlatLabel nameLabel = createFieldLabel("Supplier Name");
        nameField = createField("");

        FlatLabel contactLabel = createFieldLabel("Contact Number");
        contactField = createField("");

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(nameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(nameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(contactLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(contactField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");
        
        add(createConfirmButton("Add", () -> {
            if (!validateFields(nameField, contactField)) { return; }

            if (!contactField.getText().matches("\\d+")) {
                contactField.requestFocusInWindow();
                setFeedback("Invalid contact number!");
            } else if (contactField.getText().length() != 11) {
                contactField.requestFocusInWindow();
                setFeedback("Contact number must be 11 digits!");
            } else if (!contactField.getText().startsWith("09")) {
                contactField.requestFocusInWindow();
                setFeedback("Contact number must start with 09 (+63)!");
            } else {
                PopupDialog confirm = new PopupDialog("Add Supplier");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("Are you sure you want to add " + nameField.getText() + " as a supplier?");
                confirm.setCloseButtonAction(() -> confirm.dispose());
                confirm.setConfirmButtonAction(() -> {
                    try {
                        Supplier supplier = new Supplier(nameField.getText().trim(), contactField.getText().trim());

                        SupplierDao.addSupplier(supplier);

                        confirm.dispose();

                        PopupDialog notif = new PopupDialog("Supplier Added");
                        notif.setDialogType(DialogType.NOTIFICATION);
                        notif.setMessage("Supplier has been successfully added!");
                        notif.setCloseButtonAction(() -> notif.dispose());
                        notif.display();

                        Supplier addedSupplier = SupplierDao.getLatestSupplier();
                        SupplierEntry supplierEntry = new SupplierEntry(owner, addedSupplier);

                        owner.setActiveSupplierEntry(supplierEntry);
                        owner.loadSupplier();
                        owner.getOwner().removeActionView();
                    } catch (SQLException e) {
                        PopupDialog error = new PopupDialog("Unable to Add Supplier");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Something unexpected happened. Unable to add supplier.\n\nError: " + e.getMessage());
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                });

                confirm.display();  
            }
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
