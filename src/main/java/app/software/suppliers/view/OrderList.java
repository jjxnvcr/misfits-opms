package app.software.suppliers.view;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.components.ScrollView;
import app.components.SortButton;
import app.db.dao.production.SupplyOrderDao;

import app.db.pojo.column.SupplyOrderColumn;
import app.db.pojo.production.Supplier;
import app.db.pojo.production.SupplyOrder;

import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class OrderList extends Page {
    private ScrollView view = new ScrollView(1);
    private FlatLabel orderCountLabel;
    private Supplier supplier;
    private boolean sort = false;

    public OrderList(Supplier supplier) {
        super(new MigLayout("fillx", "[][]"), false);

        this.supplier = supplier;

        SortButton dateSort = new SortButton(SupplyOrderColumn.OrderDate, "Date");
        dateSort.setIcon(new Iconify("desc", dateSort.getForeground()).derive(dateSort.getFont().getSize(), dateSort.getFont().getSize()));
        dateSort.setAction(() -> {
            dateSort.setIcon(new Iconify(!getSort() ? "asc" : "desc", dateSort.getForeground()).derive(dateSort.getFont().getSize(), dateSort.getFont().getSize()));
            setSort(!getSort());
            loadOrders();
        });

        FlatLabel ordersLabel = new FlatLabel();
        ordersLabel.setText("Orders");
        ordersLabel.setForeground(Palette.SUBTEXT0.color());

        orderCountLabel = new FlatLabel();
        orderCountLabel.setForeground(Palette.TEXT.color());
        
        add(ordersLabel);
        add(dateSort, "align 100%, pushx, wrap");
        
        add(orderCountLabel, "align 50%, span, wrap");
        add(new ScrollList(view), "height 100%, span, grow, wrap");

        loadOrders();
    }

    private void loadOrders() {
        view.removeAll();

        List<SupplyOrder> orders = new ArrayList<>();
        
        try {
            orders = SupplyOrderDao.getSupplyOrdersBySupplierIdOrderBy(supplier.getSupplierId(), SupplyOrderColumn.OrderDate + " " + (sort ? Sort.ASC.getName() : Sort.DESC.getName()));
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to Load Orders");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load orders for this supplier\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
        }

        orderCountLabel.setText(orders.size() == 0 ? "No orders found." : orders.size() + " order" + (orders.size() == 1 ? "" : "s"));
        orderCountLabel.revalidate();

        if (orders.size() > 0) {
            Timestamp currDate = orders.get(0).getOrderDate();

            for (SupplyOrder order: orders) {
                FlatLabel dateLabel = new FlatLabel();
                dateLabel.setForeground(Palette.SUBTEXT0.color());
                dateLabel.setText(new SimpleDateFormat("MMM dd, yyyy").format(new Date(currDate.getTime())));

                if (currDate != order.getOrderDate()) {
                    dateLabel.setText(new SimpleDateFormat("MMM dd, yyyy").format(new Date(order.getOrderDate().getTime())));
                }

                currDate = order.getOrderDate();

                view.add(dateLabel, "align 0%, gapbottom 5");
                view.add(new OrderEntry(order), "growx");
            }

            view.revalidate();
            view.repaint();
        }
    }

    
    public boolean getSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }
}
