package app.db.pojo.column;

/**
 * An enum for the Customer table columns. Implements the Column interface
 */
public enum CustomerColumn implements Column {
    CustomerID,
    FirstName,
    LastName,
    ContactNumber,
    Street,
    Barangay,
    City,
    Province;

    @Override
    public String getName() {
        return name();
    }
}
