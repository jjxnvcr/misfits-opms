package app.db.pojo.sales;

public class TransactionItem {
    private int transactionItemId;
    private int transactionId;
    private int itemId;
    private int quantity;

    public TransactionItem(int transactionId, int itemId, int quantity) {
        this.transactionId = transactionId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public TransactionItem(int transactionItemId, int transactionId, int itemId, int quantity) {
        this(transactionId, itemId, quantity);
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                '}';
    }
}
