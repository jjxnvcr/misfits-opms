package app.components;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import com.formdev.flatlaf.extras.components.FlatButton;

import app.software.App;
import app.software.Nav;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.NavState;
import app.utils.Palette;

public class NavButton extends FlatButton implements ActionListener {
    private Nav owner;
    private String page;
    private Iconify icon;
    private Color foreground;
    private String text;

    public NavButton(Nav owner, String page, Palette foreground, String iconName, String text) {
        super();

        this.owner = owner;
        this.page = page;
        this.foreground = foreground.color();
        icon = new Iconify(iconName, this.foreground);
        this.text = text;

        ClientProperty.setProperty(this, "arc", "999");
        ClientProperty.setProperty(this, "focusWidth", "1");
        ClientProperty.setProperty(this, "focusColor", foreground.varString());

        setBackground(Palette.TRANSPARENT.color());
        setForeground(foreground.color());
        setIcon(icon.setColor(foreground.color()));
        setIconTextGap(10);
        setIcon(icon);
        setHorizontalAlignment(FlatButton.LEADING);
        setText(text);
        setFont(getFont().deriveFont(Font.BOLD, 18));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(7, 5, 7, 5));

        addMouseListener(mouseAdapter(this));
        addActionListener(this);
    }

    private MouseAdapter mouseAdapter(NavButton button) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (owner.getActiveButton() != button) {
                    changeState(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (owner.getActiveButton() != button) {
                    changeState(false);
                }
            }
        };
    }

    public void changeState(boolean active) {
        if (active) {
            setBackground(foreground);
            setForeground(owner.getBackground());
            setIcon(icon.setColor(owner.getBackground()));
        } else {
            setBackground(Palette.TRANSPARENT.color());
            setForeground(foreground);
            setIcon(icon.setColor(foreground));
        }

        setSelected(active);
    }

    public void toggle(NavState state) {
        if (state == NavState.EXPAND) {
            setText(text);
            setMargin(new Insets(7, 5, 7, 5));
        } else {
            setText("");
            setToolTipText(text);
            setMargin(new Insets(5, 5, 5, 5));
        }

        revalidate();
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        owner.setActiveButton(this);

        try {
            Constructor<?> constructor = Class.forName(page).getConstructor(
                App.class
            );

            owner.getOwner().loadPage((Page) constructor.newInstance(owner.getOwner()));
        } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }
}
