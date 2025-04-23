package app.components;

import app.utils.Action;

import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class ActionPanel extends Page {
    private ActionButton viewButton, addButton, editButton, deleteButton;
    private ActionButton defaultButton, activeButton;

    public ActionPanel() {
        super(new MigLayout("fill", "5[]5"), false);

        viewButton = new ActionButton(Action.VIEW, Palette.PEACH);
        addButton = new ActionButton(Action.ADD, Palette.MAUVE);
        editButton = new ActionButton(Action.EDIT, Palette.GREEN);
        deleteButton = new ActionButton(Action.DELETE, Palette.BLUE);

        add(viewButton);
        add(addButton);
        add(editButton);
        add(deleteButton);
        
        setDefaultButton(viewButton);
        setActiveButton(viewButton);
    }

    public void setButtonAction(ActionButton button, Runnable action) {
        button.setAction(action);
    }

    public void setDefaultButton(ActionButton button) { this.defaultButton = button; }
    public void setActiveButton(ActionButton button) {
        if (activeButton != null) { activeButton.setSelected(false); }

        this.activeButton = button; 
        activeButton.setSelected(true);
        activeButton.requestFocusInWindow();
    }

    public ActionButton getDefaultButton() { return defaultButton; }
    public ActionButton getActiveButton() { return activeButton; }

    public ActionButton getViewButton() { return viewButton; }
    public ActionButton getAddButton() { return addButton; }
    public ActionButton getEditButton() { return editButton; }
    public ActionButton getDeleteButton() { return deleteButton; }
}
