package ui;

import dao.MenuDAO;
import models.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuManagementUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtName;
    private JTextField txtPrice;

    private MenuDAO menuDAO;

    public MenuManagementUI() {
        menuDAO = new MenuDAO();

        setTitle("Menu Management - Majeed Foods");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- TITLE ----------
        JLabel lblTitle = new JLabel("Menu Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitle, BorderLayout.NORTH);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new String[]{"ID", "Name", "Price"}, 0);
        table = new JTable(model);
        loadMenuItems();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- FORM PANEL ----------
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Item Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Item Price:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        add(formPanel, BorderLayout.SOUTH);

        // ---------- BUTTON PANEL ----------
        JPanel btnPanel = new JPanel();

        JButton btnAdd = new JButton("Add Item");
        JButton btnUpdate = new JButton("Update Item");
        JButton btnDelete = new JButton("Delete Item");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
JPanel southPanel = new JPanel(new BorderLayout());
southPanel.add(formPanel, BorderLayout.NORTH);
southPanel.add(btnPanel, BorderLayout.SOUTH);

// ADD TO FRAME
add(southPanel, BorderLayout.SOUTH);
        // add(btnPanel, BorderLayout.SOUTH);
        // add(btnPanel, BorderLayout.SOUTH);

        // ---------- BUTTON ACTIONS ----------

        // Add Menu Item
        btnAdd.addActionListener(e -> addMenuItem());

        // Update Menu Item
        btnUpdate.addActionListener(e -> updateMenuItem());

        // Delete Menu Item
        btnDelete.addActionListener(e -> deleteMenuItem());

        setVisible(true);
    }

    // ---------- LOAD DATA INTO TABLE ----------
    private void loadMenuItems() {
        model.setRowCount(0);  // clear table

        List<MenuItem> items = menuDAO.getAllMenuItems();
        for (MenuItem item : items) {
            model.addRow(new Object[]{
                    item.getItemId(),
                    item.getItemName(),
                    item.getItemPrice()
            });
        }
    }

    // ---------- ADD ITEM ----------
    private void addMenuItem() {
        String name = txtName.getText();
        String priceText = txtPrice.getText();

        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);

            MenuItem item = new MenuItem();
            item.setItemName(name);
            item.setItemPrice(price);

            boolean success = menuDAO.addMenuItem(item);

            if (success) {
                JOptionPane.showMessageDialog(this, "Item added successfully!");
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this, "Item already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be numeric!");
        }
    }

    // ---------- UPDATE ITEM ----------
    private void updateMenuItem() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to update!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        String name = txtName.getText();
        String priceText = txtPrice.getText();

        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);

            MenuItem item = new MenuItem();
            item.setItemId(id);
            item.setItemName(name);
            item.setItemPrice(price);

            if (menuDAO.updateMenuItem(item)) {
                JOptionPane.showMessageDialog(this, "Item updated!");
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid price!");
        }
    }

    // ---------- DELETE ITEM ----------
    private void deleteMenuItem() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to delete!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (menuDAO.removeMenuItem(id)) {
                JOptionPane.showMessageDialog(this, "Item deleted!");
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed!");
            }
        }
    }
}
