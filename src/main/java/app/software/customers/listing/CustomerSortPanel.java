package app.software.customers.listing;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.SortButton;

import app.db.pojo.column.CustomerColumn;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class CustomerSortPanel extends Page {
    private CustomerList owner;
    
    public CustomerSortPanel(CustomerList owner) {
        super(new MigLayout(""), false);

        this.owner = owner;

        FlatLabel label = new FlatLabel();
        label.setText("Sort by:");
        label.setForeground(Palette.TEXT.color());

        SortButton idSortButton = new SortButton(CustomerColumn.CustomerID, "ID");
        idSortButton.setAction(sortAction(idSortButton));   

        SortButton firstNameSortButton = new SortButton(CustomerColumn.FirstName, "First Name");
        firstNameSortButton.setAction(sortAction(firstNameSortButton));

        SortButton lastNameSortButton = new SortButton(CustomerColumn.LastName, "Last Name");
        lastNameSortButton.setAction(sortAction(lastNameSortButton));

        add(label);
        add(idSortButton);
        add(firstNameSortButton);
        add(lastNameSortButton);
    }

    private Runnable sortAction(SortButton button) {
        return () -> {
            if (button.getClicks() == 0) {
                owner.removeSort(button.getColumn());
            } else {
                owner.addSort(button.getColumn(), button.getClicks() == 1 ? Sort.ASC : Sort.DESC);
            }
            
            if (owner.getSearchPanel().isSearching()) {
                owner.loadCustomers(owner.getSearchPanel().getSearchBar().getText());
            } else {
                owner.loadCustomers();
            }
        };
    }
}
