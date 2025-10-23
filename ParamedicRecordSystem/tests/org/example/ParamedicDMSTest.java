package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Brent Echols
 * Unit tests for ParamedicDMS project.
 * Verifies: file loading, adding/removing/updating patients,
 * and custom reporting functions.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParamedicDMSTest {

    private PatientDatabase database;
    private PatientFileLoader loader;
    private PatientFileSaver saver;
    private static final String TEST_FILE = "test_patients.txt";

    @BeforeEach
    void setUp() {
        database = new PatientDatabase();
        loader = new PatientFileLoader();
        saver = new PatientFileSaver();
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    // Test 1 File can be opened and patient data loaded
    @Test
    @Order(1)
    void testFileCanBeOpened() throws IOException {
        String content = """
                ID,FirstName,LastName,DOB,Contact,Condition,Admission,Status
                1,John,Doe,01-01-1990,555-1234,Flu,10-10-2025 10:00:00,true
                """;
        Files.writeString(Path.of(TEST_FILE), content);

        List<Patient> patients = loader.load(TEST_FILE);

        assertNotNull(patients, "Loader should not return null");
        assertEquals(1, patients.size(), "One patient should be loaded from file");
        assertEquals("John", patients.get(0).getFirstName(), "First name should match file data");
        assertEquals("Flu", patients.get(0).getMedicalCondition(), "Condition should match file data");
    }

    // Test 2 Patient can be added to database
    @Test
    @Order(2)
    void testAddPatient() {
        String result = database.addPatient("Jane", "Smith", "02-02-1992", "555-4321", "Asthma", true);
        assertTrue(result.contains("added"), "Add operation should confirm success");
        assertEquals(1, database.getAllRecords().size(), "Database should contain one patient after add");
    }

    // Test 3 Patient can be removed from database
    @Test
    @Order(3)
    void testRemovePatient() {
        database.addPatient("Mark", "Hill", "03-03-1985", "555-7777", "Burns", true);
        database.addPatient("Sara", "Bell", "04-04-1990", "555-8888", "Head Injury", false);

        String removeMsg = database.removePatient(1);
        assertTrue(removeMsg.toLowerCase().contains("removed"), "Removal message should confirm success");
        assertEquals(1, database.getAllRecords().size(), "Database should have one record after removal");
    }

    // Test 4 Patient fields can be updated dynamically
    @Test
    @Order(4)
    void testUpdatePatientField() {
        database.addPatient("Tom", "Rivers", "05-05-1995", "555-0000", "Broken Arm", true);

        String update1 = database.updatePatientField(1, "firstname", "Tim");
        String update2 = database.updatePatientField(1, "condition", "Cold");


        assertTrue(update1.toLowerCase().contains("updated"), "Should confirm first name update");
        assertTrue(update2.toLowerCase().contains("updated"), "Should confirm condition update");

        List<String> records = database.viewAllPatients();
        assertTrue(records.get(0).contains("Tim"), "Updated first name should appear in record");
        assertTrue(records.get(0).contains("Cold"), "Updated condition should appear in record");
    }

    // Test 5 Custom action (report by condition)
    @Test
    @Order(5)
    void testReportByCondition() {
        database.addPatient("Ava", "Jones", "06-06-1998", "555-9999", "Fracture", true);
        database.addPatient("Eli", "Gray", "07-07-1991", "555-2222", "Asthma", false);
        database.addPatient("Zoe", "Stone", "08-08-1989", "555-3333", "Fracture", true);

        List<String> report = database.reportByCondition("Fracture");

        assertEquals(2, report.size(), "Report should include two patients with 'Fracture'");
        assertTrue(report.get(0).contains("Fracture"), "Each record should mention the condition");
    }

    // Test 6 Saving and reloading database consistency
    @Test
    @Order(6)
    void testSaveAndReloadConsistency() {
        database.addPatient("Leo", "Cruz", "09-09-1999", "555-1111", "Stroke", true);
        saver.save(TEST_FILE, database.getAllRecords());

        List<Patient> reloaded = loader.load(TEST_FILE);

        assertEquals(1, reloaded.size(), "Reloaded file should contain one patient");
        assertEquals("Leo", reloaded.get(0).getFirstName(), "Reloaded patient name should match");
    }
}
