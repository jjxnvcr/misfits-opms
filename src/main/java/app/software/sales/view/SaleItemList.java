package app.software.sales.view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.ScrollList;
import app.components.Table;
import app.db.dao.production.ItemDao;
import app.db.dao.sales.SaleItemDao;
import app.db.pojo.column.Measurement;
import app.db.pojo.sales.SaleItem;
import app.db.pojo.sales.TransactionItem;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SaleItemList extends Page {
    public SaleItemList(List<TransactionItem> items) {
        super(new MigLayout("fillx, wrap"), false);

        FlatLabel label = new FlatLabel();
        label.setText("Transaction Items");
        label.setForeground(Palette.SUBTEXT0.color());

        Vector<Vector<String>> data = new Vector<>();
        Vector<String> columns = new Vector<>();
        columns.add("Item ID");
        columns.add("Name");
        columns.add("Price");
        columns.add("Quantity");
        columns.add("Total");

        for (TransactionItem item : items) {
            Vector<String> row = new Vector<>();
            row.add(String.valueOf(item.getSaleItemId()));

            try {
                DecimalFormat sizeFormat = new DecimalFormat();
                sizeFormat.setMinimumFractionDigits(0);
                sizeFormat.setMaximumFractionDigits(1);

                SaleItem saleItem = SaleItemDao.getSaleItem(item.getSaleItemId());
                row.add(ItemDao.getItemById(saleItem.getItemId()).getItemName() + " (" + 
                (saleItem.getMeasurementSystem() == null ? "One Size" : saleItem.getMeasurementSystem().equals(Measurement.Alpha.name()) ? saleItem.getAlphaSize() : sizeFormat.format(saleItem.getNumericSize())) + ")");
                
                row.add(String.format("₱ %,.2f", saleItem.getItemPrice()));
                row.add(String.valueOf(item.getQuantity()));
                row.add(String.format("₱ %,.2f", saleItem.getItemPrice() * item.getQuantity()));
            } catch (Exception e) {
                row.add("Unknown");
                row.add("0");
                row.add("0");
                row.add("0");
            }

            data.add(row);
        }

        add(label, "wrap");
        add(new ScrollList(new Table(data, columns)), "grow");
    }
}
