package dao;

import java.sql.*;

import models.Balance;

public class BalanceDAO {

    private Connection conn;

    public BalanceDAO() {
        conn = DBConnection.getConnection();
    }

    // Create - Only needed once to initialize balance row
    public boolean addInitialBalance(double amount) {
        String sql = "INSERT INTO balance (amount) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - Get current balance
    public Balance getBalance() {
        String sql = "SELECT TOP 1 * FROM balance ORDER BY balance_id DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Balance b = new Balance();
                b.setBalanceId(rs.getInt("balance_id"));
                b.setAmount(rs.getDouble("amount"));
                return b;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE - Set new balance manually
    public boolean updateBalance(double newAmount) {
        Balance current = getBalance();
        if (current == null) return false;

        String sql = "UPDATE balance SET amount = ? WHERE balance_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newAmount);
            ps.setInt(2, current.getBalanceId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // EXTRA - Add to balance (on sale)
    public boolean addBalanceOnSale(double saleAmount) {
        Balance b = getBalance();
        if (b == null) return false;

        double updated = b.getAmount() + saleAmount;

        return updateBalance(updated);
    }

    // EXTRA - Deduct from balance (on stock purchase)
    public boolean deductBalanceOnPurchase(double purchaseAmount) {
        Balance b = getBalance();
        if (b == null) return false;

        double updated = b.getAmount() - purchaseAmount;

        if (updated < 0) {
            System.out.println("WARNING: Balance cannot go negative!");
            return false;
        }

        return updateBalance(updated);
    }

    // EXTRA - Low balance alert
    public boolean isLowBalance(double threshold) {
        Balance b = getBalance();
        if (b == null) return true;

        return b.getAmount() <= threshold;
    }

    // EXTRA - Generate report (simple placeholder)
    public String getBalanceReport(String period) {
        // You will later replace this with StatsDAO calculations
        switch (period.toLowerCase()) {
            case "daily":
                return "Daily Report: Sales + Purchases Summary Unavailable (StatsDAO needed)";
            case "weekly":
                return "Weekly Report: Coming Soon";
            case "monthly":
                return "Monthly Report: Coming Soon";
            default:
                return "Invalid period. Use daily | weekly | monthly.";
        }
    }
}

