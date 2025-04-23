package app.utils;

import java.awt.Color;

import javax.swing.UIManager;

/**
 * An enum of colors used throughout the application.  
 * Additional method is added to integrate the enum with FlatLaF
 * 
 * Sourced from Catpuccin Palette (https://github.com/catppuccin)
 */

public enum Palette {
    CRUST("crust"),
    MANTLE("mantle"),
    BASE("base"),
    SURFACE2("surface2"),
    SURFACE1("surface1"),
    SURFACE0("surface0"),
    OVERLAY2("overlay2"),
    OVERLAY1("overlay1"),
    OVERLAY0("overlay0"),
    SUBTEXT1("subtext1"),
    SUBTEXT0("subtext0"),
    TEXT("text"),
    PEACH("peach"),
    MAUVE("mauve"),
    GREEN("green"),
    BLUE("blue"),
    MAROON("maroon"),
    YELLOW("yellow"),
    TEAL("teal"),
    PINK("pink"),
    LAVENDER("lavender"),
    RED("red"),
    TRANSPARENT("transparent");    

    private final String color;

    Palette(String color) {
        this.color = color;
    }

    public Color color() {
        return UIManager.getColor(color);
    }

    public String string() {
        return color;
    }

    public String varString() {
        return "$" + color;
    }
}
