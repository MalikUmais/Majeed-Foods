package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Manager;

public class ManagerController {

    private Connection conn;

    public ManagerController() {
        conn = DBConnection.getConnection();
    }

    // AUTH: Login manager
    public Manager loginManager(String name, String password) {
        String sql = "SELECT * FROM manager WHERE name = ? AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Manager m = new Manager();
                m.setManagerId(rs.getInt("manager_id"));
                m.setName(rs.getString("name"));
                m.setPassword(rs.getString("password"));
                m.setContactNumber(rs.getString("contact_number"));
                return m; // LOGIN SUCCESS
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // LOGIN FAILED
    }

    // AUTH: check if manager exists
    public boolean managerExists(String name) {
        String sql = "SELECT * FROM manager WHERE name = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // CREATE
    public boolean addManager(Manager m) {
        if (managerExists(m.getName())) {
            System.out.println("Manager already exists!");
            return false;
        }

        String sql = "INSERT INTO manager (name, password, contact_number) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getPassword());
            ps.setString(3, m.getContactNumber());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get single manager
    public Manager getManager(int id) {
        String sql = "SELECT * FROM manager WHERE manager_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Manager m = new Manager();
                m.setManagerId(rs.getInt("manager_id"));
                m.setName(rs.getString("name"));
                m.setPassword(rs.getString("password"));
                m.setContactNumber(rs.getString("contact_number"));
                return m;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get all managers
    public List<Manager> getAllManagers() {
        List<Manager> list = new ArrayList<>();
        String sql = "SELECT * FROM manager ORDER BY manager_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Manager m = new Manager();
                m.setManagerId(rs.getInt("manager_id"));
                m.setName(rs.getString("name"));
                m.setPassword(rs.getString("password"));
                m.setContactNumber(rs.getString("contact_number"));
                list.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Update manager details
    public boolean updateManager(Manager m) {
        String sql = "UPDATE manager SET name = ?, password = ?, contact_number = ? WHERE manager_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getPassword());
            ps.setString(3, m.getContactNumber());
            ps.setInt(4, m.getManagerId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update password only
    public boolean updatePassword(int managerId, String newPassword) {
        String sql = "UPDATE manager SET password = ? WHERE manager_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, managerId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteManager(int id) {
        String sql = "DELETE FROM manager WHERE manager_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
