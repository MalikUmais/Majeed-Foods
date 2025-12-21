package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Stock;

public class StockDAO {

    private Connection conn;

    public StockDAO() {
        conn = DBConnection.getConnection();
    }

    // CREATE
    public boolean addStock(Stock st) {
        String sql = "INSERT INTO stock ( item_name, item_quantity, item_price) VALUES ( ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, st.getItemName());
            ps.setInt(2, st.getItemQuantity());
            ps.setDouble(3, st.getItemPrice());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ SINGLE
    public Stock getStock(int itemId) {
        String sql = "SELECT * FROM stock WHERE item_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Stock s = new Stock();
                s.setItemId(rs.getInt("item_id"));
                s.setItemName(rs.getString("item_name"));
                s.setItemQuantity(rs.getInt("item_quantity"));
                s.setItemPrice(rs.getDouble("item_price"));
                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ ALL
    public List<Stock> getAllStocks() {
        List<Stock> list = new ArrayList<>();
        String sql = "SELECT * FROM stock";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Stock s = new Stock();
                s.setItemId(rs.getInt("item_id"));
                s.setItemName(rs.getString("item_name"));
                s.setItemQuantity(rs.getInt("item_quantity"));
                s.setItemPrice(rs.getDouble("item_price"));
                s.setMostUsed(rs.getBoolean("most_used"));

                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public boolean updateStock(Stock st) {
        String sql = "UPDATE stock SET item_name = ?, item_quantity = ?, item_price = ? WHERE item_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, st.getItemName());
            ps.setInt(2, st.getItemQuantity());
            ps.setDouble(3, st.getItemPrice());
            ps.setInt(4, st.getItemId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteStock(int id) {
        String sql = "DELETE FROM stock WHERE item_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //  LOW STOCK ALERT
    public List<Stock> getLowStock(int threshold) {
        List<Stock> lowList = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE item_quantity <= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Stock s = new Stock();
                s.setItemId(rs.getInt("item_id"));
                s.setItemName(rs.getString("item_name"));
                s.setItemQuantity(rs.getInt("item_quantity"));
                s.setItemPrice(rs.getDouble("item_price"));
                lowList.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lowList;
    }

    // MOST USED STOCKS (dummy placeholder)
    public String getMostUsedItemName() {
        String sql = "SELECT item_name FROM stock WHERE most_used = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "No most-used item selected yet.";
    }

    public boolean setMostUsed(int id) {

        String sql1 = "UPDATE stock SET most_used = 0";
        String sql2 = "UPDATE stock SET most_used = 1 WHERE item_id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sql1);
                    PreparedStatement ps2 = conn.prepareStatement(sql2)) {

                ps1.executeUpdate();

                ps2.setInt(1, id);
                ps2.executeUpdate();

                conn.commit();
                return true;
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
            e.printStackTrace();
            return false;
        }
    }

}
