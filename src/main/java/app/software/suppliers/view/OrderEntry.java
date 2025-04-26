package app.software.suppliers.view;

import java.awt.Font;
import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ListEntry;
import app.db.dao.production.SupplyOrderDao;
import app.db.pojo.production.SupplyOrder;

import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class OrderEntry extends ListEntry {
    public OrderEntry(SupplyOrder supplyOrder) {
        super(null, supplyOrder);
        setLayout(new MigLayout("fillx"));

        FlatLabel id = new FlatLabel();
        id.setText("Ord." + supplyOrder.getOrderId());
        id.setForeground(Palette.SURFACE1.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        double total;
        try {
            total = SupplyOrderDao.getOrderTotal(supplyOrder.getOrderId());
        } catch (SQLException e) {
            total = 0;
        }

        FlatLabel amount = new FlatLabel();

        amount.setText(String.format("₱ %,.2f", total));
        amount.setForeground(Palette.GREEN.color());
        amount.setFont(amount.getFont().deriveFont(Font.BOLD));

        FlatLabel status = new FlatLabel();
        status.setText(supplyOrder.getOrderStatus());
        status.setForeground(Palette.SUBTEXT0.color());

        setAction(() -> {return;});

        add(id);
        add(amount, "align 100%, pushx, wrap");
        add(status);
    }
}
