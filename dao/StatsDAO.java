package dao;

import java.sql.*;

import models.Stats;

public class StatsDAO {

    private Connection conn;

    public StatsDAO() {
        conn = DBConnection.getConnection();
    }

    // CREATE new stats row (if not exists)
    public boolean createInitialStats() {
        String sql = "INSERT INTO stats (order_completed, stocks_purchased) VALUES (0, 0)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // If stats already exist, ignore
            return false;
        }
    }

    // READ - Fetch latest stats
    public Stats getStats() {
        String sql = "SELECT TOP 1 * FROM stats ORDER BY report_id DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Stats s = new Stats();
                s.setReportId(rs.getInt("report_id"));
                s.setOrderCompleted(rs.getInt("order_completed"));
                s.setStocksPurchased(rs.getInt("stocks_purchased"));
                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE - increment orders
    public boolean incrementOrderCompleted() {
        Stats current = getStats();
        if (current == null) {
            createInitialStats();
            current = getStats();
        }

        String sql = "UPDATE stats SET order_completed = order_completed + 1 WHERE report_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, current.getReportId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE - increment stocks purchased
    public boolean incrementStockPurchased() {
        Stats current = getStats();
        if (current == null) {
            createInitialStats();
            current = getStats();
        }

        String sql = "UPDATE stats SET stocks_purchased = stocks_purchased + 1 WHERE report_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, current.getReportId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // RESET stats (e.g., monthly reset)
    public boolean resetStats() {
        String sql = "UPDATE stats SET order_completed = 0, stocks_purchased = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // REPORT GENERATION - Profit & Loss (simple logic integrating with BalanceDAO)
    public String generateProfitLossReport() {

        Stats stats = getStats();
        BalanceDAO balanceDAO = new BalanceDAO();

        // Current balance
        double balance = balanceDAO.getBalance().getAmount();

        // Fake multipliers for demonstration (adjust as needed)
        double saleValueEstimate = stats.getOrderCompleted() * 500; // assume avg order = 500
        double purchaseValueEstimate = stats.getStocksPurchased() * 300; // assume avg purchase = 300

        double profit = saleValueEstimate - purchaseValueEstimate;

        return  "----- Profit & Loss Report -----\n"
                + "Orders Completed: " + stats.getOrderCompleted() + "\n"
                + "Stocks Purchased: " + stats.getStocksPurchased() + "\n"
                + "Estimated Sales Total: " + saleValueEstimate + "\n"
                + "Estimated Purchase Total: " + purchaseValueEstimate + "\n"
                + "--------------------------------\n"
                + "Profit / Loss: " + profit + "\n"
                + "Current Balance: " + balance + "\n"
                + "--------------------------------";
    }
}

