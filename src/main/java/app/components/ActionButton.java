package app.components;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.extras.components.FlatButton;

import app.utils.Action;
import app.utils.ClientProperty;
import app.utils.Iconify;
import app.utils.Palette;

public class ActionButton extends FlatButton implements ActionListener {
    private Runnable action;

    public ActionButton(Action action, Palette palette) {
        super();
        ClientProperty.setProperty(this, "focusColor", palette.varString());
        ClientProperty.setProperty(this, "focusWidth", "1");
        setBackground(Palette.BASE.color());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setIcon(new Iconify(action == Action.VIEW ? "eye" : action == Action.ADD ? "plus" : action == Action.EDIT ? "pencil" : "trash", palette.color()).derive(20, 20));
        setToolTipText(action == Action.VIEW ? "View" : action == Action.ADD ? "Add" : action == Action.EDIT ? "Edit" : "Delete");

        addActionListener(this);
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        action.run();
    }
}
