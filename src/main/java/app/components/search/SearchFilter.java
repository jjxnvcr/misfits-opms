package app.components.search;

import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatScrollPane;

import app.utils.ClientProperty;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class SearchFilter extends FlatScrollPane {
    public SearchFilter(SearchPanel owner) {
        super();

        setOpaque(false);
        setBorder(null);

        setViewportBorder(null);
        getViewport().setOpaque(false);

        setVerticalScrollBarPolicy(FlatScrollPane.VERTICAL_SCROLLBAR_NEVER);

        getHorizontalScrollBar().setUnitIncrement(5);
        getHorizontalScrollBar().setBackground(UIManager.getColor("transparent"));
        getHorizontalScrollBar().setCursor(new Cursor(Cursor.HAND_CURSOR));
        ClientProperty.setProperty(getHorizontalScrollBar(),"width", "7");

        JPanel view = new JPanel(new MigLayout("insets 5", "10[]10"));
        view.setOpaque(false);

        FlatLabel label = new FlatLabel();
        label.setText("Search columns:");
        label.setForeground(Palette.SUBTEXT0.color());
        
        view.add(label);
        
        owner.getMapSearchableColumns().forEach(
            (key, value) -> {
                view.add(new FilterButton(owner, key, value));
            }
        );

        setViewportView(view);

        revalidate();
        repaint();
    }
}
