package app;

import java.awt.EventQueue;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import app.software.App;

public class Main {
    public static void main(String[] args) {
        //Program entry point

        setupLaF();
        runApp();
    }

    static void setupLaF() {
        FlatInterFont.install();
        FlatLaf.setPreferredFontFamily(FlatInterFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatInterFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatInterFont.FAMILY_SEMIBOLD);

        FlatLaf.registerCustomDefaultsSource("laf");
        FlatLaf.setup(new FlatMacDarkLaf());
    }

    static void runApp() {
        EventQueue.invokeLater(() -> {
            new App();
        });
    }
}