package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Reads a CSV file into a list of patients.
 *
 * @return list of Patient objects
 * @throws Exception on file IO or format failure
 */

public class PatientFileLoader {

    public static List<Patient> loadPatientsFromFile(String filename) throws Exception {
        List<Patient> patients = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            // Parse CSV line
            Patient p = Patient.fromCsvString(line);
            patients.add(p);
        }
        reader.close();
        return patients;
    }

    public static void savePatientsToFile(List<Patient> patients, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Patient p : patients) {
            writer.write(p.toCsvString());
            writer.newLine();
        }
        writer.close();
    }
}
