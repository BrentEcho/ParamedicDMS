package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientDatabase {

    private final String url;
    private final String username;
    private final String password;

    public PatientDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // ✅ Add patient (6 args — matches your TESTS)
    public void addPatient(String firstName, String lastName, String contact,
                           String medicalCondition, boolean active, LocalDateTime recordTime) throws SQLException {

        String sql = "INSERT INTO patients (first_name, last_name, contact, medical_condition, active, record_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, contact);
            stmt.setString(4, medicalCondition);
            stmt.setBoolean(5, active);
            stmt.setTimestamp(6, Timestamp.valueOf(recordTime != null ? recordTime : LocalDateTime.now()));
            stmt.executeUpdate();
        }
    }

    // ✅ Overload (5 args — for GUI)
    public void addPatient(String firstName, String lastName, String contact,
                           String medicalCondition, boolean active) throws SQLException {
        addPatient(firstName, lastName, contact, medicalCondition, active, LocalDateTime.now());
    }

    // ✅ Update any field
    public void updatePatientField(int id, String field, Object value) throws SQLException {
        String sql = "UPDATE patients SET " + field + " = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (value instanceof Boolean) {
                stmt.setBoolean(1, (Boolean) value);
            } else {
                stmt.setString(1, value.toString());
            }
            stmt.setInt(2, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No patient found with ID: " + id);
            }
        }
    }

    // ✅ Delete (alias for tests)
    public void removePatient(int id) throws SQLException {
        deletePatient(id);
    }

    // ✅ Delete patient
    public void deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ✅ Find patient by ID
    public Patient findById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapPatient(rs);
            }
        }
        return null;
    }

    // ✅ View all patients
    public List<Patient> viewAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id ASC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        }
        return patients;
    }

    // ✅ Report by condition
    public List<Patient> reportByCondition(String condition) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE medical_condition LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + condition + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        }
        return patients;
    }

    // ✅ Clear all (for tests)
    public void clearAll() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM patients");
        }
    }

    // ✅ Helper method
    private Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("contact"),
                rs.getString("medical_condition"),
                rs.getBoolean("active"),
                rs.getTimestamp("record_time").toLocalDateTime()
        );
    }
}
