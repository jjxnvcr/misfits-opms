package app.components;

import java.awt.Cursor;
import java.awt.Font;

import com.formdev.flatlaf.extras.components.FlatTextArea;

import app.utils.Palette;

public class LabelWrap extends FlatTextArea {
    public LabelWrap(String text, Font font) {
        super();
        setText(text);
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(false);
        setEditable(false);
        setBorder(null);
        setFocusable(false);
        setForeground(Palette.TEXT.color());
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setFont(font);
    }
}
