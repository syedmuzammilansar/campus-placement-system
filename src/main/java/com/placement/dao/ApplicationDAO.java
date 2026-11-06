package com.placement.dao;

import com.placement.model.Application;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * UNIT-V: Demonstrates Basic Transaction Handling (commit & rollback)
 * and CRUD operations for Job Applications.
 */
public class ApplicationDAO {

    /**
     * Submits an application wrapped in an explicit ACID transaction.
     * Demonstrates setAutoCommit(false), commit(), and rollback().
     */
    public boolean submitApplication(String rollNo, int companyId, String companyName) {
        String insertSql = "INSERT INTO applications(roll_no, company_id, company_name, status, application_date) VALUES(?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();
            // Start Transaction: Disable Auto-Commit (Unit-V: Transaction Handling)
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, rollNo);
                pstmt.setInt(2, companyId);
                pstmt.setString(3, companyName);
                pstmt.setString(4, "APPLIED");
                pstmt.setString(5, LocalDate.now().toString());

                pstmt.executeUpdate();
            }

            // Commit transaction if successful
            conn.commit();
            return true;
        } catch (SQLException e) {
            // Rollback on any failure or constraint violation
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<Application> getApplicationsByStudent(String rollNo) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE roll_no = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Application(
                            rs.getInt("application_id"),
                            rs.getString("roll_no"),
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getString("status"),
                            rs.getString("application_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching applications: " + e.getMessage());
        }
        return list;
    }

    public List<Application> getAllApplications() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications ORDER BY application_id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Application(
                        rs.getInt("application_id"),
                        rs.getString("roll_no"),
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("status"),
                        rs.getString("application_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all applications: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int appId, String newStatus) {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, appId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }
}
