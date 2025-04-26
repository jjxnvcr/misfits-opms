package app.utils;

import java.awt.Color;

import com.formdev.flatlaf.extras.FlatSVGIcon;

/**
 * A class wrapper for FlatSVGIcon with methods for setting the icon color.
 */

public class Iconify extends FlatSVGIcon {
    private static String iconPath = "icons/%s.svg";

    public Iconify(String iconName) {
        super(String.format(iconPath, iconName));
    }

    public Iconify(String iconName, Color color) {
        super(String.format(iconPath, iconName));
        setColorFilter(new FlatSVGIcon.ColorFilter(e -> color));
    }
    
    public Iconify setColor(Color color) {
        setColorFilter(new FlatSVGIcon.ColorFilter(e -> color));
        return this;
    }
}
