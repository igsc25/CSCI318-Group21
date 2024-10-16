package com.csci318.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonWebTokenProvider {
    private static final String SECRET_KEY = "secret";
    private static final long EXPIRATION_IN_MILLISECONDS = 3600000; // 1 hour expiration

    private static String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String generateSignature(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(JsonWebTokenProvider.SECRET_KEY.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] signatureBytes = mac.doFinal(data.getBytes());
        return base64UrlEncode(signatureBytes);
    }

    public static String createJWT(String subject, String role) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        String headerJson = objectMapper.writeValueAsString(header);
        String headerBase64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", subject); // Subject (e.g., user identifier)
        payload.put("role", role);   // Role (e.g., "ADMIN" or "CUSTOMER")
        payload.put("iat", new Date().getTime() / 1000); // Issued at (current time in seconds)
        payload.put("exp", (new Date().getTime() + EXPIRATION_IN_MILLISECONDS) / 1000); // Expiration time in seconds
        String payloadJson = objectMapper.writeValueAsString(payload);
        String payloadBase64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

        String signature = generateSignature(headerBase64 + "." + payloadBase64);

        return headerBase64 + "." + payloadBase64 + "." + signature;
    }
}
