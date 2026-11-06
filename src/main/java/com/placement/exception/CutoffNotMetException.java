package com.placement.exception;

/**
 * UNIT-II: Custom Checked Exception thrown when student CGPA is below cutoff.
 */
public class CutoffNotMetException extends Exception {
    private static final long serialVersionUID = 1L;

    public CutoffNotMetException(String message) {
        super(message);
    }
}
