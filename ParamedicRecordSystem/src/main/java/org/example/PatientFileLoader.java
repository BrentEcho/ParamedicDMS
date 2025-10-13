package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * Brent Echols, CEN-3024C, 10/13/2025
 * PatientFileLoader
 * Reads the sample_file and imports the patient information
 */
class PatientFileLoader {
    private static final DateTimeFormatter DOB_FMT = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    // loads patient data from patient_sample.txt
    public List<Patient> load(String filename) {
        List<Patient> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String header = br.readLine(); // skip header if exists
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue;
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String fn = parts[1].trim();
                    String ln = parts[2].trim();
                    LocalDate dob = LocalDate.parse(parts[3].trim(), DOB_FMT);
                    String contact = parts[4].trim();
                    String condition = parts[5].trim();
                    LocalDateTime admission = LocalDateTime.parse(parts[6].trim(), DT_FMT).withNano(0);
                    boolean status = Boolean.parseBoolean(parts[7].trim());
                    list.add(new Patient(id, fn, ln, dob, contact, condition, admission, status));
                } catch (Exception ex) {
                    // skip malformed line but continue loading others
                }
            }
        } catch (IOException e) {
            // File not found or unreadable: return empty list (caller can handle)
        }
        return list;
    }
}
