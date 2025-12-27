package ui;

import models.Balance;
import models.Stats;

import javax.swing.*;

import controller.BalanceController;
import controller.StatsController;

import java.awt.*;

public class ReportsUI extends JFrame {

    private JTextArea txtReportArea;

    private StatsController statsDAO;
    private BalanceController balanceDAO;

    public ReportsUI() {

        statsDAO = new StatsController();
        balanceDAO = new BalanceController();

        setTitle("Reports - Majeed Foods");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // title
        JLabel lblTitle = new JLabel("Financial & Activity Reports", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // report area
        txtReportArea = new JTextArea();
        txtReportArea.setEditable(false);
        txtReportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(txtReportArea);
        add(scrollPane, BorderLayout.CENTER);

        // btn panel
        JPanel btnPanel = new JPanel();

        JButton btnGenerate = new JButton("Generate Report");
        JButton btnRefresh = new JButton("Refresh Stats");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnGenerate);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClear);

        add(btnPanel, BorderLayout.SOUTH);

        //btn actions
        btnGenerate.addActionListener(e -> generateReport());
        btnRefresh.addActionListener(e -> loadStats());
        btnClear.addActionListener(e -> txtReportArea.setText(""));

        loadStats();

        setVisible(true);
    }

    // load basic stats
    private void loadStats() {
        txtReportArea.setText("");

        Stats stats = statsDAO.getStats();
        Balance balance = balanceDAO.getBalance();

        if (stats == null || balance == null) {
            txtReportArea.setText("Stats or balance not initialized.");
            return;
        }

        txtReportArea.append("----- Basic System Stats -----\n");
        txtReportArea.append("Stocks Purchased: " + stats.getStocksPurchased() + "\n");
        txtReportArea.append("Current Balance:  PKR " + balance.getAmount() + "\n");
        txtReportArea.append("--------------------------------\n\n");
    }

    //generate detailed report
    private void generateReport() {
        String report = statsDAO.generateProfitLossReport();
        txtReportArea.append(report + "\n\n");
    }
}
