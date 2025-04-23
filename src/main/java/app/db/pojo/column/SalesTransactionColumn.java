package app.db.pojo.column;

public enum SalesTransactionColumn implements Column {
    TransactionID,
    CustomerID,
    TransactionDate,
    PaymentType,
    EWalletType,
    BankName,
    MobileNumber,
    AccountNumber,
    ReferenceNumber;

    @Override
    public String getName() {
        return name();
    }
}
