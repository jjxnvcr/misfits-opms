package app.db.pojo.column;

public enum SupplyOrderColumn implements Column {
    OrderID,
    SupplierID,
    OrderDate,
    OrderStatus;

    @Override
    public String getName() {
        return name();
    }
}
