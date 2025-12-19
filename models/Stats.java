package models;
public class Stats {
    private int reportId;
    private int orderCompleted;
    private int stocksPurchased;

    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }

    public int getOrderCompleted() { return orderCompleted; }
    public void setOrderCompleted(int orderCompleted) { this.orderCompleted = orderCompleted; }

    public int getStocksPurchased() { return stocksPurchased; }
    public void setStocksPurchased(int stocksPurchased) { this.stocksPurchased = stocksPurchased; }
}
