package app.components;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.extras.components.FlatButton;

import app.utils.ClientProperty;
import app.utils.Palette;

public class ListEntry extends FlatButton implements ActionListener {
    private Component owner;
    private Runnable action;
    private Object pojo;
    
    public ListEntry(Component owner, Object object) {
        super();

        this.owner = owner;
        this.pojo = object;

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        ClientProperty.setProperty(this, "arc", "10");
        setBackground(Palette.CRUST.color());
        setForeground(Palette.TEXT.color());

        addActionListener(this);
    }

    public void setAction(Runnable runnable) {
        this.action = runnable;
    }

    public Component getOwner() {
        return owner;
    }

    public Object getPojo() {
        return pojo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        action.run();
        
    }
}
