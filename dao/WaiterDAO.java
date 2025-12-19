package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Waiter;

public class WaiterDAO {

    private Connection conn;

    public WaiterDAO() {
        conn = DBConnection.getConnection();
    }

    // CREATE
    public boolean addWaiter(Waiter w) {
        String sql = "INSERT INTO waiter (name, contact_number) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, w.getName());
            ps.setString(2, w.getContactNumber());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - Get waiter by ID
    public Waiter getWaiter(int id) {
        String sql = "SELECT * FROM waiter WHERE waiter_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Waiter w = new Waiter();
                w.setWaiterId(rs.getInt("waiter_id"));
                w.setName(rs.getString("name"));
                w.setContactNumber(rs.getString("contact_number"));
                return w;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ - Get all waiters
    public List<Waiter> getAllWaiters() {
        List<Waiter> list = new ArrayList<>();
        String sql = "SELECT * FROM waiter ORDER BY name";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Waiter w = new Waiter();
                w.setWaiterId(rs.getInt("waiter_id"));
                w.setName(rs.getString("name"));
                w.setContactNumber(rs.getString("contact_number"));
                list.add(w);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE
    public boolean updateWaiter(Waiter w) {
        String sql = "UPDATE waiter SET name = ?, contact_number = ? WHERE waiter_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, w.getName());
            ps.setString(2, w.getContactNumber());
            ps.setInt(3, w.getWaiterId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteWaiter(int id) {
        String sql = "DELETE FROM waiter WHERE waiter_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getWaiterNameById(int waiterId) {
    String sql = "SELECT name FROM waiter WHERE waiter_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, waiterId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "None";
}
public Waiter loginWaiter(String name, String password) {
    String sql = "SELECT * FROM waiter WHERE name = ? AND password = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, name);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Waiter w = new Waiter();
            w.setWaiterId(rs.getInt("waiter_id"));
            w.setName(rs.getString("name"));
            w.setPassword(rs.getString("password"));
            w.setContactNumber(rs.getString("contact_number"));
            return w;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

}
