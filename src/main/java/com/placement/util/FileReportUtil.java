package com.placement.util;

import com.placement.model.Application;
import com.placement.model.Student;

import java.io.*;
import java.util.List;

/**
 * UNIT-IV: Java Input/Output (java.io).
 * - File class
 * - Character Streams (FileWriter, BufferedWriter)
 * - Object Serialization & Deserialization (ObjectOutputStream, ObjectInputStream)
 */
public class FileReportUtil {

    private static final String EXPORT_DIR = "exports";

    /**
     * Character Stream: Exports shortlisted candidates to a clean CSV file.
     * Uses BufferedWriter for high-performance buffered writes.
     */
    public static String exportShortlistToCsv(String companyName, List<Student> students) {
        File dir = new File(EXPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String safeCompanyName = companyName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String filename = EXPORT_DIR + "/shortlist_" + safeCompanyName + ".csv";

        // Try-with-resources auto-closes the character stream
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write CSV Header
            writer.write("Roll No,Name,Email,Branch,CGPA,Active Backlogs,Skills");
            writer.newLine();

            // Write each candidate record
            for (Student s : students) {
                String line = String.format("%s,%s,%s,%s,%.2f,%d,\"%s\"",
                        s.getRollNo(),
                        s.getName(),
                        s.getEmail(),
                        s.getBranch(),
                        s.getCgpa(),
                        s.getBacklogs(),
                        s.getSkills());
                writer.write(line);
                writer.newLine();
            }

            return filename;
        } catch (IOException e) {
            System.err.println("Error writing CSV report: " + e.getMessage());
            return null;
        }
    }

    /**
     * UNIT-IV: Byte Stream Object Serialization.
     * Serializes List of Application objects into binary file.
     */
    public static boolean serializeApplications(List<Application> apps, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(apps);
            return true;
        } catch (IOException e) {
            System.err.println("Serialization error: " + e.getMessage());
            return false;
        }
    }

    /**
     * UNIT-IV: Byte Stream Object Deserialization.
     * Reconstructs Application objects from binary file.
     */
    @SuppressWarnings("unchecked")
    public static List<Application> deserializeApplications(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Application>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization error: " + e.getMessage());
            return null;
        }
    }
}
