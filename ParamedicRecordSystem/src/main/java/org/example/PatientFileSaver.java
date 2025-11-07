package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class PatientFileSaver {
    public static void save(String filename, List<Patient> patients) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("ID,FirstName,LastName,DOB,Contact,Condition,AdmissionDateTime,Active\n");
            for (Patient p : patients) {
                bw.write(p.toCsvString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
