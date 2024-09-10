package org.example.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper {

    // Method to validate customer attributes
    public static boolean validateCustomer(String name, String email, String phone, String address) {
        // Validate name (not null and not empty)
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Validation failed: Name cannot be empty");
            return false;
        }

        // Validate email using a simple regex pattern
        if (!isValidEmail(email)) {
            System.out.println("Validation failed: Invalid email format");
            return false;
        }

        // Validate phone (must be digits and not empty)
        if (!isValidPhoneNumber(phone)) {
            System.out.println("Validation failed: Invalid phone number");
            return false;
        }

        // Address can be optional but should not be blank if provided
        if (address != null && address.trim().isEmpty()) {
            System.out.println("Validation failed: Address cannot be empty if provided");
            return false;
        }

        // All validations passed
        return true;
    }

    // Helper method to validate email format
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPhoneNumber(String phone) {
        // Allow phone numbers that start with + and include digits, dashes (-), or spaces
        String phoneRegex = "^\\+?[0-9\\-\\s]+$";
        Pattern pattern = Pattern.compile(phoneRegex);
        if (phone == null) {
            return false;
        }
        return pattern.matcher(phone).matches();
    }


}
