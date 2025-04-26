package app.components;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.SpinnerModel;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatCheckBox;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.utils.ClientProperty;
import app.utils.Delay;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class Form extends Page {
    private FlatLabel header;
    private FlatLabel feedback;

    public Form() {
        super(new MigLayout());

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        header = new FlatLabel();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 30));

        feedback = new FlatLabel();
        feedback.setForeground(Palette.RED.color());
        feedback.setHorizontalAlignment(FlatLabel.CENTER);
    }

    public FlatLabel createFieldLabel(String text) {
        FlatLabel label = new FlatLabel();
        label.setText(text);
        label.setFont(label.getFont().deriveFont(16));
        label.setForeground(Palette.OVERLAY1.color());
        return label;
    }

    public FlatTextField createField(String placeholder) {
        FlatTextField field = new FlatTextField();
        field.setPlaceholderText(placeholder);

        ClientProperty.setProperty(field, "arc", "15");
        ClientProperty.setProperty(field, "focusColor", Palette.OVERLAY1.varString());
        ClientProperty.setProperty(field, "focusWidth", "1");
        
        field.setBackground(Palette.CRUST.color());
        field.setForeground(Palette.TEXT.color());
        field.setCaretColor(Palette.SUBTEXT0.color());
        field.setFont(getFont().deriveFont(16));
        field.setMargin(new Insets(10,5,10,5));

        return field;
    }

    public  <T> FlatComboBox <T> createComboBox() {
        FlatComboBox<T> field = new FlatComboBox<>();
        field.setOpaque(false);
        field.setBackground(Palette.CRUST.color());
        field.setForeground(Palette.TEXT.color());
        field.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ClientProperty.setProperty(field, "arc", "15");
        ClientProperty.setProperty(field, "popupBackground", Palette.CRUST.varString());
        ClientProperty.setProperty(field, "buttonBackground", null);
        ClientProperty.setProperty(field, "buttonStyle", "none");
        ClientProperty.setProperty(field, "arrowType", "triangle");
        ClientProperty.setProperty(field, "maximumRowCount", "3");
        ClientProperty.setProperty(field, "padding", "10, 5, 10, 5");

        return field;
    }

    public FlatSpinner createSpinner(SpinnerModel model) {
        FlatSpinner field = new FlatSpinner();
        field.setModel(model);
        field.setOpaque(false);
        field.setBackground(Palette.CRUST.color());
        field.setForeground(Palette.TEXT.color());
        field.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ClientProperty.setProperty(field, "arc", "15");
        ClientProperty.setProperty(field, "buttonBackground", null);
        ClientProperty.setProperty(field, "buttonStyle", "none");
        ClientProperty.setProperty(field, "arrowType", "triangle");
        ClientProperty.setProperty(field, "padding", "10, 5, 10, 5");

        return field;
    }

    public FlatCheckBox createCheckBox(String text) {
        FlatCheckBox button = new FlatCheckBox();
        button.setText(text);
        button.setForeground(Palette.OVERLAY1.color());
        button.setIconTextGap(5);

        ClientProperty.setProperty(button, "icon.background", Palette.BASE.varString());
        ClientProperty.setProperty(button, "icon.checkmarkColor", Palette.CRUST.varString());
        ClientProperty.setProperty(button, "icon.borderWidth", "1");
        ClientProperty.setProperty(button, "icon.borderColor", Palette.SUBTEXT0.varString());
        ClientProperty.setProperty(button, "icon.selectedBackground", Palette.BLUE.varString());

        return button;
    }
    
    public FlatButton createConfirmButton(String text, Runnable action) {
        FlatButton button = new FlatButton();
        button.setText(text);

        ClientProperty.setProperty(button, "arc", "10");
        ClientProperty.setProperty(button, "focusColor", Palette.TEXT.varString());
        ClientProperty.setProperty(button, "focusWidth", "1");

        button.setBackground(Palette.BLUE.color());
        button.setForeground(getBackground());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(getFont().deriveFont(Font.BOLD, 16));
        button.setMargin(new Insets(10, 5, 10, 5));

        button.addActionListener(e -> action.run());

        return button;
    }

    public FlatButton createCancelButton(Runnable action) {
        FlatButton button = createConfirmButton("Cancel", action);
        button.setBackground(Palette.SURFACE0.color());
        button.setForeground(Palette.SUBTEXT0.color());

        return button;
    }

    public FlatButton createCancelButton(String text, Runnable action) {
        FlatButton button = createConfirmButton(text, action);
        button.setBackground(Palette.SURFACE0.color());
        button.setForeground(Palette.SUBTEXT0.color());

        return button;
    }
    
    public void setFeedback(String message) {
        feedback.setText(message);
        feedback.revalidate();

        new Delay(() -> { feedback.setText(""); feedback.revalidate();}, 2500);
    }

    public FlatLabel getFeedback() {
        return feedback;
    }

    public void setHeader(String text) {
        header.setText(text);
        header.revalidate();
    }

    public FlatLabel getHeader() {
        return header;
    }

    public boolean validateFields(FlatTextField... fields) {
        for (FlatTextField field : fields) {
            if (field.getText().isEmpty()) {
                field.requestFocusInWindow();
                setFeedback("All fields are required!");
                return false;
            }
        }
        return true;
    }

    public boolean validateComboBox(FlatComboBox<?>... fields) {
        for (FlatComboBox<?> field : fields) {
            if (field.getSelectedIndex() == 0) {
                field.requestFocusInWindow();
                setFeedback("All fields are required!");
                return false;
            }
        }
        return true;
    }
}
