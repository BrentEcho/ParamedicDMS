package org.example;

import java.util.*;
import java.io.File;

/**
 * Brent Echols, CEN-3024C, 10/13/2025
 * ParamedicDMS
 * Main class runs and pulls all other classes together.
 * The overall objective is to keep a record of patients that paramedics take care of on a day to day basis.
 * Compiles all the data into a readable list and generates reports.
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
            8. Load Patient Data from File
            Choose an option (1-8):
            """;

    public static void main(String[] args) {
        ParamedicDMS app = new ParamedicDMS();
        System.out.println(app.run());
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
                case "8" -> System.out.println(handleLoadFromFile());
                default -> System.out.println("Invalid option. Enter 1-8.");
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

    // View all patients
    private void viewAllPrint() {
        List<String> lines = database.viewAllPatients();
        if (lines.isEmpty()) {
            System.out.println("No patient records found.");
        } else {
            lines.forEach(System.out::println);
        }
    }

    // Update patient record
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

    // Remove patient record
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
        if (report.isEmpty()) {
            System.out.println("No patients found with condition: " + condition);
        } else {
            report.forEach(System.out::println);
        }
    }

    // Report by date range
    private void reportByDateRangePrint() {
        System.out.print("From date (MM-DD-YYYY): ");
        String from = scanner.nextLine().trim();
        System.out.print("To date (MM-DD-YYYY): ");
        String to = scanner.nextLine().trim();
        List<String> report = database.reportByDateRange(from, to);
        if (report.isEmpty()) {
            System.out.println("No patients found in the specified date range.");
        } else {
            report.forEach(System.out::println);
        }
    }

    // Manual file load
    private String handleLoadFromFile() {
        System.out.print("Enter the file name to load (e.g., patients_sample.txt): ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            return "File name cannot be empty.";
        }

        File file = new File(fileName);
        if (!file.exists()) {
            return "Error: File not found.";
        }

        List<Patient> loaded = loader.load(fileName);
        if (loaded.isEmpty()) {
            return "No valid patient data found in file.";
        }

        for (Patient p : loaded) {
            database.addPatientFromFile(p);
        }

        database.setNextID(database.highestId() + 1);
        return "Successfully loaded " + loaded.size() + " patients from " + fileName + ".";
    }
}
