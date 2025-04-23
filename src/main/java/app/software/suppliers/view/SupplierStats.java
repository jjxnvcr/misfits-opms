package app.software.suppliers.view;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.ScrollList;
import app.components.ScrollView;
import app.db.dao.production.SupplierDao;
import app.db.pojo.production.Supplier;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SupplierStats extends Page {
    private ScrollView view = new ScrollView(3);

    public SupplierStats(Object pojo) {
        super(new MigLayout("wrap 2, fillx", "[grow]"), false);

        FlatLabel label = new FlatLabel();
        label.setText("Supplier Statistics");
        label.setForeground(Palette.SUBTEXT0.color());
        
        Supplier supplier = (Supplier) pojo;

        add(label, "wrap");
        add(new ScrollList(view), "grow");

        try {
            view.add(createSupplierStatisticsCard("Delivered Supplies", String.format("%,d", SupplierDao.getSupplierSupplyOrderCount(supplier.getSupplierId(), "Delivered"))), "growx");
            
            view.add(createSupplierStatisticsCard("Delivered Items", String.format("%,d", SupplierDao.getSupplierSupplyItemCount(supplier.getSupplierId(), "Delivered"))), "growx");

            view.add(createSupplierStatisticsCard("Pending Supplies", String.format("%,d", SupplierDao.getSupplierSupplyOrderCount(supplier.getSupplierId(), "Pending"))), "growx");

            view.add(createSupplierStatisticsCard("Pending Items", String.format("%,d", SupplierDao.getSupplierSupplyItemCount(supplier.getSupplierId(), "Pending"))), "growx");

            view.revalidate();
            view.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Page createSupplierStatisticsCard(String text, String value) {
        Page page = new Page(new MigLayout("fillx, wrap"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel textLabel = new FlatLabel();
        textLabel.setText(text);
        textLabel.setForeground(Palette.SURFACE2.color());
        textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getSize() + 2));
        textLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        FlatLabel valueLabel = new FlatLabel();
        valueLabel.setText(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16));
        valueLabel.setForeground(Palette.GREEN.color());
        valueLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        page.add(textLabel, "width 100%");
        page.add(valueLabel, "width 100%");

        return page;
    }
}
