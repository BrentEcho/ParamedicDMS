package org.example;

import java.util.*;

/**
 */


public class ParamedicDMS {
    private final PatientDatabase database = new PatientDatabase();
    private final PatientFileLoader loader = new PatientFileLoader();
    private final PatientFileSaver saver = new PatientFileSaver();
    private final Scanner scanner = new Scanner(System.in);
    private final String dataFile = "patients_sample.txt";

    private static final String MENU = """
            \n=== Patient Record Management System ===
            1. Add Patient
            2. View All Patients
            3. Update Patient
            4. Remove Patient
            5. Generate Report by Condition
            6. Generate Report by Admission Date Range
            7. Save & Exit
            Choose an option (1-7):
            """;

    public static void main(String[] args) {
        ParamedicDMS app = new ParamedicDMS();
        System.out.println(app.startupLoad());
        System.out.println(app.run()); // run returns exit message
    }

    // Loads file on startup
    public String startupLoad() {
        List<Patient> loaded = loader.load(dataFile);
        for (Patient p : loaded) database.addPatientFromFile(p);
        if (!loaded.isEmpty()) {
            database.setNextID(database.highestId() + 1);
        }
        return "Loaded " + loaded.size() + " patients from file (if file present).";
    }

    // Main interactive loop returns exit message
    public String run() {
        while (true) {
            System.out.print(MENU);
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> System.out.println(handleAdd());
                case "2" -> viewAllPrint();
                case "3" -> System.out.println(handleUpdate());
                case "4" -> System.out.println(handleRemove());
                case "5" -> reportByConditionPrint();
                case "6" -> reportByDateRangePrint();
                case "7" -> {
                    String saveResult = saver.save(dataFile, database.getAllRecords());
                    return saveResult + "\n👋 Exiting PRMS.";
                }
                default -> System.out.println("Invalid option. Enter 1-7.");
            }
        }
    }

    // Add patient flow
    private String handleAdd() {
        System.out.print("First name: ");
        String first = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String last = scanner.nextLine().trim();
        System.out.print("DOB (MM-DD-YYYY): ");
        String dob = scanner.nextLine().trim();
        System.out.print("Emergency contact: ");
        String contact = scanner.nextLine().trim();
        System.out.print("Medical condition: ");
        String condition = scanner.nextLine().trim();
        System.out.print("Active? (yes/no): ");
        String statusStr = scanner.nextLine().trim();
        boolean status = statusStr.equalsIgnoreCase("yes") || statusStr.equalsIgnoreCase("true");
        return database.addPatient(first, last, dob, contact, condition, status);
    }

    // View all
    private void viewAllPrint() {
        List<String> lines = database.viewAllPatients();
        lines.forEach(System.out::println);
    }

    // Update flow
    private String handleUpdate() {
        try {
            System.out.print("Enter Patient ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Field to update (firstname, lastname, dob, contact, condition, status): ");
            String field = scanner.nextLine().trim();
            System.out.print("New value: ");
            String newValue = scanner.nextLine().trim();
            return database.updatePatientField(id, field, newValue);
        } catch (NumberFormatException e) {
            return "Invalid ID format.";
        }
    }

    // Remove flow
    private String handleRemove() {
        try {
            System.out.print("Enter Patient ID to remove: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            return database.removePatient(id);
        } catch (NumberFormatException e) {
            return "Invalid ID format.";
        }
    }

    // Report by condition
    private void reportByConditionPrint() {
        System.out.print("Enter medical condition to filter: ");
        String condition = scanner.nextLine().trim();
        List<String> report = database.reportByCondition(condition);
        report.forEach(System.out::println);
    }

    // Report by date range
    private void reportByDateRangePrint() {
        System.out.print("From date (MM-DD-YYYY): ");
        String from = scanner.nextLine().trim();
        System.out.print("To date (MM-DD-YYYY): ");
        String to = scanner.nextLine().trim();
        List<String> report = database.reportByDateRange(from, to);
        report.forEach(System.out::println);
    }
}
