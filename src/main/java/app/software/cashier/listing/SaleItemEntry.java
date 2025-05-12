package app.software.cashier.listing;

import java.awt.Font;
import java.text.DecimalFormat;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ListEntry;
import app.db.pojo.column.Measurement;
import app.db.pojo.sales.SaleItem;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SaleItemEntry extends ListEntry {
    public SaleItemEntry(CashierList owner, SaleItem saleItem) {
        super(owner, saleItem);

        setLayout(new MigLayout("wrap"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");
        
        DecimalFormat sizeFormat = new DecimalFormat();
        sizeFormat.setMinimumFractionDigits(0);
        sizeFormat.setMaximumFractionDigits(1);

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.SURFACE1.color());
        id.setText("Itm." + saleItem.getSaleItemId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        FlatLabel name = new FlatLabel();
        name.setForeground(Palette.TEXT.color());
        name.setText(saleItem.getItemName());
        name.setIcon(new Iconify("hanger", Palette.BLUE.color()).derive(name.getFont().getSize() + 4, name.getFont().getSize() + 4));
        name.setIconTextGap(5);

        FlatLabel size = new FlatLabel();
        size.setForeground(Palette.SUBTEXT1.color());
        size.setText("(" + (saleItem.getMeasurementSystem() == null ? "One Size" : saleItem.getMeasurementSystem().equals(Measurement.Alpha.name()) ? saleItem.getAlphaSize() : saleItem.getMeasurementSystem() + " " + sizeFormat.format(saleItem.getNumericSize())) + ")");
        size.setIcon(new Iconify("ruler", Palette.SURFACE0.color()).derive(size.getFont().getSize() + 4, size.getFont().getSize() + 4));
        size.setIconTextGap(5);

        FlatLabel quantity = new FlatLabel();
        quantity.setForeground(saleItem.getItemQuantity() > 0 ? Palette.SUBTEXT0.color() : Palette.RED.color());
        quantity.setText(saleItem.getItemQuantity() > 0 ? String.format("%,d left", saleItem.getItemQuantity()) : "Out of Stock");
        quantity.setIcon(new Iconify("package", Palette.SURFACE0.color()).derive(quantity.getFont().getSize() + 4, quantity.getFont().getSize() + 4));
        quantity.setIconTextGap(5);

        setAction(() -> {
            if (saleItem.getItemQuantity() <= 0) {
                return;
            }
            owner.getOwner().getCartView().addToCart(this);
        });

        add(id);
        add(name, "gapbottom 5");
        add(size);
        add(quantity);
    }

    public void updateQuantity(int quantity) {
        SaleItem saleItem = (SaleItem) getPojo();
        saleItem.setItemQuantity(quantity);
        ((FlatLabel) getComponent(3)).setText(saleItem.getItemQuantity() > 0 ? String.format("%,d left", saleItem.getItemQuantity()) : "Out of Stock");

        if (saleItem.getItemQuantity() <= 0) {
            ((FlatLabel) getComponent(3)).setForeground(Palette.RED.color());
        } else {
            ((FlatLabel) getComponent(3)).setForeground(Palette.SUBTEXT0.color());
        }
    }
}
