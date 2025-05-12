package app.software.cashier.view;

import java.util.Map;

import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.Page;
import app.components.PopupDialog;
import app.db.dao.sales.DeliveryDao;
import app.db.dao.sales.SalesTransactionDao;
import app.db.dao.sales.TransactionItemDao;
import app.db.pojo.column.EWallet;
import app.db.pojo.column.PaymentMethod;
import app.db.pojo.customer.Customer;
import app.db.pojo.sales.Delivery;
import app.db.pojo.sales.SaleItem;
import app.db.pojo.sales.SalesTransaction;
import app.db.pojo.sales.TransactionItem;
import app.software.cashier.listing.SaleItemEntry;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CheckoutPayment extends Form {
    private FlatComboBox<String> paymentTypeField;
    private Page fieldContainer;
    private FlatTextField mobileNumberField;
    private FlatComboBox<String> eWalletField;
    private FlatTextField bankField;
    private FlatTextField accountNumberField;
    private FlatTextField accountNameField;

    public CheckoutPayment(Checkout owner, Delivery delivery, Customer customer) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setArc(10);
        setBackground(Palette.CRUST);
        lightenBackground(6);

        setHeader("Setup Payment");
        getHeader().setForeground(Palette.LAVENDER.color());

        fieldContainer = new Page(new MigLayout("insets 0, fillx", "[50%]3%[50%]"), false);

        FlatLabel paymentTypeLabel = createFieldLabel("Payment Type");
        paymentTypeField = createComboBox();

        for (PaymentMethod paymentMethod: PaymentMethod.values()) {
            if (paymentMethod.name().equals("OnlineBank")) {
                paymentTypeField.addItem("Online Bank");
                continue;
            }

            paymentTypeField.addItem(paymentMethod.name());
        }
        paymentTypeField.setSelectedIndex(0);
        paymentTypeField.addActionListener(e -> {
            if (paymentTypeField.getSelectedItem().equals("Online Bank")) {
                fieldContainer.removeAll();
               
                FlatLabel bankLabel = createFieldLabel("Bank Name");
                bankField = createField("");

                FlatLabel accountNumberLabel = createFieldLabel("Account Number");
                accountNumberField = createField("");

                FlatLabel accountNameLabel = createFieldLabel("Account Name");
                accountNameField = createField("");

                fieldContainer.add(bankLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
                fieldContainer.add(bankField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
                fieldContainer.add(accountNumberLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
                fieldContainer.add(accountNumberField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
                fieldContainer.add(accountNameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
                fieldContainer.add(accountNameField, "wrap, span, grow, gapleft 5%, gapright 5%");
                
                fieldContainer.revalidate();
                fieldContainer.repaint();
            } else if (paymentTypeField.getSelectedItem().equals(PaymentMethod.EWallet.name())) {
                fieldContainer.removeAll();

                FlatLabel mobileNumberLabel = createFieldLabel("Mobile Number");
                mobileNumberField = createField("");

                FlatLabel eWalletLabel = createFieldLabel("E-Wallet Provider");
                eWalletField = createComboBox();

                for (EWallet eWallet: EWallet.values()) {
                    eWalletField.addItem(eWallet.name());
                }

                eWalletField.setSelectedIndex(0);

                fieldContainer.add(mobileNumberLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
                fieldContainer.add(mobileNumberField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
                fieldContainer.add(eWalletLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
                fieldContainer.add(eWalletField, "wrap, span, grow, gapleft 5%, gapright 5%");

                fieldContainer.revalidate();
                fieldContainer.repaint();
            } else {
                fieldContainer.removeAll();
                fieldContainer.revalidate();
                fieldContainer.repaint();
            }
        });

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(paymentTypeLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(paymentTypeField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");
        add(fieldContainer, "span, grow, gapbottom 5%");
        
        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getOwner().getOwner().getOwner().loadActionView(owner);
        }), "grow, gapleft 5%, gapbottom 5%");
        add(createConfirmButton("Finish", () -> {
            String paymentType = paymentTypeField.getSelectedItem().toString();

            if (paymentType.equals(PaymentMethod.EWallet.name())) {
                if (!validateFields(mobileNumberField)) {
                    mobileNumberField.requestFocusInWindow();
                    setFeedback("All fields are required!");
                    return;
                } else if (!mobileNumberField.getText().matches("\\d+")) {
                    mobileNumberField.requestFocusInWindow();
                    setFeedback("Invalid mobile number!");
                    return;
                } else if (mobileNumberField.getText().length() != 11) {
                    mobileNumberField.requestFocusInWindow();
                    setFeedback("Mobile number must be 11 digits!");
                    return;
                } else if (!mobileNumberField.getText().startsWith("09")) {
                    mobileNumberField.requestFocusInWindow();
                    setFeedback("Mobile number must start with 09 (+63)!");
                    return;
                }
            } else if (paymentType.equals("Online Bank")) {
                if (!validateFields(bankField)) {
                    bankField.requestFocusInWindow();
                    setFeedback("All fields are required!");
                    return;
                } else if (!validateFields(accountNumberField)) {
                    accountNumberField.requestFocusInWindow();
                    setFeedback("All fields are required!");
                    return;
                } else if (!validateFields(accountNameField)) {
                    accountNameField.requestFocusInWindow();
                    setFeedback("All fields are required!");
                    return;
                }
            }
            
            SalesTransaction sale = new SalesTransaction(
                customer.getCustomerId(), 
                null, 
                paymentType,  
                paymentType.equals(PaymentMethod.EWallet.name()) ? eWalletField.getSelectedItem().toString() : null, 
                paymentType.equals("Online Bank") ? bankField.getText() : null, 
                paymentType.equals(PaymentMethod.EWallet.name()) ? mobileNumberField.getText() : null, 
                paymentType.equals("Online Bank") ? accountNumberField.getText() : null, 
                paymentType.equals("Online Bank") ? accountNameField.getText() : null,
                null);
            
            try  {
                SalesTransactionDao.addSalesTransaction(sale);
                SalesTransaction addedSale = SalesTransactionDao.getLatestSalesTransaction();

                for (Map.Entry<SaleItemEntry, Integer> entry: owner.getOwner().getCart().entrySet()) {
                    SaleItem saleItem = (SaleItem) entry.getKey().getPojo();
                    int quantity = entry.getValue();

                    TransactionItem transactionItem = new TransactionItem(addedSale.getTransactionId(), saleItem.getSaleItemId(), quantity);

                    TransactionItemDao.addTransactionItem(transactionItem);
                }

                delivery.setTransactionId(addedSale.getTransactionId());

                DeliveryDao.addDelivery(delivery);

                PopupDialog notif = new PopupDialog("Transaction Complete");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Transaction has been completed!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();
                owner.getOwner().getOwner().getOwner().removeActionView();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                PopupDialog error = new PopupDialog("Unable to Complete Transaction");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Unable to complete transaction\n" + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
                return;
            }
        }), "grow, gapright 5%, gapbottom 5%");
    }
}
