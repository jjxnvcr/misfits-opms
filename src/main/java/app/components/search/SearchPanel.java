package app.components.search;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.db.pojo.column.Column;
import app.utils.Delay;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SearchPanel extends Page {
    private Component owner;
    private SearchBar searchBar;
    private FlatLabel feedback;
    private Map<Column, String> searchableColumns;
    private boolean searching = false;
    private boolean isSearchFilterVisible = false;
    private Runnable defaultSearch;
    private Runnable searchAction;

    public SearchPanel(Component owner) {
        super(new MigLayout("fill", "[][]"), false);
        
        this.owner = owner;

        feedback = new FlatLabel();
        feedback.setForeground(Palette.RED.color());

        searchBar = new SearchBar(this);

        add(searchBar, "grow, span");
        add(feedback);
        
    }

    public void showSearchFilter() {
        if (!isSearchFilterVisible) {
            buildSearchFilter();
            isSearchFilterVisible = true;
        } else {
            for (int i = 0; i < 2; i++) {
                remove(getComponentCount() - 1);
            }
            isSearchFilterVisible = false;
            revalidate();
            repaint();
        }
    }

    public void buildSearchFilter() {
        FlatButton closeFilterButton = new FlatButton();
        closeFilterButton.setBackground(Palette.TRANSPARENT.color());
        closeFilterButton.setIcon(new Iconify("x", Palette.SURFACE1.color()).derive(10, 10));
        closeFilterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeFilterButton.setToolTipText("Collapse Search Scope");

        closeFilterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeFilterButton.setBackground(Palette.SURFACE1.color());
                closeFilterButton.setIcon(new Iconify("x", getBackground()).derive(10, 10));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeFilterButton.setBackground(Palette.TRANSPARENT.color());
                closeFilterButton.setIcon(new Iconify("x", Palette.SURFACE1.color()).derive(10, 10));
            }
        });
        closeFilterButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt) {
               showSearchFilter();
           } 
        });

        add(closeFilterButton, "align 100%, pushx, wrap");
        add(new SearchFilter(this), "span, growx, height 35!");

        owner.revalidate();
        owner.repaint();
    }

    public void setFeedback(String message) {
        feedback.setText(message);
        feedback.revalidate();

        new Delay(() -> { feedback.setText(""); feedback.revalidate();}, 2500);
    }

    public void setDefaultSearch(Runnable defaultSearch) { this.defaultSearch = defaultSearch; }

    public void setSearchAction(Runnable searchAction) { this.searchAction = searchAction; }

    public void addSearchColumns(Map<Column, String> columns) { searchableColumns = columns; }

    public Component getOwner() { return owner; }

    public SearchBar getSearchBar() { return searchBar; }

    public boolean isSearchFilterVisible() { return isSearchFilterVisible; }

    public Runnable getDefaultSearch() { return defaultSearch; }

    public Runnable getSearchAction() { return searchAction; }

    public Map<Column, String> getMapSearchableColumns() { return searchableColumns; }
    public List<String> getListSearchableColumns() { 
        List<String> columns = new ArrayList<>();

        for (Column column : searchableColumns.keySet()) {
            columns.add(column.getName());
        }

        return columns;
    }

    public boolean isSearching() { return searching; }

    public void setSearching(boolean searching) { this.searching = searching; }
}
