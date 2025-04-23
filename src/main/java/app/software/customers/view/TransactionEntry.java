package app.software.customers.view;

import java.awt.Font;
import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.ListEntry;
import app.db.dao.sales.DeliveryDao;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.sales.SalesTransaction;

import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class TransactionEntry extends ListEntry {
    private boolean clicked = false;

    public TransactionEntry(SalesTransaction transaction) {
        super(null, transaction);
        setLayout(new MigLayout("fillx"));

        FlatLabel id = new FlatLabel();
        id.setText("Tsc." + transaction.getTransactionId());
        id.setForeground(Palette.SURFACE1.color());
        id.setFont(id.getFont().deriveFont(Font.BOLD));
        
        double total;
        try {
            total = SalesTransactionDao.getTransactionTotal(transaction.getTransactionId());
        } catch (SQLException e) {
            total = 0;
        }

        FlatLabel amount = new FlatLabel();

        amount.setText(String.format("₱ %,.2f", total));
        amount.setForeground(Palette.GREEN.color());
        amount.setFont(amount.getFont().deriveFont(Font.BOLD));

        setAction(() -> {
            if (clicked) {
                clicked = false;
                
                removeAll();
                add(id);
                add(amount, "align 100%, pushx, wrap");
            } else {
                clicked = true;

                FlatLabel paymentLabel = new FlatLabel();
                paymentLabel.setForeground(Palette.SUBTEXT0.color());
                
                String paymentType = transaction.getPaymentType().equals("Cash") ? transaction.getPaymentType() : transaction.getPaymentType().equals("Online Bank") ? transaction.getPaymentType() + " - " + transaction.getBankName() : transaction.getPaymentType() + " - " + transaction.geteWalletType();

                paymentLabel.setText("Paid with " + paymentType);

                add(paymentLabel, "growx, wrap");

                if (transaction.getPaymentType().equals("Online Bank")) {
                    FlatLabel accountNo = new FlatLabel();
                    accountNo.setForeground(Palette.SUBTEXT1.color());
                    accountNo.setText("Account No.: " + transaction.getAccountNumber());

                    FlatLabel accountName = new FlatLabel();
                    accountName.setForeground(Palette.SUBTEXT1.color());
                    accountName.setText("Account Name: " + transaction.getAccountName());
                    
                    add(accountNo, "growx, wrap");
                    add(accountName, "growx, wrap");
                } else if (transaction.getPaymentType().equals("EWallet")) {
                    FlatLabel mobileNo = new FlatLabel();
                    mobileNo.setForeground(Palette.SUBTEXT1.color());
                    mobileNo.setText("Mobile No.: " + transaction.getMobileNumber());

                    add(mobileNo, "growx, wrap");
                }

                if (!transaction.getPaymentType().equals("Cash")) {
                    FlatLabel refLabel = new FlatLabel();
                    refLabel.setForeground(Palette.SURFACE2.color());
                    refLabel.setText(transaction.getReferenceNumber());

                    add(refLabel, "growx, gaptop 5, wrap");
                }

                FlatLabel statusLabel = new FlatLabel();
                statusLabel.setForeground(Palette.SURFACE2.color());

                try {
                    statusLabel.setText(DeliveryDao.getDeliveryByTransactionId(transaction.getTransactionId()).getDeliveryStatus());
                } catch (Exception e) {
                    e.printStackTrace();
                    statusLabel.setText("Unknown");
                }

                add(statusLabel, "growx, gapbottom 5, wrap");
            }

            revalidate();
            repaint();
        });

        add(id);
        add(amount, "align 100%, pushx, wrap");
    }
}
