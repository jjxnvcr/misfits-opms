package app.software.suppliers.form;

import java.sql.SQLException;

import app.components.PopupDialog;
import app.db.dao.production.SupplierDao;
import app.db.pojo.production.Supplier;
import app.software.suppliers.listing.SupplierEntry;
import app.software.suppliers.listing.SupplierList;
import app.utils.DialogType;

public class SupplierDeleteConfirmation extends PopupDialog {
    public SupplierDeleteConfirmation(SupplierEntry supplierEntry) {
        super("Delete Supplier");
        setDialogType(DialogType.CONFIRMATION);
        setMessage("Would you like to delete this supplier?");
        setCloseButtonAction(() -> dispose());
        setConfirmButtonAction(() -> {
            dispose();
            try {
                SupplierDao.deleteSupplier(((Supplier) supplierEntry.getPojo()).getSupplierId());

                PopupDialog notif = new PopupDialog("Supplier Deleted");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Supplier has been successfully deleted!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();

                ((SupplierList) supplierEntry.getOwner()).loadSupplier();

            } catch (SQLException e) {
                PopupDialog error = new PopupDialog("Unable to Delete Supplier");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Something unexpected happened. Unable to delete supplier.\n\n Error: " + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
            }
        });

        display();
    }
}
