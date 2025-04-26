package app.software.orders.listing;

import java.awt.Cursor;
import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.PopupDialog;
import app.components.SortButton;
import app.db.dao.production.SupplierDao;
import app.db.pojo.column.OrderStatus;
import app.db.pojo.column.SupplierColumn;
import app.db.pojo.column.SupplyOrderColumn;
import app.db.pojo.production.Supplier;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class OrdersSortPanel extends Page {
    private OrdersList owner;
    public OrdersSortPanel(OrdersList owner) {
        super(new MigLayout(""), false);

        this.owner = owner;

        FlatLabel label = new FlatLabel();
        label.setText("Sort by:");
        label.setForeground(Palette.SUBTEXT0.color());

        SortButton idSortButton = new SortButton(SupplyOrderColumn.OrderID, "ID");
        idSortButton.setAction(sortAction(idSortButton));

        SortButton dateSortButton = new SortButton(SupplyOrderColumn.OrderDate, "Date");
        dateSortButton.setAction(sortAction(dateSortButton));

        FlatLabel supplierLabel = new FlatLabel();
        supplierLabel.setText("Supplier:");
        supplierLabel.setForeground(Palette.SUBTEXT0.color());

        FlatComboBox<String> supplierComboBox = new FlatComboBox<>();
        supplierComboBox.setOpaque(false);
        supplierComboBox.setBackground(Palette.SURFACE0.color());
        supplierComboBox.setForeground(Palette.SUBTEXT1.color());
        supplierComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        FlatLabel statusLabel = new FlatLabel();
        statusLabel.setText("Status:");
        statusLabel.setForeground(Palette.SUBTEXT0.color());

        FlatComboBox<String> statusComboBox = new FlatComboBox<>();
        statusComboBox.setOpaque(false);
        statusComboBox.setBackground(Palette.SURFACE0.color());
        statusComboBox.setForeground(Palette.SUBTEXT1.color());
        statusComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        ClientProperty.setProperty(supplierComboBox, "popupBackground", Palette.SURFACE0.varString());
        ClientProperty.setProperty(supplierComboBox, "buttonBackground", null);
        ClientProperty.setProperty(supplierComboBox, "buttonStyle", "none");
        ClientProperty.setProperty(supplierComboBox, "arrowType", "triangle");
        ClientProperty.setProperty(supplierComboBox, "maximumRowCount", "5");

        ClientProperty.setProperty(statusComboBox, "popupBackground", Palette.SURFACE0.varString());
        ClientProperty.setProperty(statusComboBox, "buttonBackground", null);
        ClientProperty.setProperty(statusComboBox, "buttonStyle", "none");
        ClientProperty.setProperty(statusComboBox, "arrowType", "triangle");
        ClientProperty.setProperty(statusComboBox, "maximumRowCount", "5");

        supplierComboBox.addActionListener(e -> {
            owner.setSortSupplier(supplierComboBox.getSelectedItem().toString());

            owner.loadCategorizedSupplyOrders();
        });

        try {
            supplierComboBox.addItem("All");
            supplierComboBox.setSelectedIndex(0);

            for (Supplier supplierPojo: SupplierDao.getAllSuppliersOrderBy(SupplierColumn.SupplierName.getName() + " " + Sort.ASC.getName())) {
                supplierComboBox.addItem(supplierPojo.getSupplierName());
            }
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Suppliers");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load suppliers\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        statusComboBox.addActionListener(e -> {
            owner.setSortStatus(statusComboBox.getSelectedItem().toString());

            owner.loadCategorizedSupplyOrders();
        });

        statusComboBox.addItem("All");
        statusComboBox.setSelectedIndex(0);

        for (OrderStatus orderStatus: OrderStatus.values()) {
            statusComboBox.addItem(orderStatus.name());
        }

        add(supplierLabel);
        add(supplierComboBox, "span, grow, wrap");
        
        add(statusLabel);
        add(statusComboBox, "span, grow, wrap");

        add(label);
        add(idSortButton);
        add(dateSortButton);
    }

    private Runnable sortAction(SortButton button) {
        return () -> {
            if (button.getClicks() == 0) {
                owner.removeSort(button.getColumn());
            } else {
                owner.addSort(button.getColumn(), button.getClicks() == 1 ? Sort.ASC : Sort.DESC);
            }
            
            owner.loadCategorizedSupplyOrders();
        };
    }
}
