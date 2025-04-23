package app.components;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import app.utils.ClientProperty;
import app.utils.Palette;

public class Page extends JPanel {
    Palette palette;

    public Page() {
        super();
    }

    public Page(LayoutManager layout) {
        this();
        setLayout(layout);
    }

    public Page(LayoutManager layout, boolean opaque) {
        this(layout);
        setOpaque(opaque);
    }

    public void setArc(int arc) {
        ClientProperty.setProperty(this, "arc", "" + arc);
    }

    public void setBackground(Palette palette) {
        this.palette = palette;

        ClientProperty.setProperty(this, "background", palette.varString());
    }

    public void lightenBackground(int percent) {
        ClientProperty.setProperty(
            this, 
            "background", 
            String.format("lighten(%s, %s)", palette.varString(), percent + "%"));
    }

    public void darkenBackground(int percent) {
        ClientProperty.setProperty(
            this, 
            "background", 
            String.format("darken(%s, %s)", palette.varString(), percent + "%"));
    }
}
