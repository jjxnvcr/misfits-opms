package app.software.category.listing;

import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ActionButton;
import app.components.ListEntry;
import app.db.pojo.production.Category;
import app.software.category.form.CategoryDeleteConfirmation;
import app.software.category.form.CategoryEditForm;
import app.software.category.view.CategoryView;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CategoryEntry extends ListEntry {
    public CategoryEntry(CategoryList owner, Category category) {
        super(owner, category);

        setLayout(new MigLayout("wrap, fill"));
        
        ClientProperty.setProperty(this, "background", "darken(" + Palette.MANTLE.varString() + ", 6%)");

        FlatLabel id = new FlatLabel();
        id.setForeground(Palette.SURFACE1.color());
        id.setText("Ctg." + category.getCategoryId());
        id.setFont(id.getFont().deriveFont(Font.BOLD));

        FlatLabel name = new FlatLabel();
        name.setForeground(Palette.TEXT.color());
        name.setText(category.getCategoryName());
        name.setIcon(new Iconify("category", Palette.GREEN.color()).derive(name.getFont().getSize() + 4, name.getFont().getSize() + 4));
        name.setIconTextGap(5);
        
        setAction(() -> {
            if (owner.getActiveCategoryEntry() == this) {
                return;
            }
            
            ActionButton activeButton = owner.getActionPanel().getActiveButton();
            owner.setActiveCategoryEntry(this);
            
            if (activeButton == owner.getActionPanel().getViewButton()) {
                owner.getOwner().loadActionView(new CategoryView(owner, category));
            } else if (activeButton == owner.getActionPanel().getAddButton()) {
                owner.getActionPanel().getDefaultButton().doClick();
                doClick();
            } else if (activeButton == owner.getActionPanel().getEditButton()) {
                owner.getOwner().loadActionView(new CategoryEditForm(owner));
            }
            else if (activeButton == owner.getActionPanel().getDeleteButton()) {
                if (new CategoryDeleteConfirmation(this).isConfirmed()) {
                    owner.getOwner().removeActionView();
                    owner.resetActiveCategoryEntry();
                }
            }
        });

        add(id);
        add(name);
    }
}
