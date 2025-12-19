package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Order;

public class OrderDAO {

    private Connection conn;
    private BalanceDAO balanceDAO;
    private StatsDAO statsDAO;

    public OrderDAO() {
        conn = DBConnection.getConnection();
        balanceDAO = new BalanceDAO();
        statsDAO = new StatsDAO();
    }

    // CREATE (Add new order)
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (order_name, order_price) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getOrderName());
            ps.setDouble(2, order.getOrderPrice());

            int rows = ps.executeUpdate();
            if (rows > 0) {

                // Auto-update Stats
                statsDAO.incrementOrderCompleted();

                // Auto-update Balance
                balanceDAO.addBalanceOnSale(order.getOrderPrice());

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // READ (single order)
    public Order getOrder(int id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderName(rs.getString("order_name"));
                order.setOrderPrice(rs.getDouble("order_price"));
                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ ALL
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_id DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setOrderName(rs.getString("order_name"));
                o.setOrderPrice(rs.getDouble("order_price"));
                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET order_name = ?, order_price = ? WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getOrderName());
            ps.setDouble(2, order.getOrderPrice());
            ps.setInt(3, order.getOrderId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
