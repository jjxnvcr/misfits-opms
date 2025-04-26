package app.software.deliveries.form;

import app.components.PopupDialog;
import app.db.dao.sales.DeliveryDao;
import app.db.pojo.sales.Delivery;
import app.software.deliveries.listing.DeliveryEntry;
import app.software.deliveries.listing.DeliveryList;
import app.utils.DialogType;

public class DeliveryDeleteConfirmation extends PopupDialog {
    public DeliveryDeleteConfirmation(DeliveryEntry deliveryEntry) {
        super("Delete Supply Order");
        setDialogType(DialogType.CONFIRMATION);
        setMessage("Would you like to delete this supply order?");
        setCloseButtonAction(() -> dispose());
        setConfirmButtonAction(() -> {
            dispose();
            try {
                DeliveryDao.deleteDelivery(((Delivery) deliveryEntry.getPojo()).getDeliveryId());

                PopupDialog notif = new PopupDialog("Delivery Deleted");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Delivery has been successfully deleted!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();

                ((DeliveryList) deliveryEntry.getOwner()).loadDeliveries();
            } catch (Exception e) {
                PopupDialog error = new PopupDialog("Unable to Delete Delivery");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Something unexpected happened. Unable to delete delivery.\n\n Error: " + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
            }
        });
        
        display();
    }
}
