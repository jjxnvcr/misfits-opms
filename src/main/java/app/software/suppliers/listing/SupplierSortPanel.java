package app.software.suppliers.listing;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.SortButton;
import app.db.pojo.column.SupplierColumn;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class SupplierSortPanel extends Page {
    private SupplierList owner;
    
    public SupplierSortPanel(SupplierList owner) {
        super(new MigLayout(""), false);

        this.owner = owner;

        FlatLabel label = new FlatLabel();
        label.setText("Sort by:");
        label.setForeground(Palette.SUBTEXT0.color());

        SortButton idSortButton = new SortButton(SupplierColumn.SupplierID, "ID");
        idSortButton.setAction(sortAction(idSortButton));

        SortButton nameSortButton = new SortButton(SupplierColumn.SupplierName, "Name");
        nameSortButton.setAction(sortAction(nameSortButton));

        add(label);
        add(idSortButton);
        add(nameSortButton);
    }

    private Runnable sortAction(SortButton button) {
        return () -> {
            if (button.getClicks() == 0) {
                owner.removeSort(button.getColumn());
            } else {
                owner.addSort(button.getColumn(), button.getClicks() == 1 ? Sort.ASC : Sort.DESC);
            }

            if (owner.getSearchPanel().isSearching()) {
                owner.loadSupplier(owner.getSearchPanel().getSearchBar().getText());
            } else {
                owner.loadSupplier();
            }
        };
    }
}