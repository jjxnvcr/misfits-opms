package app.software.suppliers.view;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
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
        lightenBackground(5);

        Page supplierInfo = new Page(new MigLayout("fillx"), false);

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

        supplierInfo.add(icon, "wrap");
        supplierInfo.add(name, "growx, gapbottom 5, wrap");
        supplierInfo.add(id, "wrap, gapbottom 15");
        supplierInfo.add(contact, "wrap");

        add(supplierInfo, "growx");
        
        add(new SupplierStats(supplier), "height 85%, grow");
        add(new OrderList(supplier), "height 100%, grow");
    }
}
