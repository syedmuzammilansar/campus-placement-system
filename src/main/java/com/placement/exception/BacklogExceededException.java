package com.placement.exception;

/**
 * UNIT-II: Custom Checked Exception thrown when student has more backlogs than allowed.
 */
public class BacklogExceededException extends Exception {
    private static final long serialVersionUID = 1L;

    public BacklogExceededException(String message) {
        super(message);
    }
}
