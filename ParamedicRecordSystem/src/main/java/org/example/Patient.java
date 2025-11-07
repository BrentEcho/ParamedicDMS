package org.example;

import java.time.LocalDateTime;

public class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private String contact;
    private String medical_condition;
    private boolean active;
    private LocalDateTime recordTime;

    public Patient(int id, String firstName, String lastName, String contact,
                   String condition, boolean active, LocalDateTime recordTime) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.medical_condition = condition;
        this.active = active;
        this.recordTime = recordTime;
    }

    // Getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getContact() { return contact; }
    public String getMedical_condition() { return medical_condition; }
    public boolean isActive() { return active; }
    public LocalDateTime getRecordTime() { return recordTime; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setContact(String contact) { this.contact = contact; }
    public void setMedical_condition(String condition) { this.medical_condition = condition; }
    public void setActive(boolean active) { this.active = active; }
    public void setRecordTime(LocalDateTime recordTime) { this.recordTime = recordTime; }

    // Convert to CSV string
    public String toCsvString() {
        return id + "," + firstName + "," + lastName + "," + contact + "," + medical_condition + "," + active + "," + recordTime;
    }

    // Create a Patient from CSV string
    public static Patient fromCsvString(String csv) {
        String[] parts = csv.split(",");
        int id = Integer.parseInt(parts[0]);
        String firstName = parts[1];
        String lastName = parts[2];
        String contact = parts[3];
        String condition = parts[4];
        boolean active = Boolean.parseBoolean(parts[5]);
        LocalDateTime recordTime = LocalDateTime.parse(parts[6]);
        return new Patient(id, firstName, lastName, contact, condition, active, recordTime);
    }
}
