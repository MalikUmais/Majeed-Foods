package dao;

import java.sql.*;

import models.Stats;

public class StatsDAO {

    private Connection conn;

    public StatsDAO() {
        conn = DBConnection.getConnection();
    }

   
    public boolean createInitialStats() {
        String sql = "INSERT INTO stats (stocks_purchased) VALUES (0)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

   public Stats getStats() {
    String sql = "SELECT TOP 1 * FROM stats ORDER BY report_id DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            Stats s = new Stats();
            s.setReportId(rs.getInt("report_id"));
            s.setStocksPurchased(rs.getInt("stocks_purchased"));
            return s;
        } else {
            createInitialStats();
            return getStats();
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}


   
    
    public boolean incrementStockPurchased(int qty) {
        Stats current = getStats();
        if (current == null) {
            createInitialStats();
            current = getStats();
        }

        String sql = "UPDATE stats SET stocks_purchased = stocks_purchased + ? WHERE report_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, current.getReportId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   

    public boolean resetStats() {
        String sql = "UPDATE stats SET stocks_purchased = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public String generateProfitLossReport() {

        Stats stats = getStats();
        BalanceDAO balanceDAO = new BalanceDAO();

        // Current balance
        double balance = balanceDAO.getBalance().getAmount();



        double purchaseValueEstimate = stats.getStocksPurchased() * 300; // assume avg purchase = 300

        double profit = balance - purchaseValueEstimate;

        return  "----- Profit & Loss Report -----\n"
                + "Stocks Purchased: " + stats.getStocksPurchased() + "\n"
                + "Estimated Purchase Total: " + purchaseValueEstimate + "\n"
                + "--------------------------------\n"
                + "Profit / Loss: " + profit + "\n"
                + "Current Balance: " + balance + "\n"
                + "--------------------------------";
    }
}

