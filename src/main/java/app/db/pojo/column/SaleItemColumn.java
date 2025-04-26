package app.db.pojo.column;

public enum SaleItemColumn implements Column {
    SaleItemID,
    ItemID,
    ItemPrice,
    ItemQuantity,
    MeasurementSystem,
    NumericSize,
    AlphaSize;

    @Override
    public String getName() {
        return name();
    }
}
