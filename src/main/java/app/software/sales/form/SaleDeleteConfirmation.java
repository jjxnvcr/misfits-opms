package app.software.sales.form;

import app.components.PopupDialog;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.sales.SalesTransaction;
import app.software.sales.listing.SaleEntry;
import app.software.sales.listing.SalesList;
import app.utils.DialogType;

public class SaleDeleteConfirmation extends PopupDialog {
    public SaleDeleteConfirmation(SaleEntry saleEntry) {
        super("Delete Sale Transaction");
        setDialogType(DialogType.CONFIRMATION);
        setMessage("Would you like to delete this sale transaction?");
        setCloseButtonAction(() -> dispose());
        setConfirmButtonAction(() -> {
            dispose();
            try {
                SalesTransactionDao.deleteSalesTransaction(((SalesTransaction) saleEntry.getPojo()).getTransactionId());

                PopupDialog notif = new PopupDialog("Sale Transaction Deleted");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Sale transaction has been successfully deleted!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();

                ((SalesList) saleEntry.getOwner()).loadSaleTransactions();
            } catch (Exception e) {
                e.printStackTrace();
                PopupDialog error = new PopupDialog("Unable to Delete Sale Transaction");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Something unexpected happened. Unable to delete sale transaction.\n\n Error: " + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
            }
        });
        
        display();
    }
}
