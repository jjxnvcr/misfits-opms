package app.software.cashier.view;

import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Form;
import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.db.pojo.sales.SaleItem;
import app.software.cashier.listing.CashierList;
import app.software.cashier.listing.SaleItemEntry;
import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CartView extends Form {
    Map<SaleItemEntry, Integer> cart = new LinkedHashMap<>();
    private FlatLabel header;
    private FlatLabel price;
    private Page cartView;
    private CashierList owner;
    private double totalPrice = 0;
    private int totalQuantity = 0;
    
    private CartScrollView view = new CartScrollView();

    public CartView(CashierList owner) {
        super();

        this.owner = owner;

        setLayout(new MigLayout("fillx", "[50%]3%[50%]"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        header = new FlatLabel();
        header.setText("Cart (0)");
        header.setForeground(Palette.TEXT.color());
        header.setFont(header.getFont().deriveFont(Font.BOLD, 30));
        header.setIcon(new Iconify("cart", Palette.LAVENDER.color()).derive(60, 60));
        header.setIconTextGap(5);

        cartView = new Page(new MigLayout("fillx"));
        cartView.setArc(10);
        cartView.setBackground(Palette.CRUST);

        FlatLabel itemNameLabel = new FlatLabel();
        itemNameLabel.setForeground(Palette.SUBTEXT0.color());
        itemNameLabel.setText("Item");

        FlatLabel quantityLabel = new FlatLabel();
        quantityLabel.setForeground(Palette.SUBTEXT0.color());
        quantityLabel.setText("Quantity");

        cartView.add(itemNameLabel, "align 20%, pushx");
        cartView.add(quantityLabel, "align 80%, pushx, wrap");
        cartView.add(new ScrollList(view), "span, grow");

        FlatLabel priceLabel = new FlatLabel();
        priceLabel.setForeground(Palette.SUBTEXT0.color());
        priceLabel.setText("Total: ");
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 18));

        price = new FlatLabel();
        price.setForeground(Palette.GREEN.color());
        price.setFont(priceLabel.getFont());
        price.setText("₱ 0.00");
        
        add(header, "growx, wrap");
        add(cartView, "grow, height 60%, span, wrap");
        add(priceLabel);
        add(price, "align 100%, pushx, wrap, gapbottom 20");
        add(createCancelButton("Clear", () -> {
            clearCart();
        }), "grow");
        add(createConfirmButton("Checkout", () -> {
            if (cart.size() <= 0) {
                PopupDialog notif = new PopupDialog("Nothing to Checkout");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Add items to the cart first!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
                return;
            }
            owner.getOwner().loadActionView(new Checkout(this));
        }), "grow");
    }

    public void addToCart(SaleItemEntry saleItemEntry) {
        if (cart.containsKey(saleItemEntry)) {
            cart.computeIfPresent(saleItemEntry, (k, v) -> v + 1);
        } else {
            cart.put(saleItemEntry, 1);
        }

        saleItemEntry.updateQuantity(((SaleItem) saleItemEntry.getPojo()).getItemQuantity() - 1);

        view.removeAll();

        List<SaleItemEntry> saleItemEntries = cart.keySet().stream().toList();
        saleItemEntries.forEach(e -> view.add(new CartItem(this, e, cart.get(e)), "growx"));

        computeRunningTotal();

        cartView.revalidate();
        cartView.repaint();
    }

    public void removeFromCart(SaleItemEntry saleItemEntry) {
        if (cart.size() <= 0) {
            return;
        }
        cart.remove(saleItemEntry);

        view.removeAll();

        for (Map.Entry<SaleItemEntry, Integer> entry : cart.entrySet()) {
            if (entry.getKey() == saleItemEntry) {
                saleItemEntry.updateQuantity(((SaleItem) saleItemEntry.getPojo()).getItemQuantity() + entry.getValue());

                totalQuantity += entry.getValue();
                totalPrice += ((SaleItem) entry.getKey().getPojo()).getItemPrice() * entry.getValue();
            }
        }
        List<SaleItemEntry> saleItemEntries = cart.keySet().stream().toList();
        saleItemEntries.forEach(e -> view.add(new CartItem(this, e, cart.get(e)), "growx"));

        computeRunningTotal();

        cartView.revalidate();
        cartView.repaint();
    }

    public void reduce(SaleItemEntry saleItemEntry) {
        if (cart.size() <= 0) {
            return;
        }
    
        int currentValue = cart.get(saleItemEntry) - 1;

        if (currentValue == 0) {
            cart.remove(saleItemEntry);
        } else {
            cart.put(saleItemEntry, currentValue);
        }

        view.removeAll();

        saleItemEntry.updateQuantity(((SaleItem) saleItemEntry.getPojo()).getItemQuantity() + 1);

        totalQuantity -= currentValue;
        totalPrice -= ((SaleItem) saleItemEntry.getPojo()).getItemPrice() * (currentValue);
        
        List<SaleItemEntry> saleItemEntries = cart.keySet().stream().toList();
        saleItemEntries.forEach(e -> view.add(new CartItem(this, e, cart.get(e)), "growx"));

        computeRunningTotal();

        cartView.revalidate();
        cartView.repaint();
    }

    public void clearCart() {
        if (cart.size() <= 0) {
            return;
        }

        for (Map.Entry<SaleItemEntry, Integer> entry : cart.entrySet()) {
            entry.getKey().updateQuantity(((SaleItem) entry.getKey().getPojo()).getItemQuantity() + entry.getValue());
        }

        cart.clear();
        view.removeAll();

        computeRunningTotal();

        cartView.revalidate();
        cartView.repaint();
    }

    private void computeRunningTotal() {
        totalQuantity = 0;
        totalPrice = 0;

        for (Map.Entry<SaleItemEntry, Integer> entry : cart.entrySet()) {
            totalQuantity += entry.getValue();
            totalPrice += ((SaleItem) entry.getKey().getPojo()).getItemPrice() * entry.getValue();
        }

        header.setText("Cart (" + totalQuantity + ")");
        price.setText(String.format("₱ %,.2f", totalPrice));
    }

    public CashierList getOwner() {
        return owner;
    }

    public Map<SaleItemEntry, Integer> getCart() {
        return cart;
    }
}
