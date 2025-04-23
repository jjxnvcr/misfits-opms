package app.db.pojo.column;

public enum SaleItemColumn implements Column {
    SaleItemID,
    ItemID,
    ItemQuantity,
    MeasurementSystem,
    NumericSize,
    AlphaSize;

    @Override
    public String getName() {
        return name();
    }
}
