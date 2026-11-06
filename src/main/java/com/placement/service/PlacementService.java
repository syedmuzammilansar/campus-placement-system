package com.placement.service;

import com.placement.exception.BacklogExceededException;
import com.placement.exception.CutoffNotMetException;
import com.placement.model.Company;
import com.placement.model.Student;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UNIT-II & UNIT-III: Core Business Logic.
 * Covers:
 * - Collections Framework (List, ArrayList, Map, Set)
 * - Comparable and Custom Comparator
 * - Java 8 Stream API (filter, map, sorted, collect)
 * - Lambda Expressions with Functional Interface
 * - Custom Exception handling with throw/throws
 */
public class PlacementService {

    // Unit-II: Lambda Expression implementing EligibilityChecker functional interface
    private final EligibilityChecker defaultChecker = (student, company) -> {
        boolean branchMatch = company.getEligibleBranches().equalsIgnoreCase("ALL") ||
                Arrays.stream(company.getEligibleBranches().split(","))
                        .map(String::trim)
                        .anyMatch(b -> b.equalsIgnoreCase(student.getBranch()));

        return student.getCgpa() >= company.getMinCgpa() &&
                student.getBacklogs() <= company.getMaxBacklogs() &&
                branchMatch;
    };

    /**
     * Validates eligibility and throws custom checked exceptions if criteria not met.
     * Demonstrates 'throws' and 'throw' (Unit-II).
     */
    public void validateEligibility(Student student, Company company)
            throws CutoffNotMetException, BacklogExceededException {

        if (student.getCgpa() < company.getMinCgpa()) {
            throw new CutoffNotMetException(String.format(
                    "Eligibility Failed: Student CGPA (%.2f) is below %s cutoff of %.2f",
                    student.getCgpa(), company.getName(), company.getMinCgpa()));
        }

        if (student.getBacklogs() > company.getMaxBacklogs()) {
            throw new BacklogExceededException(String.format(
                    "Eligibility Failed: Student has %d active backlogs. %s permits maximum %d.",
                    student.getBacklogs(), company.getName(), company.getMaxBacklogs()));
        }
    }

    /**
     * UNIT-III: Java Stream API (filter, map, sorted, collect).
     * Filters eligible students for a company drive in one declarative stream pipeline.
     */
    public List<Student> filterEligibleStudents(List<Student> allStudents, Company company) {
        return allStudents.stream()
                // 1. Stream filter with lambda
                .filter(student -> defaultChecker.test(student, company))
                // 2. Stream sorted using Custom Comparator (Highest CGPA first, then lowest backlogs)
                .sorted(getMeritComparator())
                // 3. Stream collect to List
                .collect(Collectors.toList());
    }

    /**
     * UNIT-III: Custom Comparator implementation.
     * Compares students first by CGPA descending, and breaks ties by fewest backlogs.
     */
    public Comparator<Student> getMeritComparator() {
        return (s1, s2) -> {
            int cgpaCompare = Double.compare(s2.getCgpa(), s1.getCgpa());
            if (cgpaCompare != 0) {
                return cgpaCompare;
            }
            return Integer.compare(s1.getBacklogs(), s2.getBacklogs());
        };
    }

    /**
     * UNIT-III: Map and Stream mapping example.
     * Groups count of students by Branch using Map interface.
     */
    public Map<String, Long> getStudentCountByBranch(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getBranch, Collectors.counting()));
    }
}
