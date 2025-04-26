package app.components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.UIManager;

import com.formdev.flatlaf.extras.components.FlatButton;

import app.utils.DialogType;
import net.miginfocom.swing.MigLayout;

class PopupDialogButton extends FlatButton {
    public PopupDialogButton(String text, int type) {
        super();
        setText(text);
        setFont(getFont().deriveFont(16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(5, 10, 5, 10));
        if (type == 1) {
            setBackground(UIManager.getColor("blue"));
            setForeground(UIManager.getColor("mantle"));
        } else if (type == 0) {
            setBackground(UIManager.getColor("surface0"));
            setForeground(UIManager.getColor("text"));
        }
    }
}

/**
 * A custom JDialog class for displaying pop-up dialogs. 
 * Designed to be used for confirmation, notification, and error messages in the application.
 */
public class PopupDialog extends JDialog {
    private LabelWrap message;
    private DialogType type;
    private boolean confirm;

    public PopupDialog(String title) {
        super();
        setTitle(title);
        setBackground(UIManager.getColor("mantle"));
        setPreferredSize(new Dimension(450, 250));
        setModalityType(ModalityType.APPLICATION_MODAL);
    }

    public void display() {
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        requestFocus();
    }

    public void setDialogType(DialogType type) {
        this.type = type;

        if (type == DialogType.CONFIRMATION) {
            setLayout(
                new MigLayout(
                    "insets 15, fill, align center",
                     "[50%]5%[50%]",
                      "5%[pref!]5%[pref!]5%"
                )
            );
        } else if (type == DialogType.NOTIFICATION || type == DialogType.ERROR) {
            setLayout(
                new MigLayout(
                    "insets 15, fill, align center",
                    "[]",
                    "5%[pref!]5%"
                )
            );
        }
    }

    public void setMessage(String message) {
        this.message = new LabelWrap(message, new Font("Inter", Font.PLAIN, 14));

        if (type == DialogType.CONFIRMATION || type == DialogType.NOTIFICATION) {
            this.message.setForeground(UIManager.getColor("text"));
        } else if (type == DialogType.ERROR) {
            this.message.setForeground(UIManager.getColor("red"));
        }

        add(this.message, "grow, span, wrap, gapbottom 10");
    }

    public void setCloseButtonAction(Runnable action) {
        PopupDialogButton closeButton = new PopupDialogButton("Close", 0);
        closeButton.addActionListener(e -> {
            action.run();
            confirm = false;
            setVisible(false);
        });

        add(closeButton, "grow");
    }

    public void setConfirmButtonAction(Runnable action) {
        PopupDialogButton confirmButton = new PopupDialogButton("Confirm", 1);
        confirmButton.addActionListener(e -> {
            action.run();
            confirm = true;
        });

        add(confirmButton, "grow, wrap");
    }

    public boolean isConfirmed() {
        return confirm;
    }
}


