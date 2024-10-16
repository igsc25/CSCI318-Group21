package com.csci318.user.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class JsonWebTokenValidator {
    private static final String SECRET_KEY = "secret";

    private static String base64UrlDecode(String string) {
        return new String(Base64.getUrlDecoder().decode(string));
    }

    private static String generateSignature(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] signatureBytes = mac.doFinal(data.getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
    }

    private static Map<String, Object> extractClaims(String payload) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(base64UrlDecode(payload), new TypeReference<Map<String, Object>>() {});
    }

    private static long extractExpiration(Map<String, Object> claims) {
        Object exp = claims.get("exp");

        // Extract expiration time from the claims
        if (exp instanceof Integer) {
            return ((Integer) exp).longValue();
        } else if (exp instanceof Long) {
            return (Long) exp;
        } else {
            return -1;
        }
    }

    public static Map<String, Object> decodeJWT(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        // Extract and return the payload claims
        return extractClaims(parts[1]);
    }

    public static boolean validateJWT(String token) throws Exception {
        // Split the token into header, payload and signature
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            System.out.println("Invalid token format");
            return false;
        }

        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];

        // Validate signature
        String recreatedSignature = generateSignature(header + "." + payload);
        if (!signature.equals(recreatedSignature)) {
            System.out.println("Invalid signature");
            return false;
        }

        // Decode and extract claims from the payload
        Map<String, Object> claims = extractClaims(payload);

        // Extract the expiration time manually
        long exp = extractExpiration(claims);
        if (exp == -1) {
            System.out.println("Invalid expiration in token");
            return false;
        }

        long currentTime = System.currentTimeMillis() / 1000L;  // Current time in Unix format
        if (exp < currentTime) {
            System.out.println("Token has expired");
            return false; // Token has expired
        }

        // Token is valid
        return true;
    }
}
