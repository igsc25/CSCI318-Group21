package com.csci318.auth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHashing {
    private static final int SALT_LENGTH = 16;
    private static final int HASH_ITERATIONS = 10000;

    // Generates a random salt
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    // Hashes the password with the given salt and returns the hashed value
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] saltedPassword = (salt + password).getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(saltedPassword);

            for (int i = 0; i < HASH_ITERATIONS; i++) {
                hash = digest.digest(hash);  // Key stretching
            }

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("Error occurred during password hashing", exception);
        }
    }

    // Verify a password by rehashing the input and comparing it with the stored hash
    public static boolean checkPassword(String inputPassword, String storedPassword, String salt) {
        String hashedInputPassword = hashPassword(inputPassword, salt);
        return hashedInputPassword.equals(storedPassword);
    }
}
