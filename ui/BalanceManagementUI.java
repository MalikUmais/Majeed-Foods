package ui;

import dao.BalanceDAO;
import dao.StatsDAO;
import models.Balance;

import javax.swing.*;
import java.awt.*;

public class BalanceManagementUI extends JFrame {

    private JLabel lblCurrentBalance;
    private JTextField txtSaleAmount;
    private JTextField txtPurchaseAmount;

    private BalanceDAO balanceDAO;
    private StatsDAO statsDAO;

    public BalanceManagementUI() {

        balanceDAO = new BalanceDAO();
        statsDAO = new StatsDAO();

        setTitle("Balance Management - Majeed Foods");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- TITLE ----------
        JLabel lblTitle = new JLabel("Balance Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // ---------- BALANCE DISPLAY ----------
        JPanel balancePanel = new JPanel();
        lblCurrentBalance = new JLabel();
        lblCurrentBalance.setFont(new Font("Arial", Font.BOLD, 20));
        balancePanel.add(lblCurrentBalance);

        add(balancePanel, BorderLayout.CENTER);

        // ---------- FORM ----------
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        formPanel.add(new JLabel("Sale Amount:"));
        txtSaleAmount = new JTextField();
        formPanel.add(txtSaleAmount);

        formPanel.add(new JLabel("Purchase Amount:"));
        txtPurchaseAmount = new JTextField();
        formPanel.add(txtPurchaseAmount);

        // Buttons
        JButton btnAddSale = new JButton("Add Sale");
        JButton btnPurchase = new JButton("Deduct Purchase");
        JButton btnLowAlert = new JButton("Check Low Balance");

        formPanel.add(btnAddSale);
        formPanel.add(btnPurchase);
JPanel southPanel = new JPanel(new BorderLayout());
southPanel.add(formPanel, BorderLayout.NORTH);
// southPanel.add(btnPanel, BorderLayout.SOUTH);

add(southPanel, BorderLayout.SOUTH);
        // add(formPanel, BorderLayout.SOUTH);

        // Extra Button
        JPanel extraPanel = new JPanel();
        extraPanel.add(btnLowAlert);
        add(extraPanel, BorderLayout.AFTER_LAST_LINE);

        // ---------- BUTTON ACTIONS ----------
        btnAddSale.addActionListener(e -> addSale());
        btnPurchase.addActionListener(e -> deductPurchase());
        btnLowAlert.addActionListener(e -> checkLowBalance());

        // Load initial balance
        loadBalance();

        setVisible(true);
    }

    // ---------- LOAD CURRENT BALANCE ----------
    private void loadBalance() {
        Balance b = balanceDAO.getBalance();
        if (b != null) {
            lblCurrentBalance.setText("Current Balance: PKR " + b.getAmount());
        } else {
            lblCurrentBalance.setText("Balance not initialized!");
        }
    }

    // ---------- ADD SALE ----------
    private void addSale() {
        try {
            double amount = Double.parseDouble(txtSaleAmount.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Sale amount must be positive!");
                return;
            }

            if (balanceDAO.addBalanceOnSale(amount)) {
                // Orders increment already done in OrderDAO, here is just manual sale
                JOptionPane.showMessageDialog(this, "Sale added! Balance updated.");
                loadBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update balance!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid sale amount!");
        }
    }

    // ---------- DEDUCT PURCHASE ----------
    private void deductPurchase() {
        try {
            double amount = Double.parseDouble(txtPurchaseAmount.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Purchase amount must be positive!");
                return;
            }

            if (balanceDAO.deductBalanceOnPurchase(amount)) {

                // Track stock purchases
                statsDAO.incrementStockPurchased();

                JOptionPane.showMessageDialog(this, "Purchase deducted! Balance updated.");
                loadBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient balance!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid purchase amount!");
        }
    }

    // ---------- CHECK LOW BALANCE ----------
    private void checkLowBalance() {
        String thresholdStr = JOptionPane.showInputDialog(this, "Enter threshold amount:");

        if (thresholdStr == null) return;

        try {
            double threshold = Double.parseDouble(thresholdStr);

            if (balanceDAO.isLowBalance(threshold)) {
                JOptionPane.showMessageDialog(this, "Warning! Balance is LOW.");
            } else {
                JOptionPane.showMessageDialog(this, "Balance is above threshold.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid threshold value!");
        }
    }
}
