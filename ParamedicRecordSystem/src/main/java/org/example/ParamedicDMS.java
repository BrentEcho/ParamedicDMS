package org.example;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Main controller class for the Paramedic Database Management System (DMS).
 * Acts as a service layer between the GUI and the database layer. Provides
 * CRUD operations and reporting features for paramedic patient records.
 *
 * <p>This class coordinates logic and delegates all persistence operations
 * to {@link PatientDatabase}. It is designed to keep patient files organized,
 * searchable, and exportable.</p>
 *
 * @author Brent
 * @version 1.0
 * @since 2025-10-13
 */
public class ParamedicDMS {

    private final PatientDatabase database;
    /**
     * Creates a new instance with database connection credentials.
     *
     * @param url      JDBC database URL
     * @param username Database username
     * @param password Database password
     */
    public ParamedicDMS(String url, String username, String password) {
        this.database = new PatientDatabase(url, username, password);
    }

    /**
     * Adds a new patient to the database.
     *
     * @param firstName        Patient first name
     * @param lastName         Patient last name
     * @param contact          Contact info (phone/email/etc.)
     * @param medicalCondition Primary diagnosis or treatment condition
     * @param active           Whether the record is active in system
     * @throws SQLException when database operation fails
     */
    public void addPatient(String firstName, String lastName, String contact,
                           String medicalCondition, boolean active) throws SQLException {
        database.addPatient(firstName, lastName, contact, medicalCondition, active);
    }

    /**
     * Retrieves all stored patients.
     *
     * @return list of all patient records
     * @throws SQLException if query fails
     */
    public List<Patient> viewAllPatients() throws SQLException {
        return database.viewAllPatients();
    }

    /**
     * Retrieves a specific patient record by ID.
     *
     * @param id Numeric database ID
     * @return matching patient or null if not found
     * @throws SQLException if query fails
     */
    public Patient findById(int id) throws SQLException {
        return database.findById(id);
    }

    /**
     * Updates a specific database field for an existing patient.
     *
     * @param id    patient ID
     * @param field database field name
     * @param value new value
     * @throws SQLException if update fails
     */
    public void updatePatient(int id, String field, Object value) throws SQLException {
        database.updatePatientField(id, field, value);
    }

    /**
     * Deletes an existing patient by ID.
     *
     * @param id primary key
     * @throws SQLException when deletion fails
     */
    public void deletePatient(int id) throws SQLException {
        database.deletePatient(id);
    }

    /**
     * Returns a filtered list of patients matching a given condition.
     *
     * @param condition medical condition keyword or text match
     * @return matching list of Patient objects
     * @throws SQLException if query fails
     */

    public List<Patient> reportByCondition(String condition) throws SQLException {
        return database.reportByCondition(condition);
    }

    /**
     * Clears all patient records from the database.
     *
     * <strong>Warning:</strong> This operation is irreversible.
     *
     * @throws SQLException if operation fails
     */

    public void clearAll() throws SQLException {
        database.clearAll();
    }

    // Example: Insert sample patients
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
