package app.software.deliveries.listing;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ActionButton;
import app.components.ListEntry;
import app.components.PopupDialog;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.sales.Delivery;
import app.software.deliveries.form.DeliveryDeleteConfirmation;
import app.software.deliveries.form.DeliveryEditForm;
import app.software.deliveries.view.DeliveryView;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class DeliveryEntry extends ListEntry {
    public DeliveryEntry(DeliveryList owner, Delivery delivery) {
        super(owner, delivery);

        setLayout(new MigLayout("insets 20 0 20 0, align 50%"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.TEXT.color());
        id.setText("Dlv." + delivery.getDeliveryId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));
        id.setIcon(new Iconify("truck", Palette.TEAL.color()).derive(id.getFont().getSize() + 4, id.getFont().getSize() + 4));
        id.setIconTextGap(5);

        setAction(() -> {
            if (owner.getActiveDeliveryEntry() == this) {
                return;
            }
            
            ActionButton activeButton = owner.getActionPanel().getActiveButton();
            owner.setactiveDeliveryEntry(this);
            
            if (activeButton == owner.getActionPanel().getViewButton()) {
                owner.getOwner().loadActionView(new DeliveryView(owner, delivery));
            } else if (activeButton == owner.getActionPanel().getEditButton()) {
                Delivery activeDelivery = (Delivery) getPojo();

                if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Cancelled.name())) {
                    PopupDialog error = new PopupDialog("Unable to Edit Cancelled Delivery");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("You cannot edit a cancelled delivery.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                } else if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Delivered.name())) {
                    PopupDialog error = new PopupDialog("Unable to Edit Delivered Delivery");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("You cannot edit a delivered delivery.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                }
                
                owner.getOwner().loadActionView(new DeliveryEditForm(owner));
            }
            else if (activeButton == owner.getActionPanel().getDeleteButton()) {
                
                Delivery activeDelivery = (Delivery) getPojo();
                    
                if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Shipped.name())) {
                    PopupDialog error = new PopupDialog("Unable to Delete Shipped Delivery");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("You cannot delete a shipped delivery.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                } else if (activeDelivery.getDeliveryStatus().equals(DeliveryStatus.Delivered.name())) {
                    PopupDialog error = new PopupDialog("Unable to Delete Delivered Delivery");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("You cannot delete a delivered delivery.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                } else if (activeDelivery.getDeliveryStatus().equals("Out for Delivery")) {
                    PopupDialog error = new PopupDialog("Unable to Delete Delivery in Progress");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("You cannot delete a delivery that is underway.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                }
                
                if (new DeliveryDeleteConfirmation(this).isConfirmed()) {
                    owner.getOwner().removeActionView();
                    owner.resetActiveDeliveryEntry();
                }
            }
        });

        add(id);
    }
}
