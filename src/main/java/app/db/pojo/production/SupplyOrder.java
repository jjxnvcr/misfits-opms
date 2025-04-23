package app.db.pojo.production;

import java.sql.Timestamp;

public class SupplyOrder {
    private int orderId;
    private int supplierId;
    private Timestamp orderDate;
    private String orderStatus;

    public SupplyOrder(int supplierId, Timestamp orderDate, String orderStatus) {
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public SupplyOrder(int orderId, int supplierId, Timestamp orderDate, String orderStatus) {
        this(supplierId, orderDate, orderStatus);
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "SupplyOrder{" +
                "orderId=" + orderId +
                ", supplierId=" + supplierId +
                ", orderDate=" + orderDate +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
