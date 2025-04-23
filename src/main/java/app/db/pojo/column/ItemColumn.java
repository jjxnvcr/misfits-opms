package app.db.pojo.column;

public enum ItemColumn implements Column {
    ItemID,
    CategoryID,
    ItemName,
    ItemDescription,
    ItemColor;

    @Override
    public String getName() {
        return name();
    }
}
