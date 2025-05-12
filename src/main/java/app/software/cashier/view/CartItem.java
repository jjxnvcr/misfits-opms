package app.software.cashier.view;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.LabelWrap;
import app.components.Page;
import app.db.pojo.column.Measurement;
import app.db.pojo.sales.SaleItem;
import app.software.cashier.listing.SaleItemEntry;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CartItem extends Page {
    public CartItem(CartView owner, SaleItemEntry saleItemEntry, int quantity) {
        super(new MigLayout("fillx", "[90%]5%[5%]"), false);

        setBackground(Palette.TRANSPARENT);

        Page itemInfo = new Page(new MigLayout("insets 0, wrap, fillx"), false);
        Page quantityInfo = new Page(new MigLayout("insets 0, align 50% 50%"), false);

        SaleItem item = (SaleItem) saleItemEntry.getPojo();
        
        DecimalFormat sizeFormat = new DecimalFormat();
        sizeFormat.setMinimumFractionDigits(0);
        sizeFormat.setMaximumFractionDigits(1);

        LabelWrap name = new LabelWrap(item.getItemName() + " (" + (item.getMeasurementSystem() == null ? "One Size" : item.getMeasurementSystem().equals(Measurement.Alpha.name()) ? item.getAlphaSize() : item.getMeasurementSystem() + " " + sizeFormat.format(item.getNumericSize())) + ")", getFont());

        FlatLabel quantityLabel = new FlatLabel();
        quantityLabel.setForeground(Palette.TEXT.color());
        quantityLabel.setFont(quantityLabel.getFont().deriveFont(Font.BOLD));
        quantityLabel.setText(String.format("%,d", quantity));

        FlatLabel price = new FlatLabel();
        price.setForeground(Palette.GREEN.color());
        price.setFont(price.getFont().deriveFont(Font.BOLD));
        price.setText(String.format("₱ %,.2f", item.getItemPrice() * quantity));

        FlatButton reduceButton = new FlatButton();
        reduceButton.setMargin(new Insets(0, 0, 0, 0));
        reduceButton.setBackground(Palette.TRANSPARENT.color());
        reduceButton.setToolTipText("Reduce");
        reduceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reduceButton.setIcon(new Iconify("minus", Palette.SURFACE1.color()).derive(reduceButton.getFont().getSize() + 2, reduceButton.getFont().getSize() + 2));

        reduceButton.addActionListener(e -> {
            owner.reduce(saleItemEntry);
        });
        
        reduceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                reduceButton.setBackground(Palette.MAROON.color());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                reduceButton.setBackground(Palette.TRANSPARENT.color());
            }
        });

        FlatButton increaseButton = new FlatButton();
        increaseButton.setMargin(new Insets(0, 0, 0, 0));
        increaseButton.setBackground(Palette.TRANSPARENT.color());
        increaseButton.setToolTipText("Increase");
        increaseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        increaseButton.setIcon(new Iconify("plus", Palette.SURFACE1.color()).derive(increaseButton.getFont().getSize() + 2, increaseButton.getFont().getSize() + 2));

        increaseButton.addActionListener(e -> {
            int qua = ((SaleItem) saleItemEntry.getPojo()).getItemQuantity();
            if (qua <= 0) {
                return;
            }
            owner.addToCart(saleItemEntry);
        });

        increaseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                increaseButton.setBackground(Palette.GREEN.color());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                increaseButton.setBackground(Palette.TRANSPARENT.color());
            }
        });

        itemInfo.add(name, "growx, width 100%");
        itemInfo.add(price);

        quantityInfo.add(increaseButton);
        quantityInfo.add(quantityLabel);
        quantityInfo.add(reduceButton);

        add(itemInfo, "grow");
        add(quantityInfo);
    }
}
