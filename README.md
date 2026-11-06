# Campus Placement Eligibility & Skill-Ranking System

> **Subject:** Object-Oriented Programming through Java  
> **Target Curriculum:** B.Tech / B.E. 2nd Year (R25 Regulation)  
> **Technology Stack:** Java 17+, SQLite (JDBC), Multithreading, Streams API, File I/O  
> **Cost & Hardware Requirements:** 100% Free Tools | Zero External Hardware Needed

---

## 1. Project Overview & Problem Statement

In engineering colleges, the **Training and Placement Officer (TPO)** and recruiters struggle during placement seasons with:
1. Manually sorting through hundreds of student resumes across different branches.
2. Checking varying cutoff criteria (e.g. Google requiring 8.5 CGPA and 0 backlogs, whereas TCS Digital allows 7.0 CGPA and 1 backlog).
3. Concurrently notifying eligible candidates without freezing computer systems.
4. Keeping a persistent audit trail of student applications and selection results.

**This project solves these challenges** with a high-performance, modular Java recruitment engine that automates eligibility screening, ranks candidates by merit, dispatches notifications in parallel threads, and logs results into both SQLite and exported CSV spreadsheets.

---

## 2. Direct Syllabus Mapping (R25 Regulation)

When presenting to your professor ("mam"), use this exact mapping:

| Syllabus Unit | Topics Covered in Syllabus | How Implemented in This Project | Exact Code Location |
| :--- | :--- | :--- | :--- |
| **UNIT - I** | OOP principles (Abstraction, Encapsulation), Structure of Java Program, Constructors, `this`, `static`, `final`, `String` & `StringBuilder`. | Encapsulated private variables, overloaded constructors, dynamic table banners built with `StringBuilder`, `static final` database paths. | `User.java`, `Student.java`, `InputValidator.java` |
| **UNIT - II** | Inheritance, `super` keyword, Abstract Classes, Interfaces, Exception Handling (`try-catch-finally-throw-throws`), **Custom Exceptions**, **Lambda Expressions**. | `Student extends User`, `super()` constructor calls, overridden `displayProfile()`, custom exceptions `CutoffNotMetException` & `BacklogExceededException`, `@FunctionalInterface EligibilityChecker` with lambda expressions. | `User.java`, `Student.java`, `CutoffNotMetException.java`, `EligibilityChecker.java`, `PlacementService.java` |
| **UNIT - III** | Collections Framework (`List`, `Map`, `ArrayList`), `Comparable` & `Comparator`, **Stream API (`filter`, `map`, `sorted`)**. | Natural ordering by CGPA with `Comparable<Student>`, tie-breaker sorting with `Comparator`, stream filtering (`students.stream().filter(...).collect(...)`), and grouping by branch using `Map`. | `Student.java`, `PlacementService.java` |
| **UNIT - IV** | Multithreading (Thread & `Runnable`), Thread Lifecycle, **Synchronization**, File Handling (`File`, Character Streams `BufferedWriter`), **Serialization**. | Background candidate notification worker implementing `Runnable`, synchronized counter preventing race conditions, exporting CSV shortlists using `BufferedWriter`, and `Serializable` applications. | `NotificationService.java`, `FileReportUtil.java`, `Application.java` |
| **UNIT - V** | **JDBC Architecture**, `PreparedStatement` vs `Statement`, **CRUD Operations**, **Transaction Handling (`commit`/`rollback`)**, **Mini Case Study: Student Management**. | 100% free embedded SQLite database, parameterized SQL queries preventing injection, ACID transaction rollback on failure, complete CRUD operations for students, drives, and applications. | `DatabaseManager.java`, `StudentDAO.java`, `CompanyDAO.java`, `ApplicationDAO.java` |

---

## 3. Package & Directory Structure

```
CampusPlacementSystem/
│
├── pom.xml                                   # Maven build file with free SQLite JDBC driver
├── run.bat                                   # 1-Click execution script for Windows
├── run.sh                                    # 1-Click execution script for Mac/Linux
├── .gitignore                                # Standard git ignore file
├── README.md                                 # Full documentation & viva guide
│
├── database/                                 # Zero-server embedded database folder
│   └── placement.db                          # Auto-created SQLite database file
│
├── exports/                                  # Exported reports folder
│   └── shortlist_Google.csv                  # Auto-generated CSV candidate reports
│
└── src/main/java/com/placement/
    │
    ├── model/                                # [Unit I & II] Domain Data Models
    │   ├── User.java                         # Abstract class (Abstraction & Encapsulation)
    │   ├── Student.java                      # Extends User, implements Comparable & Serializable
    │   ├── Company.java                      # Hiring company criteria model
    │   └── Application.java                  # Job application model (Serializable)
    │
    ├── exception/                            # [Unit II] Custom Checked Exceptions
    │   ├── CutoffNotMetException.java        # Thrown when CGPA is below cutoff
    │   ├── BacklogExceededException.java     # Thrown when active backlogs exceed maximum
    │   └── DuplicateApplicationException.java# Thrown on repeat applications
    │
    ├── dao/                                  # [Unit V] JDBC Data Access Objects
    │   ├── DatabaseManager.java              # Connection & table auto-initialization
    │   ├── StudentDAO.java                   # CRUD for Students using PreparedStatement
    │   ├── CompanyDAO.java                   # CRUD for Companies
    │   └── ApplicationDAO.java               # Transaction handling (commit/rollback)
    │
    ├── service/                              # [Unit II, III, IV] Core Business Logic
    │   ├── EligibilityChecker.java           # Functional Interface for Lambda Expressions
    │   ├── PlacementService.java             # Java 8 Stream API & Custom Comparators
    │   └── NotificationService.java          # Multithreading, Runnable & Synchronization
    │
    ├── util/                                 # [Unit I & IV] Utilities
    │   ├── FileReportUtil.java               # Character Streams & Object Serialization
    │   └── InputValidator.java               # String & StringBuilder helpers
    │
    └── app/
        └── Main.java                         # Interactive Menu & Automated 5-Unit Viva Demo
```

