package com.placement.exception;

/**
 * UNIT-II: Custom Checked Exception thrown when applying twice to the same drive.
 */
public class DuplicateApplicationException extends Exception {
    private static final long serialVersionUID = 1L;

    public DuplicateApplicationException(String message) {
        super(message);
    }
}
