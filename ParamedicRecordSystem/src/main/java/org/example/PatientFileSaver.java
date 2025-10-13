package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
/**
 * Brent Echols, CEN-3024C, 10/13/2025
 * patientfilesaver
 * updates the patients_sample file
 */
class PatientFileSaver {
    private static final String HEADER = "ID,FirstName,LastName,DOB,Contact,Condition,Admission,Status";
    public String save(String filename, List<Patient> patients) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(HEADER);
            bw.newLine();
            for (Patient p : patients) {
                bw.write(p.toCsvString());
                bw.newLine();
            }
            return "Saved " + patients.size() + " records to " + filename;
        } catch (IOException e) {
            return "Error saving file: " + e.getMessage();
        }
    }
}