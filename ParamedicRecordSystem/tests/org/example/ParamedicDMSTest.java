package org.example;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test suite for {@link ParamedicDMS} and {@link PatientDatabase}.
 * Uses an actual test database for integration-style testing.
 *
 * Verifies:
 * <ul>
 *     <li>Record insertion</li>
 *     <li>Field updates</li>
 *     <li>Query behavior</li>
 *     <li>Delete operations</li>
 *     <li>Condition-based reporting</li>
 * </ul>
 *
 * @author Brent
 * @since 2025-10
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParamedicDMSTest {

    private static PatientDatabase database;

    @BeforeAll
    static void setupDatabase() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3301/paramedic_db";
        String user = "root";
        String password = "Password"; // Change this!

        database = new PatientDatabase(dbUrl, user, password);
    }

    @BeforeEach
    void clearTableBeforeTest() throws SQLException {
        database.clearAll();
    }

    @Test
    @Order(1)
    void testAddAndFindPatient() throws SQLException {
        database.addPatient("John", "Doe", "555-1234", "Stable", true, LocalDateTime.now());
        List<Patient> patients = database.viewAllPatients();

        assertFalse(patients.isEmpty(), "Patients list should not be empty");
        assertEquals("John", patients.get(0).getFirstName());
    }

    @Test
    @Order(2)
    void testUpdatePatientField() throws SQLException {
        database.addPatient("Alice", "Smith", "555-6789", "Injured", true, LocalDateTime.now());
        List<Patient> patients = database.viewAllPatients();
        int id = patients.get(0).getId();

        database.updatePatientField(id, "medical_condition", "Recovered");

        List<Patient> updated = database.viewAllPatients();
        assertEquals("Recovered", updated.get(0).getMedical_condition());
    }

    @Test
    @Order(3)
    void testViewAllPatients() throws SQLException {
        database.addPatient("Bob", "Lee", "555-1111", "Critical", true, LocalDateTime.now());
        database.addPatient("Sally", "Brown", "555-2222", "Stable", false, LocalDateTime.now());

        List<Patient> patients = database.viewAllPatients();
        assertEquals(2, patients.size());
    }

    @Test
    @Order(4)
    void testDeletePatient() throws SQLException {
        database.addPatient("Mark", "Jones", "555-9999", "Stable", true, LocalDateTime.now());
        List<Patient> patients = database.viewAllPatients();
        int id = patients.get(0).getId();

        database.removePatient(id);

        List<Patient> afterDelete = database.viewAllPatients();
        assertTrue(afterDelete.isEmpty(), "Database should be empty after delete");
    }

    @Test
    @Order(5)
    void testReportByCondition() throws SQLException {
        database.addPatient("Amy", "White", "555-0001", "Critical", true, LocalDateTime.now());
        database.addPatient("Chris", "Black", "555-0002", "Stable", true, LocalDateTime.now());

        List<Patient> criticalPatients = database.reportByCondition("Critical");
        assertEquals(1, criticalPatients.size());
        assertEquals("Amy", criticalPatients.get(0).getFirstName());
    }
}
