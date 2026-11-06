package com.placement.dao;

import com.placement.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UNIT-V: PreparedStatement & CRUD Operations for Student.
 * PreparedStatements protect against SQL injection and compile faster.
 */
public class StudentDAO {

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students(roll_no, name, email, branch, cgpa, backlogs, skills) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getRollNo());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getBranch());
            pstmt.setDouble(5, student.getCgpa());
            pstmt.setInt(6, student.getBacklogs());
            pstmt.setString(7, student.getSkills());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_no ASC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("roll_no"),
                        rs.getString("branch"),
                        rs.getDouble("cgpa"),
                        rs.getInt("backlogs"),
                        rs.getString("skills")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
        return list;
    }

    public Student getStudentByRollNo(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("roll_no"),
                            rs.getString("branch"),
                            rs.getDouble("cgpa"),
                            rs.getInt("backlogs"),
                            rs.getString("skills")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding student: " + e.getMessage());
        }
        return null;
    }
}
