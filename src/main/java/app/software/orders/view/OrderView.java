package app.software.orders.view;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.StatsCard;
import app.db.dao.production.SupplierDao;
import app.db.dao.production.SupplyOrderDao;
import app.db.pojo.production.SupplyOrder;
import app.db.pojo.production.SupplyOrderItem;
import app.software.orders.listing.OrdersList;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class OrderView extends Page {
    public OrderView(OrdersList owner, SupplyOrder supplyOrder) {
        super(new MigLayout("fillx, wrap"));
        
        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        Page orderInfo = new Page(new MigLayout("insets 0, align 0%, fillx"), false);
        Page iconContainer = new Page(new MigLayout("insets 0, align 0%"), false);
        Page orderDetails = new Page(new MigLayout("fillx, wrap"), false);

        FlatLabel icon = new FlatLabel();
        icon.setIcon(new Iconify("package", Palette.TEAL.color()).derive(60, 60));

        FlatLabel id = new FlatLabel();
        id.setText("Supply Order No. " + supplyOrder.getOrderId());
        id.setForeground(Palette.TEXT.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD, 18));

        FlatLabel supplier = new FlatLabel();
        supplier.setForeground(Palette.SUBTEXT1.color());
        supplier.setIcon(new Iconify("address-book", Palette.SURFACE2.color()).derive(supplier.getFont().getSize() + 4, supplier.getFont().getSize() + 4));
        supplier.setIconTextGap(5);

        try {
            supplier.setText(SupplierDao.getSupplierById(supplyOrder.getSupplierId()).getSupplierName());
        } catch (Exception e) {
            supplier.setText("Unknown");
        }

        FlatLabel date = new FlatLabel();
        date.setForeground(Palette.SUBTEXT1.color());
        date.setText(new SimpleDateFormat("MMM dd, yyyy").format(new Date(supplyOrder.getOrderDate().getTime())));
        date.setIcon(new Iconify("calendar", Palette.SURFACE2.color()).derive(date.getFont().getSize() + 4, date.getFont().getSize() + 4));
        date.setIconTextGap(5);

        FlatLabel status = new FlatLabel();
        status.setForeground(Palette.SUBTEXT1.color());
        status.setText(supplyOrder.getOrderStatus());
        status.setIcon(new Iconify("truck", Palette.SURFACE2.color()).derive(status.getFont().getSize() + 4, status.getFont().getSize() + 4));
        status.setIconTextGap(5);

        orderDetails.add(id, "span, gapbottom 5");
        orderDetails.add(supplier, "span, gapbottom 15");
        orderDetails.add(date, "span");
        orderDetails.add(status, "span");

        iconContainer.add(icon);

        orderInfo.add(iconContainer, "height 100%");
        orderInfo.add(orderDetails, "width 100%, grow");

        StatsCard stats = new StatsCard(2);
        List<SupplyOrderItem> items = new ArrayList<>();

        try {
            items = SupplyOrderDao.getSupplyOrderItemsById(supplyOrder.getOrderId());
        
            stats.addCard("Order Value", "₱ " + String.format("%.2f", SupplyOrderDao.getOrderTotal(supplyOrder.getOrderId())), Palette.PEACH.color());

            stats.addCard("Item Count", String.format("%,d", items.size()), Palette.MAUVE.color());

            stats.addCard("Total Items", String.format("%,d", SupplyOrderDao.getOrderTotalItems(supplyOrder.getOrderId())), Palette.GREEN.color());

        } catch (Exception e) {
            e.printStackTrace();
        }

        add(orderInfo, "growx");
        add(stats, "height 51%, grow");
        add(new OrderItemList(items), "grow");
    }
}
