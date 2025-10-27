package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
/**
 * Brent Echols, CEN-3024C, 10/27/2025
 * GUI for the Paramedic Database Management System.
 * Allows user to add, remove, update, view, report, import, and export patient records.
 */
public class ParamedicGUI extends JFrame {
    private final PatientDatabase database = new PatientDatabase();
    private final JTextArea displayArea = new JTextArea(15, 50);

    public ParamedicGUI() {
        // Set window title and default close operation
        setTitle("Paramedic DMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create buttons for all available actions
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton addButton = new JButton("Add Patient");
        JButton removeButton = new JButton("Remove Patient");
        JButton updateButton = new JButton("Update Patient");
        JButton viewButton = new JButton("View All Patients");
        JButton reportButton = new JButton("Report by Condition");
        JButton importButton = new JButton("Import Patients");
        JButton exportButton = new JButton("Export Patients");
        JButton exitButton = new JButton("Exit");

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(exitButton);

        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        // Add Patient Button Action
        addButton.addActionListener(e -> {
            String first = JOptionPane.showInputDialog("First Name:");
            String last = JOptionPane.showInputDialog("Last Name:");
            String dob = JOptionPane.showInputDialog("DOB (MM-DD-YYYY):");
            String contact = JOptionPane.showInputDialog("Contact:");
            String condition = JOptionPane.showInputDialog("Condition:");
            String[] statusOptions = {"true", "false"};
            String status = (String) JOptionPane.showInputDialog(this, "Active Status:", "Select", JOptionPane.PLAIN_MESSAGE, null, statusOptions, statusOptions[0]);
            String result = database.addPatient(first, last, dob, contact, condition, Boolean.parseBoolean(status));
            displayArea.setText(result);
        });

        // Remove Patient Button Action
        removeButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Patient ID to remove:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    int id = Integer.parseInt(idStr);
                    displayArea.setText(database.removePatient(id));
                } catch (NumberFormatException ex) {
                    displayArea.setText("Invalid ID format.");
                }
            }
        });

        // Update Patient Button Action
        updateButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Patient ID to update:");
            if (idStr == null || idStr.isBlank()) return;
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.");
                return;
            }

            if (database.findById(id).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Patient ID not found.");
                return;
            }

            String[] fields = {"firstname", "lastname", "dob", "contact", "condition", "status"};
            String field = (String) JOptionPane.showInputDialog(this, "Select field to update:", "Update", JOptionPane.PLAIN_MESSAGE, null, fields, fields[0]);
            if (field == null) return;

            String newValue;
            if (field.equals("status")) {
                String[] options = {"true", "false"};
                newValue = (String) JOptionPane.showInputDialog(this, "Select new status:", "Status", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            } else {
                newValue = JOptionPane.showInputDialog("Enter new value for " + field + ":");
            }

            if (newValue != null && !newValue.isBlank()) {
                displayArea.setText(database.updatePatientField(id, field, newValue));
            }
        });

        // View All Patients Button Action
        viewButton.addActionListener(e -> {
            List<String> records = database.viewAllPatients();
            displayArea.setText(String.join("\n", records));
        });

        // Report by Condition Button Action
        reportButton.addActionListener(e -> {
            String condition = JOptionPane.showInputDialog("Enter condition to report:");
            if (condition != null && !condition.isBlank()) {
                List<String> report = database.reportByCondition(condition);
                displayArea.setText(String.join("\n", report));
            }
        });

        // Import Patients Button Action
        importButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String result = database.loadPatientsFromFile(file.getAbsolutePath());
                displayArea.setText(result);
            }
        });

        // Export Patients Button Action
        exportButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String result = database.exportToFile(file.getAbsolutePath());
                displayArea.setText(result);
            }
        });

        // Exit Button Action
        exitButton.addActionListener(e -> System.exit(0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Entry point for the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParamedicGUI::new);
    }
}
