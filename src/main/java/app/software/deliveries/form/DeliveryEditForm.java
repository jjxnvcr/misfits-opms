package app.software.deliveries.form;

import java.sql.SQLException;

import javax.swing.SpinnerNumberModel;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatSpinner;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.sales.DeliveryDao;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.column.PaymentMethod;
import app.db.pojo.sales.Delivery;
import app.software.deliveries.listing.DeliveryList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class DeliveryEditForm extends Form {
    private FlatComboBox<String> statusField;
    private FlatSpinner paidAmountField;
    private int selectedStatusIndex;

    public DeliveryEditForm(DeliveryList owner) {
        super();
        
        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Edit Delivery");
        getHeader().setForeground(Palette.YELLOW.color());

        Delivery delivery = (Delivery) owner.getActiveDeliveryEntry().getPojo();

        FlatLabel statusLabel = createFieldLabel("Delivery Status");
        statusField = createComboBox();

        for (DeliveryStatus status: DeliveryStatus.values()) {
            if (status == DeliveryStatus.OutForDelivery) {
                statusField.addItem("Out for Delivery");
                continue;
            }
            statusField.addItem(status.name());
        }

        statusField.setSelectedItem(delivery.getDeliveryStatus());

        selectedStatusIndex = statusField.getSelectedIndex();

        statusField.addActionListener(e -> {
            if (statusField.getSelectedIndex() - selectedStatusIndex > 1 && statusField.getSelectedIndex() != statusField.getItemCount() - 1) {
                statusField.setSelectedIndex(selectedStatusIndex);
                PopupDialog error = new PopupDialog("Invalid Delivery Flow");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Mark the delivery as '" + statusField.getItemAt( selectedStatusIndex + 1).toString() + "' first.");
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
                return;
            }
        });

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");

        add(statusLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(statusField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");

        try {
            if (delivery.getDeliveryStatus().equals("Out for Delivery") && SalesTransactionDao.getSalesTransaction(delivery.getTransactionId()).getPaymentType().equals(PaymentMethod.Cash.name())) {
                FlatLabel paidAmountLabel = createFieldLabel("Paid Amount");
                paidAmountField = createSpinner(new SpinnerNumberModel(0, 0, 9999999, 50));

                add(paidAmountLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
                add(paidAmountField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");
        add(createConfirmButton("Edit", () -> {
            if (paidAmountField != null) {
                double amount = Double.parseDouble(paidAmountField.getValue().toString());

                try {
                    if (amount < SalesTransactionDao.getTransactionTotal(delivery.getTransactionId())) {
                        setFeedback("Amount is less than the amount to be paid!");
                        return;
                    } else if (amount > SalesTransactionDao.getTransactionTotal(delivery.getTransactionId())) {
                        setFeedback("Amount is greater than the amount to be paid!");
                        return;
                    }
                } catch (Exception e) {
                    setFeedback("An error occurred. Please try again.");
                    e.printStackTrace();
                }
            }
            PopupDialog confirm = new PopupDialog("Edit Delivery");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to edit this delivery?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    delivery.setDeliveryStatus(statusField.getSelectedIndex() == 0 ? statusField.getPlaceholderText() : statusField.getSelectedItem().toString());

                    if (paidAmountField != null) {
                        delivery.setPaidAmount(Double.parseDouble(paidAmountField.getValue().toString()));
                    }
                    
                    DeliveryDao.updateDelivery(delivery);

                    confirm.dispose();

                    PopupDialog notif = new PopupDialog("Delivery Edited");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Delivery has been successfully edited!");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();

                    owner.loadDeliveries();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Edit Delivery");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to edit delivery.\n\n Error: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                    e.printStackTrace();
                }
            });
            confirm.display();
        }), "grow, gapbottom 5%, gapright 5%");
    }
}
