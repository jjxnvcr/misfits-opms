package app.db.pojo.column;

public enum CategoryColumn implements Column {
    CategoryID,
    CategoryName;

    @Override
    public String getName() {
        return name();
    }
}
