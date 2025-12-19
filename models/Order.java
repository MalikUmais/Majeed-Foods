package models;
public class Order {
    private int orderId;
    private String orderName;
    private double orderPrice;

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getOrderName() { return orderName; }
    public void setOrderName(String orderName) { this.orderName = orderName; }

    public double getOrderPrice() { return orderPrice; }
    public void setOrderPrice(double orderPrice) { this.orderPrice = orderPrice; }
}
