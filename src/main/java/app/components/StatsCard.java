package app.components;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Font;

public class StatsCard extends Page {
    private ScrollView view;

    public StatsCard(int wrap) {
        super(new MigLayout("wrap, fillx"), false);

        FlatLabel statsLabel = new FlatLabel();
        statsLabel.setForeground(Palette.SUBTEXT0.color());
        statsLabel.setText("Statistics");

        view = new ScrollView(wrap);

        add(statsLabel, "wrap");
        add(new ScrollList(view), "grow");
    }

    public void addCard(String text, String value, Color foreground) {
        Page page = new Page(new MigLayout("fillx, wrap"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel textLabel = new FlatLabel();
        textLabel.setText(text);
        textLabel.setForeground(Palette.SURFACE2.color());
        textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getSize() + 2));
        textLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        FlatLabel valueLabel = new FlatLabel();
        valueLabel.setText(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16));
        valueLabel.setForeground(foreground);
        valueLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        page.add(textLabel, "growx");
        page.add(valueLabel, "growx");

        view.add(page, "growx");
        view.revalidate();
        view.repaint();
    }

    public void removeCards() {
        view.removeAll();
    }

    public void reloadCards() {
        view.revalidate();
        view.repaint();
    }
}
