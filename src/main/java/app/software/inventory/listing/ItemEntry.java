package app.software.inventory.listing;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ActionButton;
import app.components.ListEntry;

import app.db.pojo.production.Item;
import app.software.inventory.form.ItemDeleteForm;
import app.software.inventory.form.ItemEditForm;
import app.software.inventory.view.ItemView;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class ItemEntry extends ListEntry {
    public ItemEntry(InventoryList owner, Item item) {
        super(owner, item);

        setLayout(new MigLayout("wrap, fill"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.SURFACE1.color());
        id.setText("Itm." + item.getItemId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        FlatLabel name = new FlatLabel();
        name.setForeground(Palette.TEXT.color());
        name.setText(item.getItemName());
        name.setIcon(new Iconify("hanger", Palette.BLUE.color()).derive(name.getFont().getSize() + 4, name.getFont().getSize() + 4));
        name.setIconTextGap(5);
        
        setAction(() -> {
            if (owner.getActiveItemEntry() == this) {
                return;
            }
            
            ActionButton activeButton = owner.getActionPanel().getActiveButton();
            owner.setActiveItemEntry(this);
            
            if (activeButton == owner.getActionPanel().getViewButton()) {
                owner.getOwner().loadActionView(new ItemView(owner, item));
            } else if (activeButton == owner.getActionPanel().getAddButton()) {
                owner.getActionPanel().getDefaultButton().doClick();
                doClick();
            } else if (activeButton == owner.getActionPanel().getEditButton()) {
                owner.getOwner().loadActionView(new ItemEditForm(owner));
            }
            else if (activeButton == owner.getActionPanel().getDeleteButton()) {
                owner.getOwner().loadActionView(new ItemDeleteForm(owner));
            }
        });

        add(id);
        add(name);
    }
}
