package app.software.orders.view;

import java.util.List;
import java.util.Vector;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.ScrollList;
import app.components.Table;
import app.db.dao.production.ItemDao;
import app.db.pojo.production.SupplyOrderItem;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class OrderItemList extends Page {
    public OrderItemList(List<SupplyOrderItem> items) {
        super(new MigLayout("fillx, wrap"), false);

        FlatLabel label = new FlatLabel();
        label.setText("Order Items");
        label.setForeground(Palette.SUBTEXT0.color());

        Vector<Vector<String>> data = new Vector<>();
        Vector<String> columns = new Vector<>();
        columns.add("Item ID");
        columns.add("Name");
        columns.add("Cost");
        columns.add("Quantity");
        columns.add("Total");

        for (SupplyOrderItem item : items) {
            Vector<String> row = new Vector<>();
            row.add(String.valueOf(item.getItemId()));

            try {
                row.add(ItemDao.getItemById(item.getItemId()).getItemName());
            } catch (Exception e) {
                row.add("Unknown");
            }

            row.add(String.format("₱ %,.2f", item.getOrderItemCost()));
            row.add(String.valueOf(item.getQuantity()));
            row.add(String.format("₱ %,.2f", item.getOrderItemCost() * item.getQuantity()));

            data.add(row);
        }

        add(label, "wrap");
        add(new ScrollList(new Table(data, columns)), "grow");
    }
}
