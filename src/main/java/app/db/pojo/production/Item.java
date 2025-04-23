package app.db.pojo.production;

public class Item {
    private int itemId;
    private int categoryId;
    private String itemName;
    private String itemDescription;
    private String itemColor;

    public Item(int categoryId, String itemName, String itemDescription, String itemColor) {
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemColor = itemColor;
    }

    public Item(int itemId, int categoryId, String itemName, String itemDescription, String itemColor) {
        this(categoryId, itemName, itemDescription, itemColor);
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", categoryId=" + categoryId +
                ", itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemColor='" + itemColor + '\'' +
                '}';
    }
}
