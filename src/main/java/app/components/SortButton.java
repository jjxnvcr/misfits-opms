package app.components;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.extras.components.FlatButton;

import app.db.pojo.column.Column;

import app.utils.Iconify;
import app.utils.Palette;
import app.utils.Sort;

public class SortButton extends FlatButton implements ActionListener {
    private Column column;
    private Runnable action;
    private int clicks = 0;

    public SortButton(Column column, String text) {
        super();

        this.column = column;

        setText(text);
        setIconTextGap(10);
        setFocusable(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(Palette.SURFACE0.color());
        setForeground(Palette.SUBTEXT1.color());
        
        addActionListener(this);
    }

    public Column getColumn() {
        return column;
    }

    public void setAction(Runnable runnable) {
        this.action = runnable;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        requestFocusInWindow();
        clicks = clicks + 1 > 2 ? 0 : clicks + 1;

        if (clicks > 0) {
            setIcon(new Iconify(Sort.values()[clicks - 1].getName().toLowerCase(), getForeground()).derive(getFont().getSize(), getFont().getSize()));
        } else {
            setIcon(null);
        }

        action.run();
    }   
}
