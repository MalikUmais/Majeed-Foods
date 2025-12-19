package ui;

import dao.ShiftDAO;
import dao.WaiterDAO;
import models.Shift;
import models.Waiter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShiftManagementUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtShiftName;
    private JTextField txtShiftTiming;
    private JTextField txtShiftDays;

    private ShiftDAO shiftDAO;
    private WaiterDAO waiterDAO;

    public ShiftManagementUI() {

        shiftDAO = new ShiftDAO();
        waiterDAO = new WaiterDAO();

        setTitle("Shift Management - Majeed Foods");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- TITLE ----------
        JLabel lblTitle = new JLabel("Shift Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new String[]{
                "Shift ID", "Shift Name", "Timing", "Days", "Waiters"
        }, 0);

        table = new JTable(model);
        loadShifts();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- FORM PANEL ----------
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("Shift Name:"));
        txtShiftName = new JTextField();
        formPanel.add(txtShiftName);

        formPanel.add(new JLabel("Shift Timing:"));
        txtShiftTiming = new JTextField();
        formPanel.add(txtShiftTiming);

        formPanel.add(new JLabel("Shift Days:"));
        txtShiftDays = new JTextField();
        formPanel.add(txtShiftDays);

        // ---------- BUTTON PANEL ----------
        JPanel btnPanel = new JPanel();

        JButton btnAddShift = new JButton("Add Shift");
        JButton btnAssignWaiter = new JButton("Assign Waiter(s)");
        JButton btnRemoveWaiter = new JButton("Remove Waiter");
        JButton btnDeleteShift = new JButton("Delete Shift");
        JButton btnRefresh = new JButton("Refresh Table");

        btnPanel.add(btnAddShift);
        btnPanel.add(btnAssignWaiter);
        btnPanel.add(btnRemoveWaiter);
        btnPanel.add(btnDeleteShift);
        btnPanel.add(btnRefresh);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // ---------- BUTTON ACTIONS ----------
        btnAddShift.addActionListener(e -> addShift());
        btnAssignWaiter.addActionListener(e -> assignWaiters());
        btnRemoveWaiter.addActionListener(e -> removeWaiter());
        btnDeleteShift.addActionListener(e -> deleteShift());
        btnRefresh.addActionListener(e -> loadShifts());

        setVisible(true);
    }


    // ---------- LOAD SHIFTS ----------
    private void loadShifts() {
        model.setRowCount(0);
        List<Shift> shifts = shiftDAO.getAllShifts();

        for (Shift s : shifts) {
            List<Waiter> wl = shiftDAO.getWaitersByShift(s.getShiftId());
            String names = wl.stream()
                    .map(Waiter::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("None");

            model.addRow(new Object[]{
                    s.getShiftId(),
                    s.getShiftName(),
                    s.getShiftTiming(),
                    s.getShiftDays(),
                    names
            });
        }
    }


    // ---------- ADD SHIFT ----------
    private void addShift() {
        String name = txtShiftName.getText();
        String timing = txtShiftTiming.getText();
        String days = txtShiftDays.getText();

        if (name.isEmpty() || timing.isEmpty() || days.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        Shift shift = new Shift();
        shift.setShiftName(name);
        shift.setShiftTiming(timing);
        shift.setShiftDays(days);

        if (shiftDAO.addShift(shift)) {
            JOptionPane.showMessageDialog(this, "Shift added!");
            loadShifts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add shift.");
        }
    }


    // ---------- ASSIGN MULTIPLE WAITERS ----------
    private void assignWaiters() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a shift first!");
            return;
        }

        int shiftId = (int) table.getValueAt(row, 0);

        List<Waiter> waiters = waiterDAO.getAllWaiters();
        if (waiters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No waiters available!");
            return;
        }

        String[] waiterNames = new String[waiters.size()];
        int[] waiterIds = new int[waiters.size()];

        for (int i = 0; i < waiters.size(); i++) {
            waiterNames[i] = waiters.get(i).getWaiterId() + " - " + waiters.get(i).getName();
            waiterIds[i] = waiters.get(i).getWaiterId();
        }

        JList<String> waiterList = new JList<>(waiterNames);
        waiterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        int result = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(waiterList),
                "Select Waiter(s)",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        List<String> selectedValues = waiterList.getSelectedValuesList();

        for (String s : selectedValues) {
            int id = Integer.parseInt(s.split(" - ")[0]);
            shiftDAO.assignWaiterToShift(shiftId, id);
        }

        JOptionPane.showMessageDialog(this, "Waiters assigned successfully!");
        loadShifts();
    }


    // ---------- REMOVE WAITER ----------
    private void removeWaiter() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a shift first!");
            return;
        }

        int shiftId = (int) table.getValueAt(row, 0);

        List<Waiter> assigned = shiftDAO.getWaitersByShift(shiftId);

        if (assigned.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No waiter assigned to remove!");
            return;
        }

        String[] assignedNames = new String[assigned.size()];

        for (int i = 0; i < assigned.size(); i++) {
            assignedNames[i] = assigned.get(i).getWaiterId() + " - " + assigned.get(i).getName();
        }

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Select a waiter to remove",
                "Remove Waiter",
                JOptionPane.PLAIN_MESSAGE,
                null,
                assignedNames,
                assignedNames[0]
        );

        if (selected == null) return;

        int waiterId = Integer.parseInt(selected.split(" - ")[0]);

        if (shiftDAO.removeWaiterFromShift(shiftId, waiterId)) {
            JOptionPane.showMessageDialog(this, "Waiter removed!");
            loadShifts();
        }
    }


    // ---------- DELETE SHIFT ----------
    private void deleteShift() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a shift!");
            return;
        }

        int shiftId = (int) table.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this shift?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (shiftDAO.deleteShift(shiftId)) {
                JOptionPane.showMessageDialog(this, "Shift deleted!");
                loadShifts();
            }
        }
    }
}
