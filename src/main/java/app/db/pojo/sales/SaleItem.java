package app.db.pojo.sales;

public class SaleItem {
    private int saleItemId;
    private int itemId;
    private String categoryName;
    private String itemName;
    private String itemDesc;
    private String itemColor;
    private double itemPrice;
    private int itemQuantity;
    private String measurementSystem;
    private double numericSize;
    private String alphaSize;

    public SaleItem(int itemId, double itemPrice, int itemQuantity, String measurementSystem, double numericSize, String alphaSize) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.measurementSystem = measurementSystem;
        this.numericSize = numericSize;
        this.alphaSize = alphaSize;
    }

    public SaleItem(int saleItemId, int itemId, double itemPrice, int itemQuantity, String measurementSystem, double numericSize, String alphaSize) {
        this(itemId, itemPrice, itemQuantity, measurementSystem, numericSize, alphaSize);
        this.saleItemId = saleItemId;
    }

    public SaleItem(int itemId, String categoryName, String itemName, String itemDesc, String itemColor, double itemPrice, int itemQuantity, String measurementSystem, double numericSize, String alphaSize) {
        this(itemId, itemPrice, itemQuantity, measurementSystem, numericSize, alphaSize);
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemColor = itemColor;
    }

    public SaleItem(int saleItemId, int itemId, String categoryName, String itemName, String itemDesc, String itemColor, double itemPrice, int itemQuantity, String measurementSystem, double numericSize, String alphaSize) {
        this(itemId, categoryName, itemName, itemDesc, itemColor, itemPrice, itemQuantity, measurementSystem, numericSize, alphaSize);
        this.saleItemId = saleItemId;
    }

    public int getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getMeasurementSystem() {
        return measurementSystem;
    }

    public void setMeasurementSystem(String measurementSystem) {
        this.measurementSystem = measurementSystem;
    }

    public double getNumericSize() {
        return numericSize;
    }

    public void setNumericSize(int numericSize) {
        this.numericSize = numericSize;
    }

    public String getAlphaSize() {
        return alphaSize;
    }

    public void setAlphaSize(String alphaSize) {
        this.alphaSize = alphaSize;
    }

    @Override
    public String toString() {
        return "ItemSize{" +
                "saleItemId=" + saleItemId +
                ", itemId=" + itemId +
                ", categoryName='" + categoryName + '\'' +
                ", itemName=" + itemName +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemColor='" + itemColor + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemQuantity=" + itemQuantity +
                ", measurementSystem='" + measurementSystem + '\'' +
                ", numericSize=" + numericSize +
                ", alphaSize='" + alphaSize + '\'' +
                '}';
    }
}
