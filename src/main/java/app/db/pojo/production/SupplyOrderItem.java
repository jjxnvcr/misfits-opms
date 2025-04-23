package app.db.pojo.production;

public class SupplyOrderItem {
    private int orderItemId;
    private int orderId;
    private int itemId;
    private double orderItemCost;
    private int quantity;

    public SupplyOrderItem(int orderId, int itemId, double orderItemCost, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.orderItemCost = orderItemCost;
        this.quantity = quantity;
    }

    public SupplyOrderItem(int orderItemId, int orderId, int itemId, double orderItemCost, int quantity) {
        this(orderId, itemId, orderItemCost, quantity);
        this.orderItemId = orderItemId;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getOrderItemCost() {
        return orderItemCost;
    }

    public void setOrderItemCost(double orderItemCost) {
        this.orderItemCost = orderItemCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "SupplyOrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", orderItemCost=" + orderItemCost +
                ", quantity=" + quantity +
                '}';
    }
}
