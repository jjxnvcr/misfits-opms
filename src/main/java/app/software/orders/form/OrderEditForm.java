package app.software.orders.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.production.SupplierDao;
import app.db.dao.production.SupplyOrderDao;
import app.db.pojo.column.OrderStatus;
import app.db.pojo.column.SupplierColumn;
import app.db.pojo.production.Supplier;
import app.db.pojo.production.SupplyOrder;

import app.software.orders.listing.OrdersList;
import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class OrderEditForm extends Form {
    private FlatComboBox<String> supplierField;
    private FlatComboBox<String> statusField;

    public OrderEditForm(OrdersList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Edit Supply Order");
        getHeader().setForeground(Palette.TEAL.color());

        SupplyOrder supplyOrder = (SupplyOrder) owner.getActiveOrderEntry().getPojo();

        FlatLabel supplierLabel = createFieldLabel("Supplier");
        supplierField = createComboBox();

        FlatLabel statusLabel = createFieldLabel("Order Status");
        statusField = createComboBox();

        try {
            for (Supplier supplierPojo: SupplierDao.getAllSuppliersOrderBy(SupplierColumn.SupplierName.getName() + " " + Sort.ASC.getName())) {
                supplierField.addItem(supplierPojo.getSupplierName());

                if (supplierPojo.getSupplierId() == supplyOrder.getSupplierId()) {
                    supplierField.setSelectedItem(supplierPojo.getSupplierName());
                }
            }

            for (OrderStatus status: OrderStatus.values()) {
                statusField.addItem(status.name());

                if (supplyOrder.getOrderStatus().equals(status.name())) {
                    statusField.setSelectedItem(status.name());
                }
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Suppliers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load suppliers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(supplierLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(supplierField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(statusLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(statusField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");
        add(createConfirmButton("Edit", () -> {
            PopupDialog confirm = new PopupDialog("Edit Supply Order");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to edit this supply order?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    supplyOrder.setSupplierId(SupplierDao.getSupplierByName(supplierField.getSelectedItem().toString()).getSupplierId());
                    
                    supplyOrder.setOrderStatus(statusField.getSelectedIndex() == 0 ? statusField.getPlaceholderText() : statusField.getSelectedItem().toString());

                    SupplyOrderDao.updateSupplyOrder(supplyOrder);

                    confirm.dispose();

                    PopupDialog notif = new PopupDialog("Supply Order Edited");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Supply Order has been successfully edited!");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();

                    owner.loadSupplyOrders();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Edit Supply Order");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to edit supply order.\n\n Error: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    e.printStackTrace();
                }
            });
            confirm.display();
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
