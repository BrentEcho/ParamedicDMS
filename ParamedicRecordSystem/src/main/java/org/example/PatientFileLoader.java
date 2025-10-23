package org.example;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PatientFileLoader {

    public List<Patient> load(String filename) {
        List<Patient> patients = new ArrayList<>();
        DateTimeFormatter dobFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter admissionFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue;

                int id = Integer.parseInt(parts[0].trim());
                String first = parts[1].trim();
                String last = parts[2].trim();
                LocalDate dob = LocalDate.parse(parts[3].trim(), dobFormat);
                String contact = parts[4].trim();
                String condition = parts[5].trim();
                LocalDateTime admission = LocalDateTime.parse(parts[6].trim(), admissionFormat).withNano(0);
                boolean status = Boolean.parseBoolean(parts[7].trim());

                // Correct constructor call
                patients.add(new Patient(id, first, last, dob, contact, condition, admission, status));
            }

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return patients;
    }
}
