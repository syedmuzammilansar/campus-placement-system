package com.placement.model;

import java.io.Serializable;

/**
 * UNIT-I, II, III: Demonstrates Inheritance ('extends User'),
 * 'super' keyword, Method Overriding, and Comparable interface.
 */
public class Student extends User implements Comparable<Student>, Serializable {
    private static final long serialVersionUID = 1L;

    private String rollNo;
    private String branch;
    private double cgpa;
    private int backlogs;
    private String skills;

    // Overloaded Constructor 1
    public Student() {
        super();
    }

    // Overloaded Constructor 2 (Unit-II: using super keyword to invoke parent constructor)
    public Student(int id, String name, String email, String rollNo, String branch, double cgpa, int backlogs, String skills) {
        super(id, name, email);
        this.rollNo = rollNo;
        this.branch = branch;
        this.cgpa = cgpa;
        this.backlogs = backlogs;
        this.skills = skills;
    }

    // Unit-II: Method Overriding from abstract class User
    @Override
    public void displayProfile() {
        System.out.println("----------------------------------------");
        System.out.println(" STUDENT PROFILE: " + getName());
        System.out.println(" Roll No  : " + rollNo);
        System.out.println(" Branch   : " + branch);
        System.out.println(" CGPA     : " + cgpa);
        System.out.println(" Backlogs : " + backlogs);
        System.out.println(" Skills   : " + skills);
        System.out.println(" Email    : " + getEmail());
        System.out.println("----------------------------------------");
    }

    // Unit-III: Comparable implementation - Natural ordering by CGPA (Descending)
    @Override
    public int compareTo(Student other) {
        return Double.compare(other.cgpa, this.cgpa);
    }

    // Getters and Setters
    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public int getBacklogs() {
        return backlogs;
    }

    public void setBacklogs(int backlogs) {
        this.backlogs = backlogs;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-16s | %-5s | CGPA: %.2f | Backlogs: %d | Skills: %s",
                rollNo, getName(), branch, cgpa, backlogs, skills);
    }
}
