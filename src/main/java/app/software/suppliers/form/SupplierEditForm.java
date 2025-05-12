package app.software.suppliers.form;

import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.production.SupplierDao;
import app.db.pojo.production.Supplier;
import app.software.suppliers.listing.SupplierList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SupplierEditForm extends Form {
    private FlatTextField nameField;
    private FlatTextField contactField;

    public SupplierEditForm(SupplierList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Edit Supplier");
        getHeader().setForeground(Palette.PINK.color());

        Supplier supplier = (Supplier) owner.getActiveSupplierEntry().getPojo();

        FlatLabel nameLabel = createFieldLabel("Supplier Name");
        nameField = createField(supplier.getSupplierName());

        FlatLabel contactLabel = createFieldLabel("Contact Number");
        contactField = createField(supplier.getContactNumber());

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(nameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(nameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(contactLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(contactField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");

        add(createConfirmButton("Edit", () -> {
            if (!contactField.getText().isBlank() && !contactField.getText().matches("\\d+")) {
                contactField.requestFocusInWindow();
                setFeedback("Invalid contact number!");
            } else if (!contactField.getText().isBlank() && contactField.getText().length() != 11) {
                contactField.requestFocusInWindow();
                setFeedback("Contact number must be 11 digits!");
            } else if (!contactField.getText().isBlank() && !contactField.getText().startsWith("09")) {
                contactField.requestFocusInWindow();
                setFeedback("Contact number must start with 09 (+63)!");
            } else {
                PopupDialog confirm = new PopupDialog("Edit Supplier");
                confirm.setDialogType(DialogType.CONFIRMATION);
                confirm.setMessage("Are you sure you want to edit this supplier?");
                confirm.setCloseButtonAction(() -> confirm.dispose());
                confirm.setConfirmButtonAction(() -> {
                    try {
                        String name = nameField.getText().trim();
                        String contact = contactField.getText().trim();

                        supplier.setSupplierName(name.isBlank() ? nameField.getPlaceholderText() : name);
                        supplier.setContactNumber(contact.isBlank() ? contactField.getPlaceholderText() : contact);

                        SupplierDao.updateSupplier(supplier);

                        confirm.dispose();

                        PopupDialog notif = new PopupDialog("Supplier Edited");
                        notif.setDialogType(DialogType.NOTIFICATION);
                        notif.setMessage("Supplier has been successfully edited!");
                        notif.setCloseButtonAction(() -> notif.dispose());
                        notif.display();

                        owner.loadSupplier();
                        owner.getOwner().removeActionView();
                    } catch (Exception e) {
                        PopupDialog error = new PopupDialog("Unable to Edit Supplier");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Something unexpected happened. Unable to edit supplier.\n\nError: " + e.getMessage());
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                });

                confirm.display();
            }
    }), "grow, gapbottom 5%, gapleft 5%");
    }
}
