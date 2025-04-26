package app.db.pojo.sales;

public class TransactionItem {
    private int transactionItemId;
    private int transactionId;
    private int saleItemId;
    private int quantity;

    public TransactionItem(int transactionId, int saleItemId, int quantity) {
        this.transactionId = transactionId;
        this.saleItemId = saleItemId;
        this.quantity = quantity;
    }

    public TransactionItem(int transactionItemId, int transactionId, int saleItemId, int quantity) {
        this(transactionId, saleItemId, quantity);
        this.transactionItemId = transactionItemId;
    }
    
    public int getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(int transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TransactionItem{" +
                "transactionItemId=" + transactionItemId +
                ", transactionId=" + transactionId +
                ", saleItemId=" + saleItemId +
                ", quantity=" + quantity +
                '}';
    }
}
