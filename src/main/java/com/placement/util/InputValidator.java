package com.placement.util;

/**
 * UNIT-I: String and StringBuilder utilities, data validation.
 */
public class InputValidator {

    public static boolean isValidRollNo(String roll) {
        return roll != null && roll.trim().matches("^[A-Za-z0-9]{5,12}$");
    }

    public static boolean isValidCgpa(double cgpa) {
        return cgpa >= 0.0 && cgpa <= 10.0;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Demonstrates StringBuilder for dynamic banner creation (Unit-I).
     */
    public static String buildBanner(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("======================================================================
");
        sb.append(String.format("   %-62s
", title));
        sb.append("======================================================================");
        return sb.toString();
    }
}
