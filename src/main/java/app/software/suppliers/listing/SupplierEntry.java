package app.software.suppliers.listing;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ActionButton;
import app.components.ListEntry;
import app.db.pojo.production.Supplier;

import app.software.suppliers.form.SupplierDeleteConfirmation;
import app.software.suppliers.form.SupplierEditForm;
import app.software.suppliers.view.SupplierView;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SupplierEntry extends ListEntry {
    public SupplierEntry(SupplierList owner, Supplier supplier) {
        super(owner, supplier);

        setLayout(new MigLayout("wrap, fill"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.SURFACE1.color());
        id.setText("Sup." + supplier.getSupplierId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        FlatLabel name = new FlatLabel();
        name.setForeground(Palette.TEXT.color());
        name.setText(supplier.getSupplierName());
        name.setIcon(new Iconify("address-book", Palette.PINK.color()).derive(name.getFont().getSize() + 4, name.getFont().getSize() + 4));
        name.setIconTextGap(5);
        
        setAction(() -> {
            if (owner.getActiveSupplierEntry() == this) {
                return;
            }
            
            ActionButton activeButton = owner.getActionPanel().getActiveButton();
            owner.setActiveSupplierEntry(this);
            
            if (activeButton == owner.getActionPanel().getViewButton()) {
                owner.getOwner().loadActionView(new SupplierView(owner, supplier));
            } else if (activeButton == owner.getActionPanel().getAddButton()) {
                owner.getActionPanel().getDefaultButton().doClick();
                doClick();
            } else if (activeButton == owner.getActionPanel().getEditButton()) {
                owner.getOwner().loadActionView(new SupplierEditForm(owner));
            }
            else if (activeButton == owner.getActionPanel().getDeleteButton()) {
                if (new SupplierDeleteConfirmation(this).isConfirmed()) {
                    owner.getOwner().removeActionView();
                    owner.resetActiveSupplierEntry();
                }
            }
        });

        add(id);
        add(name);
    }
}
