package org.example;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ParamedicDMS {

    private final PatientDatabase database;

    public ParamedicDMS(String url, String username, String password) {
        this.database = new PatientDatabase(url, username, password);
    }

    // ✅ Add a new patient
    public void addPatient(String firstName, String lastName, String contact,
                           String medicalCondition, boolean active) throws SQLException {
        database.addPatient(firstName, lastName, contact, medicalCondition, active);
    }

    // ✅ View all patients
    public List<Patient> viewAllPatients() throws SQLException {
        return database.viewAllPatients();
    }

    // ✅ Find patient by ID
    public Patient findById(int id) throws SQLException {
        return database.findById(id);
    }

    // ✅ Update any patient field
    public void updatePatient(int id, String field, Object value) throws SQLException {
        database.updatePatientField(id, field, value);
    }

    // ✅ Delete patient by ID
    public void deletePatient(int id) throws SQLException {
        database.deletePatient(id);
    }

    // ✅ Report by condition
    public List<Patient> reportByCondition(String condition) throws SQLException {
        return database.reportByCondition(condition);
    }

    // ✅ Utility for testing (clear database)
    public void clearAll() throws SQLException {
        database.clearAll();
    }

    // ✅ Example: Insert sample patients (optional utility)
    public void seedSampleData() throws SQLException {
        addPatient("John", "Doe", "555-1234", "Fracture", true);
        addPatient("Jane", "Smith", "555-5678", "Asthma", true);
        addPatient("Michael", "Brown", "555-9876", "Burn", false);
        addPatient("Sarah", "Johnson", "555-6543", "Allergy", true);
        addPatient("David", "Williams", "555-4321", "Dehydration", false);
        addPatient("Emily", "Miller", "555-2468", "Heart Disease", true);
        addPatient("Chris", "Garcia", "555-1357", "Broken Arm", true);
        addPatient("Laura", "Martinez", "555-8642", "Stroke", false);
        addPatient("Daniel", "Rodriguez", "555-9753", "Migraine", true);
        addPatient("Sophia", "Davis", "555-3698", "Anxiety", true);
        addPatient("Matthew", "Lopez", "555-8524", "Diabetes", true);
        addPatient("Olivia", "Gonzalez", "555-1597", "Cold", false);
        addPatient("Anthony", "Wilson", "555-7531", "Infection", true);
        addPatient("Isabella", "Anderson", "555-9517", "Fracture", false);
        addPatient("Ethan", "Thomas", "555-2589", "Asthma", true);
        addPatient("Ava", "Taylor", "555-4567", "Burn", false);
        addPatient("Liam", "Moore", "555-7890", "Dehydration", true);
        addPatient("Charlotte", "Jackson", "555-6548", "Heart Disease", true);
        addPatient("James", "White", "555-3214", "Migraine", true);
        addPatient("Amelia", "Harris", "555-1478", "Anxiety", true);
    }
}
