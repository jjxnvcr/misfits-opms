package app.components.search;

import java.awt.Insets;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.formdev.flatlaf.extras.components.FlatTextField;

import app.utils.ClientProperty;
import app.utils.Palette;

public class SearchBar extends FlatTextField {
    private SearchPanel owner;

    public SearchBar(SearchPanel owner) {
        super();
        this.owner = owner;

        setPlaceholderText("To search, use @{column_name}={value} separated by a colon for each pair");
        setMargin(new Insets(7, 5, 7, 5));    
        setFont(getFont().deriveFont(14));
        setBackground(Palette.MANTLE.color());
        setForeground(Palette.TEXT.color());
        setCaretColor(Palette.SURFACE1.color());
        ClientProperty.setProperty(this, "focusColor", Palette.SURFACE1.varString());
        ClientProperty.setProperty(this, "focusWidth", "0");
        ClientProperty.setProperty(this, "arc", "20");
        ClientProperty.setProperty(this, "showClearButton", "true");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                owner.showSearchFilter();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = getText().trim();

                    if (text.isBlank()) {
                        owner.getDefaultSearch().run();
                        owner.setSearching(false);
                    } else if (!text.contains("@") || !text.contains("=")) {
                        owner.setFeedback("Invalid search format!");
                    } else if (!validateColumns(text)) {
                        owner.setFeedback("Invalid column name!");
                    } else if (text.endsWith("=")) {
                        owner.setFeedback("Supply a value for the current column first!");
                    } else {
                        owner.getSearchAction().run();
                        owner.setSearching(true);
                        transferFocus();
                    }
                }
            }
        });
    
        addCaretListener(e -> {
            if (getText().isBlank()) {
                owner.setSearching(false);
            }
        });
    }

    private boolean validateColumns(String text) {
        String[] search = text.split(":");

        for (int i = 0; i < search.length - 1; i++) {
            String column = search[i].split("=")[0].substring(1);

            if (!owner.getListSearchableColumns().contains(column)) {
                return false;
            }
        }

        return true;
    }
}
