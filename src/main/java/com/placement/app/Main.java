package com.placement.app;

import com.placement.dao.ApplicationDAO;
import com.placement.dao.CompanyDAO;
import com.placement.dao.DatabaseManager;
import com.placement.dao.StudentDAO;
import com.placement.exception.BacklogExceededException;
import com.placement.exception.CutoffNotMetException;
import com.placement.model.Application;
import com.placement.model.Company;
import com.placement.model.Student;
import com.placement.service.NotificationService;
import com.placement.service.PlacementService;
import com.placement.util.FileReportUtil;
import com.placement.util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * MAIN ENTRY POINT FOR THE CAMPUS PLACEMENT ELIGIBILITY SYSTEM.
 * 
 * Interactive Console Interface featuring:
 * 1. Student Portal (Self registration, view companies, apply with eligibility checks)
 * 2. TPO / Recruiter Portal (Post drives, automated stream filtering, ranking, CSV export, multithreaded alerts)
 * 3. Quick Viva / Teacher Demonstration Mode (One-click automated walkthrough of all 5 units)
 */
public class Main {

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CompanyDAO companyDAO = new CompanyDAO();
    private static final ApplicationDAO applicationDAO = new ApplicationDAO();
    private static final PlacementService placementService = new PlacementService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initialize SQLite DB tables automatically (Unit-V)
        DatabaseManager.initializeDatabase();

        System.out.println(InputValidator.buildBanner("SMART CAMPUS PLACEMENT & RECRUITMENT MANAGEMENT SYSTEM"));
        System.out.println("  * Engineering 2nd Year Java Project (R25 Regulation)");
        System.out.println("  * Database: 100% Free SQLite Embedded (No Setup Required)");
        System.out.println("======================================================================
");

        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Enter your choice (1-4): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleStudentPortal();
                    break;
                case "2":
                    handleTpoPortal();
                    break;
                case "3":
                    runAutomatedVivaDemo();
                    break;
                case "4":
                    System.out.println("\nThank you for using Campus Placement System! Best of luck for your viva.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3, or 4.\n");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n+------------------------------------------------------+");
        System.out.println("|                      MAIN MENU                       |");
        System.out.println("+------------------------------------------------------+");
        System.out.println("|  1. Student Portal                                   |");
        System.out.println("|  2. Training & Placement Officer (TPO) Portal        |");
        System.out.println("|  3. ★ RUN FULL 5-UNIT VIVA DEMO (SHOW TO TEACHER)    |");
        System.out.println("|  4. Exit                                             |");
        System.out.println("+------------------------------------------------------+");
    }

