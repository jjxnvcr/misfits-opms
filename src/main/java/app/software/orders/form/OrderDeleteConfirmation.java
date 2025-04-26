package app.software.orders.form;

import app.components.PopupDialog;
import app.db.dao.production.SupplyOrderDao;
import app.db.pojo.production.SupplyOrder;
import app.software.orders.listing.OrderEntry;
import app.software.orders.listing.OrdersList;
import app.utils.DialogType;

public class OrderDeleteConfirmation extends PopupDialog {
    public OrderDeleteConfirmation(OrderEntry orderEntry) {
        super("Delete Supply Order");
        setDialogType(DialogType.CONFIRMATION);
        setMessage("Would you like to delete this supply order?");
        setCloseButtonAction(() -> dispose());
        setConfirmButtonAction(() -> {
            dispose();
            try {
                SupplyOrderDao.deleteSupplyOrder(((SupplyOrder) orderEntry.getPojo()).getOrderId());

                PopupDialog notif = new PopupDialog("Supply Order Deleted");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Supply Order has been successfully deleted!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();

                ((OrdersList) orderEntry.getOwner()).loadSupplyOrders();
            } catch (Exception e) {
                PopupDialog error = new PopupDialog("Unable to Delete Supply Order");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Something unexpected happened. Unable to delete supply order.\n\n Error: " + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
            }
        });
        
        display();
    }
}
