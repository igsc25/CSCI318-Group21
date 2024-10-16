package com.csci318.auth.controller;

import com.csci318.auth.service.TokenSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token-session")
public class TokenSessionController {
    private final TokenSessionService tokenSessionService;

    @Autowired
    public TokenSessionController(TokenSessionService tokenSessionService) {
        this.tokenSessionService = tokenSessionService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password, @RequestParam String role) {
        tokenSessionService.register(email, password, role);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, @RequestParam String role) {
        String token = tokenSessionService.login(email, password, role);
        if (token != null) {
            return ResponseEntity.ok("Login succeed, your token has been saved in the browser cookie, and it is: " + "\n" + token);
        } else {
            return ResponseEntity.status(401).body("Login failed: Invalid credentials");
        }
    }
}
