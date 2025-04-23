package app.components.search;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.extras.components.FlatButton;

import app.db.pojo.column.Column;
import app.utils.Palette;

public class FilterButton extends FlatButton implements ActionListener {
    private SearchPanel owner;
    private Column column;

    public FilterButton(SearchPanel owner, Column column, String text) {
        super();

        this.owner = owner;
        this.column = column;

        setText(text);
        setIconTextGap(10);
        setFont(getFont().deriveFont(12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(Palette.SURFACE0.color());
        setForeground(Palette.SUBTEXT1.color());

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SearchBar searchBar = owner.getSearchBar();
        if (!searchBar.getText().endsWith("=") && !searchBar.getText().endsWith("= ")) {
            searchBar.setText(searchBar.getText() + (searchBar.getText().isBlank() ? "" : ":") + "@" + column.getName() + "=");
        } else {
            owner.setFeedback("Supply a value for the current column first!");
        }

        searchBar.requestFocusInWindow();
    }
}
