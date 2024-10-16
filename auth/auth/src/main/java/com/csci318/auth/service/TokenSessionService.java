package com.csci318.auth.service;

import com.csci318.auth.model.event.LoginEvent;
import com.csci318.auth.model.event.RegistrationEvent;
import com.csci318.auth.model.event.TokenRetrievedEvent;
import com.csci318.auth.producer.AuthEventProducer;
import com.csci318.auth.util.JsonWebTokenProvider;
import com.csci318.auth.util.PasswordHashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TokenSessionService {
    private final AuthEventProducer authEventProducer;
    private final Map<String, CompletableFuture<String>> jsonWebTokenFutures = new ConcurrentHashMap<>();

    @Autowired
    public TokenSessionService(AuthEventProducer authEventProducer) {
        this.authEventProducer = authEventProducer;
    }

    // Async methods ----------

    // Call when password comparison result comes back
    public void completeLogin(String email, boolean passwordMatched, String role) {
        CompletableFuture<String> jwtFuture = jsonWebTokenFutures.get(email);

        if (jwtFuture != null) {
            if (passwordMatched) {
                try {
                    String createdJWT = JsonWebTokenProvider.createJWT(email, role);
                    jwtFuture.complete(createdJWT);
                } catch (Exception exception) {
                    jwtFuture.completeExceptionally(exception);
                }
            } else {
                jwtFuture.complete(null);
            }
        }
    }

    // Logic methods ----------

    public void register(String email, String password, String role) {
        String passwordSalt = PasswordHashing.generateSalt();
        String hashedPassword = PasswordHashing.hashPassword(password, passwordSalt);

        RegistrationEvent registrationEvent = new RegistrationEvent("userRegistrationRequested", email, hashedPassword, passwordSalt, role);
        authEventProducer.publishRegistrationEvent(registrationEvent);
    }

    public String login(String email, String password, String role) {
        CompletableFuture<String> jsonWebTokenFuture = new CompletableFuture<>();
        jsonWebTokenFutures.put(email, jsonWebTokenFuture);

        LoginEvent loginEvent = new LoginEvent("userLoginRequested", email, password, role);
        authEventProducer.publishLoginEvent(loginEvent);

        try {
            // Wait for the password comparison result (with a timeout, e.g., 10 seconds)
            String token = jsonWebTokenFuture.get(10, TimeUnit.SECONDS);  // Token generated here

            if (token != null) {
                TokenRetrievedEvent tokenRetrievedEvent = new TokenRetrievedEvent("tokenRetrieved", token);
                authEventProducer.publishTokenRetrievedEvent(tokenRetrievedEvent);
            }

            return token;
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
            return null;  // Return null if the login process failed
        } finally {
            // Clean up the future map
            jsonWebTokenFutures.remove(email);
        }
    }
}