    // -------------------------------------------------------------
    // 1. STUDENT PORTAL
    // -------------------------------------------------------------
    private static void handleStudentPortal() {
        System.out.println("\n=== STUDENT PORTAL ===");
        System.out.println("1. Register New Student (Unit-V: INSERT)");
        System.out.println("2. View Profile by Roll No");
        System.out.println("3. View Active Placement Drives");
        System.out.println("4. Apply for a Company Drive (Unit-II: Exception Handling)");
        System.out.println("5. View My Application Statuses");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter option: ");
        String opt = scanner.nextLine().trim();

        switch (opt) {
            case "1":
                registerStudent();
                break;
            case "2":
                viewStudentProfile();
                break;
            case "3":
                listAllCompanies();
                break;
            case "4":
                applyForDrive();
                break;
            case "5":
                viewMyApplications();
                break;
            case "6":
                return;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void registerStudent() {
        System.out.println("\n--- New Student Registration ---");
        System.out.print("Enter Roll No (e.g. 24CS010): ");
        String roll = scanner.nextLine().trim().toUpperCase();

        if (studentDAO.getStudentByRollNo(roll) != null) {
            System.out.println("Student with Roll No " + roll + " is already registered.");
            return;
        }

        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter Branch (CSE/IT/ECE/MECH/CIVIL): ");
        String branch = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter Current CGPA (0.0 - 10.0): ");
        double cgpa = 0.0;
        try {
            cgpa = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid CGPA format. Aborted.");
            return;
        }

        System.out.print("Enter Number of Active Backlogs: ");
        int backlogs = 0;
        try {
            backlogs = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid backlogs count. Aborted.");
            return;
        }

        System.out.print("Enter Technical Skills (comma-separated): ");
        String skills = scanner.nextLine().trim();

        Student student = new Student(0, name, email, roll, branch, cgpa, backlogs, skills);
        boolean ok = studentDAO.addStudent(student);
        if (ok) {
            System.out.println("✓ Student registered successfully in SQLite database!");
            student.displayProfile();
        } else {
            System.out.println("✗ Registration failed. Please check inputs.");
        }
    }

    private static void viewStudentProfile() {
        System.out.print("\nEnter Roll No: ");
        String roll = scanner.nextLine().trim().toUpperCase();
        Student s = studentDAO.getStudentByRollNo(roll);
        if (s != null) {
            s.displayProfile();
        } else {
            System.out.println("Student not found with Roll No: " + roll);
        }
    }

    private static void applyForDrive() {
        System.out.print("\nEnter your Roll No: ");
        String roll = scanner.nextLine().trim().toUpperCase();
        Student student = studentDAO.getStudentByRollNo(roll);
        if (student == null) {
            System.out.println("Student not registered. Please register first.");
            return;
        }

        listAllCompanies();
        System.out.print("Enter Company ID to apply: ");
        int companyId;
        try {
            companyId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Company ID.");
            return;
        }

        Company company = companyDAO.getCompanyById(companyId);
        if (company == null) {
            System.out.println("Company not found.");
            return;
        }

        // UNIT-II: Custom Exception Checking
        try {
            System.out.println("Checking eligibility for " + company.getName() + "...");
            placementService.validateEligibility(student, company);

            // UNIT-V: Transactional DB insert
            boolean applied = applicationDAO.submitApplication(student.getRollNo(), company.getId(), company.getName());
            if (applied) {
                System.out.println("✓ APPLICATION SUCCESSFUL! You meet all eligibility cutoffs.");
            } else {
                System.out.println("Notice: You have already applied for this company drive.");
            }
        } catch (CutoffNotMetException | BacklogExceededException e) {
            System.err.println("
✗ [ELIGIBILITY REJECTION]: " + e.getMessage());
            System.out.println("Application rejected as per company placement guidelines.");
        }
    }

    private static void viewMyApplications() {
        System.out.print("\nEnter Roll No: ");
        String roll = scanner.nextLine().trim().toUpperCase();
        List<Application> apps = applicationDAO.getApplicationsByStudent(roll);
        if (apps.isEmpty()) {
            System.out.println("No applications found for " + roll);
        } else {
            System.out.println("
Applications for Roll No: " + roll);
            System.out.println("---------------------------------------------------------------------------------");
            apps.forEach(System.out::println);
            System.out.println("---------------------------------------------------------------------------------");
        }
    }

    // -------------------------------------------------------------
    // 2. TPO / ADMIN PORTAL
    // -------------------------------------------------------------
    private static void handleTpoPortal() {
        System.out.println("\n=== TPO / PLACEMENT OFFICER PORTAL ===");
        System.out.println("1. Post New Company Drive");
        System.out.println("2. View All Registered Students");
        System.out.println("3. Run Stream API Eligibility Filter (Unit-III: Streams)");
        System.out.println("4. Export Shortlist to CSV (Unit-IV: File Handling)");
        System.out.println("5. Trigger Multithreaded Notifications (Unit-IV: Threads)");
        System.out.println("6. View Student Distribution by Branch (Unit-III: Map)");
        System.out.println("7. Back to Main Menu");
        System.out.print("Enter option: ");
        String opt = scanner.nextLine().trim();

        switch (opt) {
            case "1":
                postCompany();
                break;
            case "2":
                listAllStudents();
                break;
            case "3":
                runEligibilityStreamFilter();
                break;
            case "4":
                exportShortlist();
                break;
            case "5":
                triggerNotifications();
                break;
            case "6":
                showBranchDistribution();
                break;
            case "7":
                return;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void postCompany() {
        System.out.println("\n--- Post New Company Placement Drive ---");
        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Job Role: ");
        String role = scanner.nextLine().trim();

        System.out.print("Enter CTC Package (LPA): ");
        double ctc = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Minimum CGPA Cutoff: ");
        double minCgpa = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Maximum Allowed Backlogs (0, 1, 2...): ");
        int maxBacklogs = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Eligible Branches (e.g. CSE,IT or ALL): ");
        String branches = scanner.nextLine().trim().toUpperCase();

        Company c = new Company(0, name, role, ctc, minCgpa, maxBacklogs, branches);
        if (companyDAO.addCompany(c)) {
            System.out.println("✓ Placement drive posted successfully in database!");
        } else {
            System.out.println("✗ Failed to post company drive.");
        }
    }

    private static void listAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        System.out.println("\n--- Registered Students (" + students.size() + ") ---");
        System.out.println("--------------------------------------------------------------------------------------------------");
        students.forEach(System.out::println);
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    private static void listAllCompanies() {
        List<Company> companies = companyDAO.getAllCompanies();
        System.out.println("\n--- Active Placement Drives ---");
        System.out.println("--------------------------------------------------------------------------------------------------");
        companies.forEach(System.out::println);
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    private static void runEligibilityStreamFilter() {
        listAllCompanies();
        System.out.print("Enter Company ID to run filter: ");
        int compId = Integer.parseInt(scanner.nextLine().trim());
        Company company = companyDAO.getCompanyById(compId);
        if (company == null) {
            System.out.println("Company not found.");
            return;
        }

        List<Student> allStudents = studentDAO.getAllStudents();

        // UNIT-III: Stream API filter and custom comparator
        List<Student> eligible = placementService.filterEligibleStudents(allStudents, company);

        System.out.println("
==================================================================================================");
        System.out.println(" STREAM API ELIGIBILITY SHORTLIST FOR: " + company.getName() + " (" + company.getRole() + ")");
        System.out.println(" Criteria: Min CGPA " + company.getMinCgpa() + " | Max Backlogs: " + company.getMaxBacklogs() + " | Branches: " + company.getEligibleBranches());
        System.out.println("==================================================================================================");
        if (eligible.isEmpty()) {
            System.out.println("No candidates meet the eligibility criteria.");
        } else {
            eligible.forEach(System.out::println);
        }
        System.out.println("==================================================================================================");
        System.out.println("Total Eligible Candidates: " + eligible.size() + " out of " + allStudents.size());
    }

    private static void exportShortlist() {
        listAllCompanies();
        System.out.print("Enter Company ID to export shortlist: ");
        int compId = Integer.parseInt(scanner.nextLine().trim());
        Company company = companyDAO.getCompanyById(compId);
        if (company == null) return;

        List<Student> allStudents = studentDAO.getAllStudents();
        List<Student> eligible = placementService.filterEligibleStudents(allStudents, company);

        // UNIT-IV: Export using File Handling & Character Streams
        String filePath = FileReportUtil.exportShortlistToCsv(company.getName(), eligible);
        if (filePath != null) {
            System.out.println("
✓ CSV Report successfully generated at: " + filePath);
            System.out.println("  You can open this file in Excel or attach it to your college submission!");
        }
    }

    private static void triggerNotifications() {
        listAllCompanies();
        System.out.print("Enter Company ID to notify shortlisted students: ");
        int compId = Integer.parseInt(scanner.nextLine().trim());
        Company company = companyDAO.getCompanyById(compId);
        if (company == null) return;

        List<Student> allStudents = studentDAO.getAllStudents();
        List<Student> eligible = placementService.filterEligibleStudents(allStudents, company);

        if (eligible.isEmpty()) {
            System.out.println("No candidates to notify.");
            return;
        }

        // UNIT-IV: Multithreaded execution
        NotificationService.dispatchParallelNotifications(eligible, company.getName());
    }

    private static void showBranchDistribution() {
        List<Student> students = studentDAO.getAllStudents();
        var distribution = placementService.getStudentCountByBranch(students);

        System.out.println("
--- Student Count by Branch (Unit-III: Map & Collectors) ---");
        distribution.forEach((branch, count) ->
                System.out.printf(" Branch: %-8s | Total Students: %d
", branch, count));
    }

    // -------------------------------------------------------------
    // 3. AUTOMATED 5-UNIT VIVA DEMONSTRATION (PERFECT TO SHOW TO MAM)
    // -------------------------------------------------------------
    private static void runAutomatedVivaDemo() {
        System.out.println("
======================================================================");
        System.out.println("        STARTING AUTOMATED 5-UNIT SYLLABUS DEMO FOR VIVA");
        System.out.println("======================================================================");

        // --- UNIT I: Fundamentals & OOP ---
        System.out.println("
>>> [UNIT-I] OOP PRINCIPLES & FUNDAMENTALS:");
        System.out.println("Creating Student object demonstrating Encapsulation, 'this', and constructors...");
        Student demoStudent = new Student(99, "Vikram Sen", "vikram@campus.edu", "24CS099", "CSE", 8.90, 0, "Java, Data Structures");
        demoStudent.displayProfile();

        // --- UNIT II: Advanced OOP & Custom Exceptions ---
        System.out.println("
>>> [UNIT-II] INHERITANCE, METHOD OVERRIDING & CUSTOM EXCEPTIONS:");
        System.out.println("Student inherits from abstract class 'User' and overrides 'displayProfile()'.");
        System.out.println("Testing custom exception handling: Trying to apply for Google with 6.2 CGPA...");
        Student lowCgpaStudent = new Student(98, "Amit Kumar", "amit@campus.edu", "24ME098", "MECH", 6.20, 2, "CAD");
        Company google = companyDAO.getCompanyById(1); // Google min 8.5 CGPA

        try {
            placementService.validateEligibility(lowCgpaStudent, google);
        } catch (CutoffNotMetException | BacklogExceededException e) {
            System.out.println("✓ CATCH BLOCK EXECUTED: " + e.getClass().getSimpleName() + " caught!");
            System.out.println("  Exception message: " + e.getMessage());
        }

        // --- UNIT III: Collections & Stream API ---
        System.out.println("
>>> [UNIT-III] COLLECTIONS FRAMEWORK & STREAM API (filter, map, sorted):");
        System.out.println("Running Stream pipeline to filter eligible students for " + google.getName() + "...");
        List<Student> all = studentDAO.getAllStudents();
        List<Student> googleShortlist = placementService.filterEligibleStudents(all, google);
        googleShortlist.forEach(s -> System.out.println("  -> Shortlisted: " + s));

        // --- UNIT IV: Multithreading & File Handling ---
        System.out.println("
>>> [UNIT-IV] MULTITHREADING & FILE HANDLING:");
        System.out.println("1. Generating CSV Shortlist file using Character Streams (BufferedWriter)...");
        String csvPath = FileReportUtil.exportShortlistToCsv(google.getName(), googleShortlist);
        System.out.println("   File generated at: " + csvPath);

        System.out.println("2. Spawning concurrent threads to simulate interview dispatch...");
        NotificationService.dispatchParallelNotifications(googleShortlist, google.getName());

        // --- UNIT V: JDBC & Transactions ---
        System.out.println("
>>> [UNIT-V] JDBC ARCHITECTURE & CRUD WITH PREPARED STATEMENT:");
        System.out.println("Querying registered companies from SQLite database using PreparedStatement...");
        List<Company> companies = companyDAO.getAllCompanies();
        System.out.println("Retrieved " + companies.size() + " companies from local database 'database/placement.db'.");

        System.out.println("
======================================================================");
        System.out.println(" ✓ 5-UNIT DEMO COMPLETE! All concepts successfully verified.");
        System.out.println("======================================================================
");
    }
}
