package app.db.pojo.sales;

import java.sql.Timestamp;

public class Delivery {
    private int deliveryId;
    private int transactionId;
    private String deliveryAddress;
    private Timestamp deliveryDate;
    private String deliveryStatus;
    private Double paidAmount;

    public Delivery(int transactionId, String deliveryAddress, Timestamp deliveryDate, String deliveryStatus, Double paidAmount) {
        this.transactionId = transactionId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
        this.deliveryStatus = deliveryStatus;
        this.paidAmount = paidAmount;
    }

    public Delivery(int deliveryId, int transactionId, String deliveryAddress, Timestamp deliveryDate, String deliveryStatus, Double paidAmount) {
        this(transactionId, deliveryAddress, deliveryDate, deliveryStatus, paidAmount);
        this.deliveryId = deliveryId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getTransactionId() {
        return transactionId;
    } 

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {    
        this.deliveryStatus = deliveryStatus;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }
}
