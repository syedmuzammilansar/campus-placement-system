package com.placement.dao;

import com.placement.model.Company;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UNIT-V: PreparedStatement CRUD operations for Company Drives.
 */
public class CompanyDAO {

    public boolean addCompany(Company company) {
        String sql = "INSERT INTO companies(name, role, package_lpa, min_cgpa, max_backlogs, eligible_branches) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getRole());
            pstmt.setDouble(3, company.getPackageLpa());
            pstmt.setDouble(4, company.getMinCgpa());
            pstmt.setInt(5, company.getMaxBacklogs());
            pstmt.setString(6, company.getEligibleBranches());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding company: " + e.getMessage());
            return false;
        }
    }

    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT * FROM companies ORDER BY package_lpa DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Company c = new Company(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getDouble("package_lpa"),
                        rs.getDouble("min_cgpa"),
                        rs.getInt("max_backlogs"),
                        rs.getString("eligible_branches")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching companies: " + e.getMessage());
        }
        return list;
    }

    public Company getCompanyById(int id) {
        String sql = "SELECT * FROM companies WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Company(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("role"),
                            rs.getDouble("package_lpa"),
                            rs.getDouble("min_cgpa"),
                            rs.getInt("max_backlogs"),
                            rs.getString("eligible_branches")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching company: " + e.getMessage());
        }
        return null;
    }
}
