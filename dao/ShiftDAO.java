package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Shift;
import models.Waiter;

public class ShiftDAO {

    private Connection conn;

    public ShiftDAO() {
        conn = DBConnection.getConnection();
    }

    // Add a new shift (without waiter)
    public boolean addShift(Shift shift) {
        String sql = "INSERT INTO shift (shift_name, shift_timing, shift_days) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, shift.getShiftName());
            ps.setString(2, shift.getShiftTiming());
            ps.setString(3, shift.getShiftDays());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get shift by ID
    public Shift getShiftById(int id) {
        String sql = "SELECT * FROM shift WHERE shift_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Shift s = new Shift();
                s.setShiftId(rs.getInt("shift_id"));
                s.setShiftName(rs.getString("shift_name"));
                s.setShiftTiming(rs.getString("shift_timing"));
                s.setShiftDays(rs.getString("shift_days"));
                s.setWaiterId(rs.getInt("waiter_id"));
                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all shifts
    public List<Shift> getAllShifts() {
        List<Shift> list = new ArrayList<>();
        String sql = "SELECT * FROM shift ORDER BY shift_name";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Shift s = new Shift();
                s.setShiftId(rs.getInt("shift_id"));
                s.setShiftName(rs.getString("shift_name"));
                s.setShiftTiming(rs.getString("shift_timing"));
                s.setShiftDays(rs.getString("shift_days"));
                s.setWaiterId(rs.getInt("waiter_id"));
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // REMOVE waiter from shift
    public boolean removeWaiterFromShift(int shiftId) {
        String sql = "UPDATE shift SET waiter_id = NULL WHERE shift_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE shift
    public boolean deleteShift(int shiftId) {
        String sql = "DELETE FROM shift WHERE shift_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get users who are not assigned to ANY shift
    public List<Waiter> getUnassignedWaiters() {
        List<Waiter> list = new ArrayList<>();

        String sql = "SELECT * FROM waiter WHERE waiter_id NOT IN (SELECT waiter_id FROM shift WHERE waiter_id IS NOT NULL)";

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

    // Check if waiter is already assigned to any shift
    public boolean isWaiterAssigned(int waiterId) {
        String sql = "SELECT * FROM shift WHERE waiter_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, waiterId);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = waiter is assigned

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Shift getShiftByWaiter(int waiterId) {
        String sql = "SELECT * FROM shift WHERE waiter_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, waiterId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Shift s = new Shift();
                s.setShiftId(rs.getInt("shift_id"));
                s.setShiftName(rs.getString("shift_name"));
                s.setShiftTiming(rs.getString("shift_timing"));
                s.setShiftDays(rs.getString("shift_days"));
                s.setWaiterId(rs.getInt("waiter_id"));
                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Waiter> getWaitersByShift(int shiftId) {
        List<Waiter> list = new ArrayList<>();
        String sql = "SELECT w.waiter_id, w.name, w.contact_number " +
                "FROM shift_waiter_map swm " +
                "JOIN waiter w ON swm.waiter_id = w.waiter_id " +
                "WHERE swm.shift_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
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

    public boolean assignWaiterToShift(int shiftId, int waiterId) {

        String sql = "INSERT INTO shift_waiter_map (shift_id, waiter_id) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
            ps.setInt(2, waiterId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            return false; // Already assigned
        }
    }

    public boolean removeWaiterFromShift(int shiftId, int waiterId) {
        String sql = "DELETE FROM shift_waiter_map WHERE shift_id = ? AND waiter_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
            ps.setInt(2, waiterId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
