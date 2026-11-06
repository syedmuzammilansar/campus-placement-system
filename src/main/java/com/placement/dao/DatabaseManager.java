package com.placement.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * UNIT-V: JDBC Architecture & Connection Handling.
 * Uses 100% free, zero-configuration SQLite.
 * Automatically initializes database tables on first launch.
 */
public class DatabaseManager {
    // Database file will reside locally in the project's database folder
    private static final String DB_DIR = "database";
    private static final String DB_FILE = "database/placement.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    // Static block or method to establish connection
    public static Connection getConnection() throws SQLException {
        try {
            // Register SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Fallback for classpath variations
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates all necessary tables if they do not exist.
     * Demonstrates Statement execution in JDBC.
     */
    public static void initializeDatabase() {
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "roll_no TEXT UNIQUE NOT NULL, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "branch TEXT NOT NULL, " +
                "cgpa REAL NOT NULL, " +
                "backlogs INTEGER NOT NULL, " +
                "skills TEXT" +
                ");";

        String createCompaniesTable = "CREATE TABLE IF NOT EXISTS companies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "package_lpa REAL NOT NULL, " +
                "min_cgpa REAL NOT NULL, " +
                "max_backlogs INTEGER NOT NULL, " +
                "eligible_branches TEXT NOT NULL" +
                ");";

        String createApplicationsTable = "CREATE TABLE IF NOT EXISTS applications (" +
                "application_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "roll_no TEXT NOT NULL, " +
                "company_id INTEGER NOT NULL, " +
                "company_name TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "application_date TEXT NOT NULL, " +
                "UNIQUE(roll_no, company_id), " +
                "FOREIGN KEY(roll_no) REFERENCES students(roll_no), " +
                "FOREIGN KEY(company_id) REFERENCES companies(id)" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createStudentsTable);
            stmt.execute(createCompaniesTable);
            stmt.execute(createApplicationsTable);
            
            // Seed sample data if empty
            seedInitialData(conn);
        } catch (SQLException e) {
            System.err.println("Database initialization notice: " + e.getMessage());
        }
    }

    private static void seedInitialData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Check if students already seeded
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM students;");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("INSERT INTO students (roll_no, name, email, branch, cgpa, backlogs, skills) VALUES " +
                        "('24CS001', 'Rahul Sharma', 'rahul@campus.edu', 'CSE', 8.75, 0, 'Java, Spring, SQL'), " +
                        "('24CS002', 'Priya Patel', 'priya@campus.edu', 'CSE', 9.10, 0, 'Java, Python, Cloud'), " +
                        "('24IT003', 'Ananya Reddy', 'ananya@campus.edu', 'IT', 7.80, 0, 'Java, Web, React'), " +
                        "('24EC004', 'Kiran Kumar', 'kiran@campus.edu', 'ECE', 6.90, 1, 'C++, Embedded, IoT'), " +
                        "('24CS005', 'Sneha Roy', 'sneha@campus.edu', 'CSE', 8.20, 0, 'Java, AI, MySQL');");

                stmt.executeUpdate("INSERT INTO companies (name, role, package_lpa, min_cgpa, max_backlogs, eligible_branches) VALUES " +
                        "('Google', 'Software Engineer', 24.5, 8.5, 0, 'CSE,IT'), " +
                        "('Microsoft', 'SWE Intern', 18.0, 8.0, 0, 'CSE,IT,ECE'), " +
                        "('TCS Digital', 'Systems Engineer', 7.5, 7.0, 1, 'ALL'), " +
                        "('Infosys', 'Specialist Programmer', 9.5, 7.5, 0, 'CSE,IT');");
            }
        } catch (SQLException ignored) {
        }
    }
}
