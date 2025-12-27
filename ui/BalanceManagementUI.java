package ui;

import models.Balance;

import javax.swing.*;

import controller.BalanceController;
import controller.StatsController;

import java.awt.*;

public class BalanceManagementUI extends JFrame {

    private JLabel lblCurrentBalance;
    private JTextField txtSaleAmount;
    private JTextField txtPurchaseAmount;

    private BalanceController balanceDAO;
    private StatsController statsDAO;

    public BalanceManagementUI() {

        balanceDAO = new BalanceController();
        statsDAO = new StatsController();

        setTitle("Balance Management - Majeed Foods");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // title pannel
        JLabel lblTitle = new JLabel("Balance Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // display balance pannel
        JPanel balancePanel = new JPanel();
        lblCurrentBalance = new JLabel();
        lblCurrentBalance.setFont(new Font("Arial", Font.BOLD, 20));
        balancePanel.add(lblCurrentBalance);
        add(balancePanel, BorderLayout.CENTER);

        // input pannel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        formPanel.add(new JLabel("Sale Amount:"));
        txtSaleAmount = new JTextField();
        formPanel.add(txtSaleAmount);

        formPanel.add(new JLabel("Purchase Amount:"));
        txtPurchaseAmount = new JTextField();
        formPanel.add(txtPurchaseAmount);

        add(formPanel, BorderLayout.NORTH);

        // btn pannel
        JButton btnAddSale = new JButton("Add Sale");
        JButton btnPurchase = new JButton("Deduct Purchase");
        JButton btnLowAlert = new JButton("Check Low Balance");
        JButton btnInit = new JButton("Initialize Balance");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        buttonPanel.add(btnAddSale);
        buttonPanel.add(btnPurchase);
        buttonPanel.add(btnLowAlert);
        buttonPanel.add(btnInit);

        add(buttonPanel, BorderLayout.SOUTH);

        // btn event handlers
        btnAddSale.addActionListener(e -> addSale());
        btnPurchase.addActionListener(e -> deductPurchase());
        btnLowAlert.addActionListener(e -> checkLowBalance());
        btnInit.addActionListener(e -> initBalance());

        // Load initial balance
        loadBalance();

        setVisible(true);
    }

    // load balance
    private void loadBalance() {
        Balance b = balanceDAO.getBalance();
        if (b != null) {
            lblCurrentBalance.setText("Current Balance: PKR " + b.getAmount());
        } else {
            lblCurrentBalance.setText("Balance not initialized!");
        }
    }

    // add sale
    private void addSale() {
        try {
            double amount = Double.parseDouble(txtSaleAmount.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Sale amount must be positive!");
                return;
            }

            if (balanceDAO.addBalanceOnSale(amount)) {
                JOptionPane.showMessageDialog(this, "Sale added! Balance updated.");
                loadBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update balance!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid sale amount!");
        }
    }

    // deduct purchase
    private void deductPurchase() {
        try {
            double amount = Double.parseDouble(txtPurchaseAmount.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Purchase amount must be positive!");
                return;
            }

            if (balanceDAO.deductBalanceOnPurchase(amount)) {
                JOptionPane.showMessageDialog(this, "Purchase deducted! Balance updated.");
                loadBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient balance!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid purchase amount!");
        }
    }

    // low balance checking
    private void checkLowBalance() {
        String thresholdStr = JOptionPane.showInputDialog(this, "Enter threshold amount:");

        if (thresholdStr == null)
            return;

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

    // balance initialization
    private void initBalance() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter starting balance:");

        try {
            double amount = Double.parseDouble(amountStr);
            if (balanceDAO.addInitialBalance(amount)) {
                JOptionPane.showMessageDialog(this, "Balance initialized!");
                loadBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Balance initialization failed!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
    }
}
