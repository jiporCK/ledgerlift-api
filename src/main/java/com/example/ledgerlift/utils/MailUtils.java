package com.example.ledgerlift.utils;

import java.security.SecureRandom;

public class MailUtils {

    // Email content constants
    public static final String WELCOME_SIGNING_UP = "Thanks for registering on our platform!";
    public static final String WELCOME_ORGANIZATION = "Welcome to our fundraising platform. Start managing your campaigns today!";
    public static final String PASSWORD_RESET_SUBJECT = "Password Reset Request";
    public static final String PASSWORD_RESET_BODY = "You have requested to reset your password. Use the provided token to proceed.";
    public static final String ACCOUNT_UPDATED = "Your account details have been updated successfully.";

    // Method to generate a numeric token
    public static String generateDigitsToken() {
        SecureRandom random = new SecureRandom();
        int tokenLength = 6;
        StringBuilder token = new StringBuilder(tokenLength);

        for (int i = 0; i < tokenLength; i++) {
            int digit = random.nextInt(10);
            token.append(digit);
        }
        return token.toString();
    }

    public enum AccountUpdateType {
        PROFILE_IMAGE,
        CHANGE_PASSWORD
    }
}

