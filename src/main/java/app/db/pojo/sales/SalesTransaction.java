package app.db.pojo.sales;

import java.sql.Timestamp;

public class SalesTransaction {
    private int transactionId;
    private int customerId;
    private Timestamp transactionDate;
    private String paymentType;
    private String eWalletType;
    private String bankName;
    private String mobileNumber;
    private String accountNumber;
    private String accountName;
    private String referenceNumber;

    public SalesTransaction(int customerId, Timestamp transactionDate, String paymentType, String eWalletType, String bankName, String mobileNumber, String accountNumber, String accountName, String referenceNumber) {
        this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.paymentType = paymentType;
        this.eWalletType = eWalletType;
        this.bankName = bankName;
        this.mobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.referenceNumber = referenceNumber;
    }

    public SalesTransaction(int transactionId, int customerId, Timestamp transactionDate, String paymentType, String eWalletType, String bankName, String mobileNumber, String accountNumber, String accountName, String referenceNumber) {
        this(customerId, transactionDate, paymentType, eWalletType, bankName, mobileNumber, accountNumber, accountName, referenceNumber);
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String geteWalletType() {
        return eWalletType;
    }

    public void seteWalletType(String eWalletType) {
        this.eWalletType = eWalletType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String toString() {
        return "SalesTransaction{" +
                "transactionId=" + transactionId +
                ", customerId=" + customerId +
                ", transactionDate=" + transactionDate +
                ", paymentType='" + paymentType + '\'' +
                ", eWalletType='" + eWalletType + '\'' +
                ", bankName='" + bankName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}
