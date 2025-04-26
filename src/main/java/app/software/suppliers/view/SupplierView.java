package app.software.suppliers.view;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.StatsCard;
import app.db.dao.production.SupplierDao;
import app.db.pojo.production.Supplier;
import app.software.suppliers.listing.SupplierList;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SupplierView extends Page {
    public SupplierView(SupplierList owner, Supplier supplier) {
        super(new MigLayout("fillx, wrap"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        Page supplierInfo = new Page(new MigLayout("insets 0, align 0%, fillx"), false);
        Page iconContainer = new Page(new MigLayout("insets 0, align 0%"), false);
        Page supplierDetails = new Page(new MigLayout("fillx, wrap"), false);
        
        FlatLabel icon = new FlatLabel();
        icon.setIcon(new Iconify("address-book", Palette.PINK.color()).derive(60, 60));

        FlatLabel name = new FlatLabel();
        name.setForeground(Palette.TEXT.color());
        name.setText(supplier.getSupplierName());
        name.setFont(name.getFont().deriveFont(Font.BOLD, 18));

        FlatLabel id = new FlatLabel();
        id.setText("Supplier No. " + supplier.getSupplierId());
        id.setForeground(Palette.SURFACE2.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        FlatLabel contact = new FlatLabel();
        contact.setText(supplier.getContactNumber());
        contact.setForeground(Palette.SUBTEXT1.color());
        contact.setIcon(new Iconify("phone", Palette.SURFACE2.color()).derive(contact.getFont().getSize() + 4, contact.getFont().getSize() + 4));
        contact.setIconTextGap(5);

        iconContainer.add(icon);
        supplierDetails.add(name, "span, gapbottom 5");
        supplierDetails.add(id, "span, gapbottom 15");
        supplierDetails.add(contact, "span");

        supplierInfo.add(iconContainer, "height 100%");
        supplierInfo.add(supplierDetails, "width 100%, grow");

        StatsCard statsCard = new StatsCard(2);

        try {
            statsCard.addCard("Delivered Supplies", String.format("%,d", SupplierDao.getSupplierSupplyOrderCount(supplier.getSupplierId(), "Delivered")), Palette.PEACH.color());

            statsCard.addCard("Delivered Items", String.format("%,d", SupplierDao.getSupplierSupplyItemCount(supplier.getSupplierId(), "Delivered")), Palette.MAUVE.color());

            statsCard.addCard("Pending Supplies", String.format("%,d", SupplierDao.getSupplierSupplyOrderCount(supplier.getSupplierId(), "Pending")), Palette.GREEN.color());

            statsCard.addCard("Pending Items", String.format("%,d", SupplierDao.getSupplierSupplyItemCount(supplier.getSupplierId(), "Pending")), Palette.BLUE.color());
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(supplierInfo, "growx");
        add(statsCard, "height 85%, grow");
        add(new OrderList(supplier), "height 100%, grow");
    }
}
