package ui;

import javax.swing.*;

import models.Manager;

import java.awt.*;

public class DashboardUI extends JFrame {

    private Manager loggedManager;

    public DashboardUI(Manager manager) {
        this.loggedManager = manager;

        setTitle("Dashboard - Majeed Foods System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---------- TOP PANEL: Welcome Message ----------
        JPanel topPanel = new JPanel();
        JLabel lblWelcome = new JLabel("Welcome, " + manager.getName());
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(lblWelcome);

        // ---------- CENTER PANEL: Buttons ----------
        JPanel centerPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton btnMenu = new JButton("Menu Management");
        JButton btnStock = new JButton("Stock Management");
        JButton btnShift = new JButton("Shift Management");
        JButton btnBalance = new JButton("Balance Management");
        JButton btnReports = new JButton("Reports");
        JButton btnLogout = new JButton("Logout");

        btnMenu.setFont(new Font("Arial", Font.PLAIN, 16));
        btnStock.setFont(new Font("Arial", Font.PLAIN, 16));
        btnShift.setFont(new Font("Arial", Font.PLAIN, 16));
        btnBalance.setFont(new Font("Arial", Font.PLAIN, 16));
        btnReports.setFont(new Font("Arial", Font.PLAIN, 16));
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 16));

        centerPanel.add(btnMenu);
        centerPanel.add(btnStock);
        centerPanel.add(btnShift);
        centerPanel.add(btnBalance);
        centerPanel.add(btnReports);
        centerPanel.add(btnLogout);

        // ---------- ADD PANELS ----------
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);

        // ---------- BUTTON EVENTS ----------
        
        btnMenu.addActionListener(e -> {
            new MenuManagementUI();
        });

        btnStock.addActionListener(e -> {
            new StockManagementUI();
        });

        btnShift.addActionListener(e -> {
            new ShiftManagementUI();
        });

        btnBalance.addActionListener(e -> {
            new BalanceManagementUI();
        });

        btnReports.addActionListener(e -> {
            new ReportsUI();
        });

        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            new LoginUI();
            dispose();
        });
    }
}
