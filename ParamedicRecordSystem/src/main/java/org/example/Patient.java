package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Brent Echols, CEN-3024C, 10/13/2025
 * Patient Constructor class
 * creates the patient objects
 *
 */

class Patient {
    private final int patientID;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth; // MM-dd-yyyy
    private String emergencyContact;
    private String medicalCondition;
    private LocalDateTime admissionDate; // rounded to seconds
    private boolean status; // active/inactive

    private static final DateTimeFormatter DOB_FMT = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    public Patient(int patientID, String firstName, String lastName, LocalDate dateOfBirth,
                   String emergencyContact, String medicalCondition, LocalDateTime admissionDate, boolean status) {
        this.patientID = patientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.emergencyContact = emergencyContact;
        this.medicalCondition = medicalCondition;
        this.admissionDate = admissionDate.withNano(0); // round to whole second
        this.status = status;
    }

    public int getPatientID() { return patientID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getEmergencyContact() { return emergencyContact; }
    public String getMedicalCondition() { return medicalCondition; }
    public LocalDateTime getAdmissionDate() { return admissionDate; }
    public boolean isActive() { return status; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }
    public void setAdmissionDate(LocalDateTime admissionDate) { this.admissionDate = admissionDate.withNano(0); }
    public void setStatus(boolean status) { this.status = status; }

    public String toDisplayString() {
        return String.format("ID:%d | %s %s | DOB:%s | Contact:%s | Condition:%s | Admitted:%s | Active:%s",
                patientID, firstName, lastName,
                dateOfBirth.format(DOB_FMT),
                emergencyContact,
                medicalCondition,
                admissionDate.format(DT_FMT),
                status ? "Yes" : "No");
    }

    public String toCsvString() {
        return String.join(",",
                Integer.toString(patientID),
                firstName,
                lastName,
                dateOfBirth.format(DOB_FMT),
                emergencyContact,
                medicalCondition,
                admissionDate.format(DT_FMT),
                Boolean.toString(status)
        );
    }

    public static Optional<LocalDate> parseDob(String s) {
        try {
            return Optional.of(LocalDate.parse(s, DOB_FMT));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDateTime> parseDateTime(String s) {
        try {
            return Optional.of(LocalDateTime.parse(s, DT_FMT).withNano(0));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}