---

## 4. How to Run the Project (Step-by-Step)

### Option A: Using VS Code (Easiest)
1. Open **VS Code**.
2. Click **File -> Open Folder...** and select the `CampusPlacementSystem` folder.
3. If prompted, install the free **Extension Pack for Java** (by Microsoft).
4. Navigate to `src/main/java/com/placement/app/Main.java`.
5. Click the **Run** button at the top right of the file.

### Option B: Using IntelliJ IDEA (Community Edition)
1. Open IntelliJ IDEA.
2. Select **Open** and choose the `CampusPlacementSystem` folder.
3. IntelliJ will automatically detect `pom.xml` and import dependencies.
4. Open `src/main/java/com/placement/app/Main.java` and click the green **Play** icon.

### Option C: Using Terminal / Command Prompt
* **Windows:**
  Double-click `run.bat` or open Command Prompt in the project folder and type:
  ```cmd
  mvn clean compile exec:java
  ```
* **Mac / Linux:**
  Open terminal in the project folder and run:
  ```bash
  chmod +x run.sh
  ./run.sh
  ```

---

## 5. How to Demonstrate to Your Teacher ("Mam") in 3 Minutes

When demonstrating your project, you have an automated shortcut built right in:

1. Launch the application.
2. From the main menu, press **Option 3** (`★ RUN FULL 5-UNIT VIVA DEMO`).
3. Point out each unit as it executes on screen:
   * **Unit I:** Point to `Student` inheriting fields with `this` and encapsulated getters/setters.
   * **Unit II:** Show the red `[ELIGIBILITY REJECTION]` message catching `CutoffNotMetException` when a student with 6.2 CGPA applies to Google.
   * **Unit III:** Show the instant list of qualified candidates filtered and sorted via the **Java 8 Stream API**.
   * **Unit IV:** Show the `[NotifyWorker-1]`, `[NotifyWorker-2]` threads running simultaneously to deliver interview tickets, followed by opening the generated `.csv` report file in the `exports/` folder.
   * **Unit V:** Explain how the SQLite JDBC driver connected and saved all data via `PreparedStatement`.

---

## 6. Top 5 Viva Questions & Model Answers

**Q1: Why did you use `PreparedStatement` instead of `Statement`?**  
> *"PreparedStatement pre-compiles the SQL query on the database side, making execution faster for repeated calls. More importantly, it uses parameterized placeholders (`?`) which completely prevent SQL Injection attacks."*

**Q2: What is the difference between `Comparable` and `Comparator` in your project?**  
> *"Comparable is implemented inside the `Student` class to define its natural sorting order (highest CGPA first). Comparator is implemented in `PlacementService` as a custom tie-breaker that first checks CGPA and then breaks ties by fewest backlogs."*

**Q3: How does your multithreading feature prevent race conditions?**  
> *"In `NotificationService`, multiple threads update the `totalDispatched` counter concurrently. We wrapped this update in a `synchronized(lock)` block, ensuring only one thread can modify the variable at any given millisecond."*

**Q4: Why did you create custom exceptions instead of using standard Java exceptions?**  
> *"Custom exceptions like `CutoffNotMetException` and `BacklogExceededException` make the business domain explicit and allow targeted catch blocks to display meaningful placement rejection reasons rather than a generic RuntimeException."*

**Q5: Why did you choose SQLite?**  
> *"SQLite is an embedded, serverless database that requires zero configuration, port setup, or background daemons. The entire relational database is persisted in a single `.db` file in our project folder."*

---

## 7. How to Upload to GitHub

Open Command Prompt or Terminal inside the `CampusPlacementSystem` directory:

```bash
# 1. Initialize git
git init

# 2. Stage all files
git add .

# 3. Create initial commit
git commit -m "Initial commit: Campus Placement Eligibility & Skill-Ranking System"

# 4. Create a new repository on https://github.com/new named 'campus-placement-system'
# Then link and push (replace YOUR_USERNAME):
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/campus-placement-system.git
git push -u origin main
```
