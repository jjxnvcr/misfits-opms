package app.software;

import java.awt.Dimension;

import javax.swing.JFrame;

import app.components.Page;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class App extends JFrame {
    private static final int WIDTH = 1450;
    private static final int HEIGHT = 820;
    private static final Dimension appSize = new Dimension(WIDTH, HEIGHT);
    private Nav nav;
    
    public App() {
        setTitle("Misfits OPMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(appSize);
        setMinimumSize(appSize);
        setLayout(new MigLayout("", "[]1%[fill, grow]"));
        getContentPane().setBackground(Palette.BASE.color());
        
        nav = new Nav(this);

        add(nav, "height 100%");

        nav.getDefaulButton().doClick();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void loadPage(Page page) {
        if (getContentPane().getComponentCount() == 2) {
            getContentPane().remove(1);
        }

        add(page, "height 100%");

        revalidate();
        repaint();
    }

    public Nav getNav() {
        return nav;
    }
}
