package app.db.pojo.production;

public class Supplier {
    private int supplierId;
    private String supplierName;
    private String contactNumber;

    public Supplier(String supplierName, String contactNumber) {
        this.supplierName = supplierName;
        this.contactNumber = contactNumber;
    }

    public Supplier(int supplierId, String supplierName, String contactNumber) {
        this(supplierName, contactNumber);
        this.supplierId = supplierId;
    }
    
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
