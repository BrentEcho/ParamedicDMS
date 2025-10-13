package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Brent Echols, CEN-3024C, 10/13/2025
 * patient database
 * creates the array list of all the patients added
 *
 */
class PatientDatabase {
    private final List<Patient> records = new ArrayList<>();
    private int nextID = 1;

    public String addPatient(String firstName, String lastName, String dob, String contact,
                             String condition, boolean status) {
        Optional<LocalDate> maybeDob = Patient.parseDob(dob);
        if (maybeDob.isEmpty()) {
            return "Invalid DOB format. Use MM-DD-YYYY.";
        }
        LocalDate birth = maybeDob.get();
        LocalDateTime admission = LocalDateTime.now();
        int id = nextID++;
        Patient p = new Patient(id, firstName.trim(), lastName.trim(), birth, contact.trim(), condition.trim(), admission, status);
        records.add(p);
        return "Patient added: " + p.toDisplayString();
    }

    public void addPatientFromFile(Patient p) {
        records.add(p);
        nextID = Math.max(nextID, p.getPatientID() + 1);
    }

    public void setNextID(int next) { this.nextID = Math.max(next, this.nextID); }

    public List<String> viewAllPatients() {
        if (records.isEmpty()) return List.of("No patient records available.");
        return records.stream().map(Patient::toDisplayString).collect(Collectors.toList());
    }

    public Optional<Patient> findById(int id) {
        return records.stream().filter(r -> r.getPatientID() == id).findFirst();
    }
    
    public String updatePatientField(int id, String field, String newValue) {
        Optional<Patient> opt = findById(id);
        if (opt.isEmpty()) return "Patient ID not found.";
        Patient p = opt.get();
        switch (field.toLowerCase()) {
            case "firstname":
                p.setFirstName(newValue);
                break;
            case "lastname":
                p.setLastName(newValue);
                break;
            case "dob":
                Optional<LocalDate> d = Patient.parseDob(newValue);
                if (d.isEmpty()) return "Invalid DOB format. Use MM-DD-YYYY.";
                p.setDateOfBirth(d.get());
                break;
            case "contact":
                p.setEmergencyContact(newValue);
                break;
            case "condition":
                p.setMedicalCondition(newValue);
                break;
            case "status":
                p.setStatus(newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("yes"));
                break;
            default:
                return "Unknown field name. Valid fields: firstname, lastname, dob, contact, condition, status";
        }
        return "Updated: " + p.toDisplayString();
    }
    //Removes patients
    public String removePatient(int id) {
        boolean removed = records.removeIf(r -> r.getPatientID() == id);
        return removed ? ("Patient " + id + " removed.") : ("Patient ID not found.");
    }

    public List<Patient> getAllRecords() { return new ArrayList<>(records); }

    // Custom feature: report by condition
    public List<String> reportByCondition(String condition) {
        List<Patient> filtered = records.stream()
                .filter(p -> p.getMedicalCondition().equalsIgnoreCase(condition.trim()))
                .toList();
        if (filtered.isEmpty()) return List.of("No patients found for condition: " + condition);
        return filtered.stream().map(Patient::toDisplayString).collect(Collectors.toList());
    }

    // Custom feature: report by date range (inclusive). Dates format MM-DD-YYYY
    public List<String> reportByDateRange(String fromDateStr, String toDateStr) {
        Optional<LocalDate> fromOpt = Patient.parseDob(fromDateStr);
        Optional<LocalDate> toOpt = Patient.parseDob(toDateStr);
        if (fromOpt.isEmpty() || toOpt.isEmpty()) return List.of("Invalid date format. Use MM-DD-YYYY for range.");
        LocalDate from = fromOpt.get();
        LocalDate to = toOpt.get();
        List<Patient> filtered = records.stream()
                .filter(p -> {
                    LocalDate admittedDate = p.getAdmissionDate().toLocalDate();
                    return (!admittedDate.isBefore(from)) && (!admittedDate.isAfter(to));
                })
                .toList();
        if (filtered.isEmpty()) return List.of("No patients admitted between " + fromDateStr + " and " + toDateStr);
        return filtered.stream().map(Patient::toDisplayString).collect(Collectors.toList());
    }

    public int highestId() {
        return records.stream().mapToInt(Patient::getPatientID).max().orElse(0);
    }
}
