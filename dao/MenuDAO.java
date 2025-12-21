package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.MenuItem;

public class MenuDAO {

    private Connection conn;

    public MenuDAO() {
        conn = DBConnection.getConnection();
    }

    // CREATE
    public boolean addMenuItem(MenuItem item) {
        if (itemExists(item.getItemName())) {
            return false;  
        }

        String sql = "INSERT INTO menu (item_name, item_price) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemName());
            ps.setDouble(2, item.getItemPrice());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (single)
    public MenuItem getMenuItem(int id) {
        String sql = "SELECT * FROM menu WHERE item_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MenuItem item = new MenuItem();
                item.setItemId(rs.getInt("item_id"));
                item.setItemName(rs.getString("item_name"));
                item.setItemPrice(rs.getDouble("item_price"));
                return item;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ ALL
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY item_name";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setItemId(rs.getInt("item_id"));
                item.setItemName(rs.getString("item_name"));
                item.setItemPrice(rs.getDouble("item_price"));
                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public boolean updateMenuItem(MenuItem item) {
        String sql = "UPDATE menu SET item_name = ?, item_price = ? WHERE item_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemName());
            ps.setDouble(2, item.getItemPrice());
            ps.setInt(3, item.getItemId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean removeMenuItem(int id) {
        String sql = "DELETE FROM menu WHERE item_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // check duplicate
    public boolean itemExists(String name) {
        String sql = "SELECT item_id FROM menu WHERE item_name = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if exists

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

