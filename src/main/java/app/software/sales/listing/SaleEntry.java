package app.software.sales.listing;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ActionButton;
import app.components.ListEntry;
import app.components.PopupDialog;
import app.db.dao.sales.DeliveryDao;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.sales.SalesTransaction;
import app.software.sales.form.SaleDeleteConfirmation;
import app.software.sales.form.SaleEditForm;
import app.software.sales.view.SaleView;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SaleEntry extends ListEntry {
    public SaleEntry(SalesList owner, SalesTransaction sale) {
        super(owner, sale);

        setLayout(new MigLayout("insets 20 0 20 0, align 50%"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.TEXT.color());
        id.setText("Tsc." + sale.getTransactionId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));
        id.setIcon(new Iconify("cash-register", Palette.MAROON.color()).derive(id.getFont().getSize() + 4, id.getFont().getSize() + 4));
        id.setIconTextGap(5);

        setAction(() -> {
            if (owner.getActiveSaleEntry() == this) {
                return;
            }
            
            ActionButton activeButton = owner.getActionPanel().getActiveButton();
            owner.setActiveSaleEntry(this);
            
            if (activeButton == owner.getActionPanel().getViewButton()) {
                owner.getOwner().loadActionView(new SaleView(owner, sale));
            } else if (activeButton == owner.getActionPanel().getEditButton()) {
                try {
                    if (DeliveryDao.getDeliveryByTransactionId(((SalesTransaction) owner.getActiveSaleEntry().getPojo()).getTransactionId()).getDeliveryStatus().equals(DeliveryStatus.Pending.name())) {
                        owner.getOwner().loadActionView(new SaleEditForm(owner));
                    } else {
                        PopupDialog error = new PopupDialog("Unable to Edit Sale Transaction");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Sale transaction is already being processed. Unable to edit.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                } catch (Exception e) {
                    PopupDialog error = new PopupDialog("Unable to Edit Sale Transaction");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("An error occured. Please try again.\n\n" + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display(); 
                }
            }
            else if (activeButton == owner.getActionPanel().getDeleteButton()) {
                try {
                    if (DeliveryDao.getDeliveryByTransactionId(((SalesTransaction) owner.getActiveSaleEntry().getPojo()).getTransactionId()).getDeliveryStatus().equals(DeliveryStatus.Pending.name())) {
                        if (new SaleDeleteConfirmation(this).isConfirmed()) {
                            owner.getOwner().removeActionView();
                            owner.resetActiveSaleEntry();
                        }
                    } else {
                        PopupDialog error = new PopupDialog("Unable to Delete Sale Transaction");
                        error.setDialogType(DialogType.ERROR);
                        error.setMessage("Sale transaction is already being processed. Unable to delete.");
                        error.setCloseButtonAction(() -> error.dispose());
                        error.display();
                    }
                } catch (Exception e) {
                    PopupDialog error = new PopupDialog("Unable to Delete Sale Transaction");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("An error occured. Please try again.\n\n" + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                }
            }
        });

        add(id);
    }
}