package ui;

import dao.StockDAO;
import models.Stock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockManagementUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtItemName;
    private JTextField txtQuantity;
    private JTextField txtPrice;

    private StockDAO stockDAO;

    public StockManagementUI() {

        stockDAO = new StockDAO();

        setTitle("Stock Management - Majeed Foods");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- TITLE ----------
        JLabel lblTitle = new JLabel("Stock Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new String[]{
                "Item ID", "Item Name", "Quantity", "Price"
        }, 0);

        table = new JTable(model);
        loadStocks();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- FORM PANEL ----------
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("Item Name:"));
        txtItemName = new JTextField();
        formPanel.add(txtItemName);

        formPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        formPanel.add(txtQuantity);

        formPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        // ---------- BUTTON PANEL ----------
        JPanel btnPanel = new JPanel();

        JButton btnAdd = new JButton("Add Stock");
        JButton btnUpdate = new JButton("Update Stock");
        JButton btnDelete = new JButton("Delete Stock");
        JButton btnLowStock = new JButton("Low Stock Alert");
        JButton btnMostUsed = new JButton("Most Used Item");
        JButton btnMarkMostUsed = new JButton("Mark Most Used");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnLowStock);
        btnPanel.add(btnMostUsed);
        btnPanel.add(btnMarkMostUsed);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // ---------- BUTTON ACTIONS ----------
        btnAdd.addActionListener(e -> addStock());
        btnUpdate.addActionListener(e -> updateStock());
        btnDelete.addActionListener(e -> deleteStock());
        btnLowStock.addActionListener(e -> showLowStock());
        btnMostUsed.addActionListener(e -> showMostUsedItem());
        btnMarkMostUsed.addActionListener(e -> markMostUsed());

        setVisible(true);
    }

    // ---------- LOAD DATA ----------
    private void loadStocks() {
        model.setRowCount(0);
        List<Stock> stocks = stockDAO.getAllStocks();

        for (Stock s : stocks) {
            model.addRow(new Object[]{
                    s.getItemId(),
                    s.getItemName(),
                    s.getItemQuantity(),
                    s.getItemPrice()
            });
        }
    }

    // ---------- ADD STOCK ----------
    private void addStock() {
        try {
            String name = txtItemName.getText();
            int qty = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Item name is required!");
                return;
            }

            Stock s = new Stock();
            s.setItemName(name);
            s.setItemQuantity(qty);
            s.setItemPrice(price);

            if (stockDAO.addStock(s)) {
                JOptionPane.showMessageDialog(this, "Stock added!");
                loadStocks();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Cannot add stock!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input values!");
        }
    }

    // ---------- UPDATE STOCK ----------
    private void updateStock() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select stock to update!");
            return;
        }

        try {
            int id = (int) table.getValueAt(row, 0);
            String name = txtItemName.getText();
            int qty = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            Stock s = new Stock();
            s.setItemId(id);
            s.setItemName(name);
            s.setItemQuantity(qty);
            s.setItemPrice(price);

            if (stockDAO.updateStock(s)) {
                JOptionPane.showMessageDialog(this, "Stock updated!");
                loadStocks();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid values!");
        }
    }

    // ---------- DELETE STOCK ----------
    private void deleteStock() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select stock to delete!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this stock?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (stockDAO.deleteStock(id)) {
                JOptionPane.showMessageDialog(this, "Stock deleted!");
                loadStocks();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed!");
            }
        }
    }

    // ---------- SHOW LOW STOCK ----------
    private void showLowStock() {
        String thresholdStr = JOptionPane.showInputDialog(this, "Enter low-stock threshold:");
        if (thresholdStr == null) return;

        try {
            int threshold = Integer.parseInt(thresholdStr);

            List<Stock> low = stockDAO.getLowStock(threshold);
            if (low.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No low-stock items.");
                return;
            }

            StringBuilder sb = new StringBuilder("Low Stock Items:\n\n");
            for (Stock s : low) {
                sb.append(s.getItemName()).append(" — ").append(s.getItemQuantity()).append(" left\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid number!");
        }
    }

    // ---------- MOST USED ITEM DISPLAY ----------
    private void showMostUsedItem() {
        JOptionPane.showMessageDialog(this,
                "Most Used Item:\n" + stockDAO.getMostUsedItemName(),
                "Most Used Item", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- MARK MOST USED ----------
    private void markMostUsed() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a stock item first!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        if (stockDAO.setMostUsed(id)) {
            JOptionPane.showMessageDialog(this, "Marked as most used!");
            loadStocks();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update!");
        }
    }
}
