package ui;

import dao.ManagerDAO;
import dao.WaiterDAO;
import models.Manager;
import models.Waiter;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginUI() {
        setTitle("Manager Login - Majeed Foods System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---------- Title Panel ----------
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("Manager Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(lblTitle);

        // ---------- Form Panel ----------
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        // ---------- Button Panel ----------
        JPanel buttonPanel = new JPanel();
        btnLogin = new JButton("Login");
        JButton btnExit = new JButton("Exit");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        // Add panels to frame
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ---------- Button Listeners ----------

        // LOGIN BUTTON
        btnLogin.addActionListener(e -> handleLogin());

        // EXIT BUTTON
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // ---------- LOGIN LOGIC ----------
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = String.valueOf(txtPassword.getPassword());

        ManagerDAO managerDAO = new ManagerDAO();
        WaiterDAO waiterDAO=new WaiterDAO();
        Manager manager = managerDAO.loginManager(username, password);

        if (manager != null) {
    new DashboardUI(manager);
    dispose();
    return;
}

// 2️⃣ Waiter check
Waiter waiter = waiterDAO.loginWaiter(username, password);

if (waiter != null) {
    new WaiterDashboardUI(waiter.getWaiterId(), waiter.getName());

    dispose();
    return;
} else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- MAIN METHOD FOR TESTING ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
