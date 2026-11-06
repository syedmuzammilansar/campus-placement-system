package com.placement.service;

import com.placement.model.Company;
import com.placement.model.Student;

/**
 * UNIT-II: Functional Interface for Lambda Expressions.
 * Contains single abstract method to test eligibility.
 */
@FunctionalInterface
public interface EligibilityChecker {
    boolean test(Student student, Company company);
}
