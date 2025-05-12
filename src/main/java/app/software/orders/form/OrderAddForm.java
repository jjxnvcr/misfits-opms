package app.software.orders.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.production.SupplierDao;
import app.db.dao.production.SupplyOrderDao;
import app.db.pojo.column.SupplierColumn;
import app.db.pojo.production.Supplier;
import app.db.pojo.production.SupplyOrder;
import app.software.orders.listing.OrderEntry;
import app.software.orders.listing.OrdersList;
import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class OrderAddForm extends Form {
    private FlatComboBox<String> supplierField;
    private FlatTextField statusField;

    public OrderAddForm(OrdersList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Add Supply Order");
        getHeader().setForeground(Palette.TEAL.color());

        FlatLabel supplierLabel = createFieldLabel("Supplier");
        supplierField = createComboBox();
        
        try {
            supplierField.addItem("Select Supplier");
            supplierField.setSelectedIndex(0);

            for (Supplier supplierPojo: SupplierDao.getAllSuppliersOrderBy(SupplierColumn.SupplierName.getName() + " " + Sort.ASC.getName())) {
                supplierField.addItem(supplierPojo.getSupplierName());
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Suppliers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load suppliers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        FlatLabel statusLabel = createFieldLabel("Order Status");
        statusField = createField("Pending");
        statusField.setEditable(false);

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(supplierLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(supplierField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(statusLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(statusField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");

        add(createConfirmButton("Add", () -> {
            if (!validateComboBox(supplierField)) return;

            PopupDialog confirm = new PopupDialog("Add Supply Order");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to add this supply order?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    SupplyOrder order = new SupplyOrder(
                        SupplierDao.getSupplierByName(supplierField.getSelectedItem().toString()).getSupplierId(),
                        null,
                        statusField.getPlaceholderText()
                    );

                    SupplyOrderDao.addSupplyOrder(order);

                    confirm.dispose();

                    PopupDialog notif = new PopupDialog("Supply Order Added");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Supply Order has been successfully added!");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();

                    SupplyOrder addedOrder = SupplyOrderDao.getLatestSupplyOrder();
                    OrderEntry orderEntry = new OrderEntry(owner, addedOrder);

                    owner.setActiveOrderEntry(orderEntry);
                    owner.loadSupplyOrders();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Add Supply Order");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to add supply order.\n\n Error: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    e.printStackTrace();
                }
            });
            confirm.display();
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
