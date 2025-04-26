package app.components;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class ScrollView extends JPanel {
    public ScrollView(int columns) {
        super();

        setLayout(new MigLayout("insets 0, fillx, align center, wrap " + columns, columns > 1 ? "10[]10" : "", "10[]10"));
        setOpaque(false);
    }
}
