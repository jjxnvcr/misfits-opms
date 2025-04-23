package app.db.pojo.column;

public enum SupplierColumn implements Column {
    SupplierID,
    SupplierName,
    ContactNumber;

    @Override
    public String getName() {
        return name();
    }
}
