package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class ParamedicGUI extends JFrame {
    private final ParamedicDMS dms;
    private final JTable patientTable;
    private final DefaultTableModel tableModel;

    public ParamedicGUI(ParamedicDMS dms) {
        this.dms = dms;

        setTitle("Paramedic Database Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLayout(new BorderLayout());

        // --- Table setup ---
        String[] columns = {"ID", "First Name", "Last Name", "Contact", "Condition", "Active", "Record Time"};
        tableModel = new DefaultTableModel(columns, 0);
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setAutoCreateRowSorter(true);
        add(new JScrollPane(patientTable), BorderLayout.CENTER);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add Patient");
        JButton updateBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton reportBtn = new JButton("Report by Condition");

        buttonPanel.add(refreshBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(reportBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Button logic ---
        refreshBtn.addActionListener(e -> loadPatients());

        addBtn.addActionListener(e -> showAddDialog());
        updateBtn.addActionListener(e -> showUpdateDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
        reportBtn.addActionListener(e -> reportByCondition());

        loadPatients();
        setVisible(true);
    }

    // ✅ Add Patient
    private void showAddDialog() {
        JTextField first = new JTextField();
        JTextField last = new JTextField();
        JTextField contact = new JTextField();
        JTextField condition = new JTextField();
        JCheckBox active = new JCheckBox("Active");

        Object[] fields = {
                "First Name:", first,
                "Last Name:", last,
                "Contact:", contact,
                "Condition:", condition,
                active
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add New Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                dms.addPatient(first.getText(), last.getText(), contact.getText(),
                        condition.getText(), active.isSelected());
                loadPatients();
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    // ✅ Update selected row
    private void showUpdateDialog() {
        int row = patientTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        // Field dropdown
        String[] displayNames = {"First Name", "Last Name", "Contact", "Condition", "Active"};
        String[] dbFields = {"first_name", "last_name", "contact", "medical_condition", "active"};
        JComboBox<String> fieldDropdown = new JComboBox<>(displayNames);

        int fieldResult = JOptionPane.showConfirmDialog(this, fieldDropdown, "Select Field to Update",
                JOptionPane.OK_CANCEL_OPTION);
        if (fieldResult != JOptionPane.OK_OPTION) return;

        int selectedIndex = fieldDropdown.getSelectedIndex();
        String dbField = dbFields[selectedIndex];

        Object newValue;
        if (dbField.equals("active")) {
            // Checkbox for active status
            JCheckBox activeBox = new JCheckBox("Active");
            int ok = JOptionPane.showConfirmDialog(this, activeBox, "Set Active Status", JOptionPane.OK_CANCEL_OPTION);
            if (ok != JOptionPane.OK_OPTION) return;
            newValue = activeBox.isSelected();
        } else {
            String val = JOptionPane.showInputDialog(this, "Enter new value:");
            if (val == null || val.trim().isEmpty()) return;
            newValue = val.trim();
        }

        try {
            dms.updatePatient(id, dbField, newValue);
            loadPatients();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ✅ Delete selected row
    private void deleteSelected() {
        int row = patientTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this patient?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dms.deletePatient(id);
                loadPatients();
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    // ✅ Report by condition
    private void reportByCondition() {
        String condition = JOptionPane.showInputDialog(this, "Enter condition to search for:");
        if (condition == null || condition.trim().isEmpty()) return;
        try {
            List<Patient> results = dms.reportByCondition(condition.trim());
            populateTable(results);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ✅ Table data handling
    private void loadPatients() {
        try {
            List<Patient> patients = dms.viewAllPatients();
            populateTable(patients);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void populateTable(List<Patient> patients) {
        tableModel.setRowCount(0);
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getContact(),
                    p.getMedical_condition(),
                    p.isActive(),
                    p.getRecordTime()
            });
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    public static void main(String[] args) {
        JTextField hostField = new JTextField("jdbc:mysql://localhost:3306/paramedic_db");
        JTextField userField = new JTextField("root");
        JPasswordField passField = new JPasswordField();

        Object[] fields = {
                "MySQL URL:", hostField,
                "Username:", userField,
                "Password:", passField
        };

        int res = JOptionPane.showConfirmDialog(null, fields, "Database Login", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                ParamedicDMS dms = new ParamedicDMS(
                        hostField.getText(),
                        userField.getText(),
                        new String(passField.getPassword())
                );
                SwingUtilities.invokeLater(() -> new ParamedicGUI(dms));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Connection failed: " + e.getMessage());
            }
        }
    }
}
