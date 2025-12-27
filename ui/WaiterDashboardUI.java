package ui;

import models.MenuItem;
import models.Shift;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.MenuController;
import controller.ShiftController;

import java.awt.*;
import java.util.List;

public class WaiterDashboardUI extends JFrame {

    private JTable menuTable;
    private DefaultTableModel menuModel;
    private JLabel lblWaiterName;
    private JLabel lblShiftTiming;
    private JLabel lblShiftDays;

    private int waiterId;
    private MenuController menuDAO;
    private ShiftController shiftDAO;

    public WaiterDashboardUI(int waiterId, String waiterName) {

        this.waiterId = waiterId;
        this.menuDAO = new MenuController();
        this.shiftDAO = new ShiftController();

        setTitle("Waiter Dashboard - Majeed Foods");
        setSize(800, 550);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Top panel: waiter info
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblWaiterName = new JLabel("Waiter: " + waiterName);
        lblWaiterName.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(lblWaiterName);

        lblShiftTiming = new JLabel("Shift Time: Loading...");
        lblShiftDays = new JLabel("Shift Days: Loading...");

        topPanel.add(lblShiftTiming);
        topPanel.add(lblShiftDays);

        add(topPanel, BorderLayout.NORTH);

        // Menu table
        menuModel = new DefaultTableModel(new String[] { "Item Name", "Price" }, 0);
        menuTable = new JTable(menuModel);

        JScrollPane tableScroll = new JScrollPane(menuTable);
        add(tableScroll, BorderLayout.CENTER);

        // Bottom panel logout
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);

        loadMenu();
        loadShift();

        setVisible(true);
    }

    // Load menu items
    private void loadMenu() {
        menuModel.setRowCount(0);

        List<MenuItem> items = menuDAO.getAllMenuItems();
        for (MenuItem m : items) {
            menuModel.addRow(new Object[] { m.getItemName(), m.getItemPrice() });
        }
    }

    // Load assigned shift info
    private void loadShift() {

        Shift s = shiftDAO.getShiftById(waiterId);

        if (s != null) {
            lblShiftTiming.setText("Shift Time: " + s.getShiftTiming());
            lblShiftDays.setText("Shift Days: " + s.getShiftDays());
        } else {
            lblShiftTiming.setText("Shift Time: Not Assigned");
            lblShiftDays.setText("Shift Days: None");
        }
    }
}
