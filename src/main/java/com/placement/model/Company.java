package com.placement.model;

import java.io.Serializable;

/**
 * Model class representing a hiring company and its placement drive criteria.
 */
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String role;
    private double packageLpa;
    private double minCgpa;
    private int maxBacklogs;
    private String eligibleBranches; // e.g. "CSE,IT,ECE" or "ALL"

    public Company() {}

    public Company(int id, String name, String role, double packageLpa, double minCgpa, int maxBacklogs, String eligibleBranches) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.packageLpa = packageLpa;
        this.minCgpa = minCgpa;
        this.maxBacklogs = maxBacklogs;
        this.eligibleBranches = eligibleBranches;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(double packageLpa) { this.packageLpa = packageLpa; }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public int getMaxBacklogs() { return maxBacklogs; }
    public void setMaxBacklogs(int maxBacklogs) { this.maxBacklogs = maxBacklogs; }

    public String getEligibleBranches() { return eligibleBranches; }
    public void setEligibleBranches(String eligibleBranches) { this.eligibleBranches = eligibleBranches; }

    @Override
    public String toString() {
        return String.format("ID: %-2d | %-15s | Role: %-15s | CTC: %5.2f LPA | Min CGPA: %4.2f | Max Backlogs: %d | Branches: %s",
                id, name, role, packageLpa, minCgpa, maxBacklogs, eligibleBranches);
    }
}
