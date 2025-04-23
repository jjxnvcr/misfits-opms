package app.components;

import java.awt.Cursor;

import javax.swing.UIManager;

import com.formdev.flatlaf.extras.components.FlatScrollPane;

public class ScrollList extends FlatScrollPane {
    public ScrollList(ScrollView view) {
        super();
        setOpaque(false);
        setBorder(null);
        setViewportBorder(null);
        getViewport().setOpaque(false);

        getVerticalScrollBar().setUnitIncrement(13);
        getVerticalScrollBar().setBackground(UIManager.getColor("transparent"));
        getVerticalScrollBar().setCursor(new Cursor(Cursor.HAND_CURSOR));

        getHorizontalScrollBar().setUnitIncrement(13);
        getHorizontalScrollBar().setBackground(UIManager.getColor("transparent"));
        getHorizontalScrollBar().setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        setViewportView(view);
    }

    public ScrollList() {
        super();

        setOpaque(false);
        setBorder(null);
        setViewportBorder(null);
        getViewport().setOpaque(false);

        getVerticalScrollBar().setUnitIncrement(13);
        getVerticalScrollBar().setBackground(UIManager.getColor("transparent"));
        getVerticalScrollBar().setCursor(new Cursor(Cursor.HAND_CURSOR));

        getHorizontalScrollBar().setUnitIncrement(13);
        getHorizontalScrollBar().setBackground(UIManager.getColor("transparent"));
        getHorizontalScrollBar().setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
