package app.utils;

import javax.swing.JComponent;

import com.formdev.flatlaf.FlatClientProperties;

public class ClientProperty {
    public static void setProperty(JComponent c, String key, String value) {
        String style = c.getClientProperty(FlatClientProperties.STYLE) != null ? c.getClientProperty(FlatClientProperties.STYLE).toString() : "";

        c.putClientProperty(FlatClientProperties.STYLE, style + key + ":" + value + ";");
    }  
}
