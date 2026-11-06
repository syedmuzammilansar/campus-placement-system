package com.placement.model;

import java.io.Serializable;

/**
 * Model representing a job application by a student for a specific company drive.
 * UNIT-IV: Implements Serializable for binary file storage.
 */
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private int applicationId;
    private String rollNo;
    private int companyId;
    private String companyName;
    private String status; // "APPLIED", "SHORTLISTED", "REJECTED", "PLACED"
    private String applicationDate;

    public Application() {}

    public Application(int applicationId, String rollNo, int companyId, String companyName, String status, String applicationDate) {
        this.applicationId = applicationId;
        this.rollNo = rollNo;
        this.companyId = companyId;
        this.companyName = companyName;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApplicationDate() { return applicationDate; }
    public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }

    @Override
    public String toString() {
        return String.format("App#%-3d | Roll: %-10s | Company: %-15s | Status: %-12s | Date: %s",
                applicationId, rollNo, companyName, status, applicationDate);
    }
}
