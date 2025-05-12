package app.software.sales.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.sales.SalesTransaction;
import app.software.sales.listing.SalesList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SaleEditForm extends Form {
    public SaleEditForm(SalesList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Edit Sales Transaction");
        getHeader().setForeground(Palette.TEAL.color());

        SalesTransaction salesTransaction = (SalesTransaction) owner.getActiveSaleEntry().getPojo();

        FlatLabel referenceNumberLabel = createFieldLabel("Reference Number");
        FlatTextField referenceField = createField(salesTransaction.getReferenceNumber() == null ? "" : salesTransaction.getReferenceNumber());

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(referenceNumberLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(referenceField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");
        add(createConfirmButton("Edit", () -> {
            PopupDialog confirm = new PopupDialog("Edit Sales Transaction");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to edit this sales transaction?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    salesTransaction.setReferenceNumber(referenceField.getText() == "" ? salesTransaction.getReferenceNumber() : referenceField.getText());

                    SalesTransactionDao.updateSalesTransaction(salesTransaction);

                    confirm.dispose();

                    PopupDialog notif = new PopupDialog("Sales Transaction Edited");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Sales transaction has been successfully edited!");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();

                    owner.loadSaleTransactions();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Edit Sales Transaction");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to edit sales transaction.\n\n Error: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    e.printStackTrace();
                }
            });
            confirm.display();
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
