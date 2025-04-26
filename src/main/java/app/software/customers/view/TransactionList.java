package app.software.customers.view;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.formdev.flatlaf.extras.components.FlatLabel;

import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.components.ScrollView;
import app.components.SortButton;
import app.db.dao.sales.SalesTransactionDao;
import app.db.pojo.column.SalesTransactionColumn;
import app.db.pojo.customer.Customer;
import app.db.pojo.sales.SalesTransaction;
import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class TransactionList extends Page {
    private ScrollView view = new ScrollView(1);
    private FlatLabel transactionCountLabel;
    private Customer customer;
    private boolean sort = false;

    public TransactionList(Customer customer) {
        super(new MigLayout("fillx", "[][]"), false);

        this.customer = customer;
        
        SortButton dateSort = new SortButton(SalesTransactionColumn.TransactionDate, "Date");
        dateSort.setIcon(new Iconify("desc", dateSort.getForeground()).derive(dateSort.getFont().getSize(), dateSort.getFont().getSize()));
        dateSort.setAction(() -> {
            dateSort.setIcon(new Iconify(!getSort() ? "asc" : "desc", dateSort.getForeground()).derive(dateSort.getFont().getSize(), dateSort.getFont().getSize()));
            setSort(!getSort());
            loadTransactions();
        });

        FlatLabel transactionLabel = new FlatLabel();
        transactionLabel.setText("Transactions");
        transactionLabel.setForeground(Palette.SUBTEXT0.color());

        add(transactionLabel);
        add(dateSort, "align 100%, pushx, wrap");

        transactionCountLabel = new FlatLabel();
        transactionCountLabel.setForeground(Palette.TEXT.color());
        
        add(transactionCountLabel, "align 50%, span, wrap");
        add(new ScrollList(view), "height 100%, span, grow, wrap");

        loadTransactions();
    }

    private void loadTransactions() {
        view.removeAll();

        List<SalesTransaction> transactions = new ArrayList<>();
        
        try {
            transactions = SalesTransactionDao.getSalesTransactionsByCustomerIdOrderBy(customer.getCustomerId(), SalesTransactionColumn.TransactionDate + " " + (sort ? Sort.ASC.getName() : Sort.DESC.getName()));
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to Load Transactions");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load transactions for this customer\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
        }

        transactionCountLabel.setText(transactions.size() == 0 ? "No transactions found." : transactions.size() + " transaction" + (transactions.size() == 1 ? "" : "s"));
        transactionCountLabel.revalidate();

        if (transactions.size() > 0) {
            Timestamp currDate = transactions.get(0).getTransactionDate();

            for (SalesTransaction transaction: transactions) {
                FlatLabel dateLabel = new FlatLabel();
                dateLabel.setForeground(Palette.SUBTEXT0.color());
                dateLabel.setText(new SimpleDateFormat("MMM dd, yyyy").format(new Date(currDate.getTime())));

                if (currDate != transaction.getTransactionDate()) {
                    dateLabel.setText(new SimpleDateFormat("MMM dd, yyyy").format(new Date(transaction.getTransactionDate().getTime())));
                }

                currDate = transaction.getTransactionDate();

                view.add(dateLabel, "align 0%, gapbottom 5");
                view.add(new TransactionEntry(transaction), "growx");
            }

            view.revalidate();
            view.repaint();
        }
    }

    public boolean getSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }
}
