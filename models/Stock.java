package models;
public class Stock {
    private int itemId;
    private String itemName;
    private int itemQuantity;
    private double itemPrice;
    private boolean mostUsed;


    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getItemQuantity() { return itemQuantity; }
    public void setItemQuantity(int itemQuantity) { this.itemQuantity = itemQuantity; }

    public double getItemPrice() { return itemPrice; }
    public void setItemPrice(double itemPrice) { this.itemPrice = itemPrice; }

    public boolean isMostUsed() {
    return mostUsed;
}

public void setMostUsed(boolean mostUsed) {
    this.mostUsed = mostUsed;
}
}
