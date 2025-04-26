package app.software.orders.listing;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ActionButton;
import app.components.ListEntry;
import app.components.PopupDialog;
import app.db.pojo.column.OrderStatus;
import app.db.pojo.production.SupplyOrder;
import app.software.orders.form.OrderDeleteConfirmation;
import app.software.orders.form.OrderEditForm;
import app.software.orders.view.OrderView;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class OrderEntry extends ListEntry {
    public OrderEntry(OrdersList owner, SupplyOrder supplyOrder) {
        super(owner, supplyOrder);

        setLayout(new MigLayout("insets 20 0 20 0, align 50%"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.TEXT.color());
        id.setText("Ord." + supplyOrder.getOrderId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));
        id.setIcon(new Iconify("package", Palette.TEAL.color()).derive(id.getFont().getSize() + 4, id.getFont().getSize() + 4));
        id.setIconTextGap(5);

        setAction(() -> {
            if (owner.getActiveOrderEntry() == this) {
                return;
            }
            
            ActionButton activeButton = owner.getActionPanel().getActiveButton();
            owner.setActiveOrderEntry(this);
            
            if (activeButton == owner.getActionPanel().getViewButton()) {
                owner.getOwner().loadActionView(new OrderView(owner, supplyOrder));
            } else if (activeButton == owner.getActionPanel().getAddButton()) {
                owner.getActionPanel().getDefaultButton().doClick();
                doClick();
            } else if (activeButton == owner.getActionPanel().getEditButton()) {
                if (supplyOrder.getOrderStatus().equals(OrderStatus.Cancelled.name())) {
                    PopupDialog error = new PopupDialog("Unable to Edit Cancelled Order");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("You cannot edit a cancelled supply order.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                } else if (supplyOrder.getOrderStatus().equals(OrderStatus.Delivered.name())) {
                    PopupDialog error = new PopupDialog("Unable to Edit Delivered Order");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("You cannot edit a delivered supply order.");
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    return;
                }
                owner.getOwner().loadActionView(new OrderEditForm(owner));
            }
            else if (activeButton == owner.getActionPanel().getDeleteButton()) {
                if (supplyOrder.getOrderStatus().equals(OrderStatus.Delivered.name())) {
                    PopupDialog error = new PopupDialog("Unable to Delete Supply Order");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("This supply order has already been delivered and cannot be deleted.");
                    error.setCloseButtonAction(() -> {error.dispose(); owner.resetActiveOrderEntry();});
                    error.display();
                } else if (new OrderDeleteConfirmation(this).isConfirmed()) {
                    owner.getOwner().removeActionView();
                    owner.resetActiveOrderEntry();
                }
            }
        });

        add(id);
    }
}
