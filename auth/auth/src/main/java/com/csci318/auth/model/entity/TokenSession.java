package com.csci318.auth.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class TokenSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionID;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean isRevoked = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date issuedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expiresAt;

    // Getters
    public Long getSessionID() {
        return sessionID;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsRevoked() {
        return isRevoked;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    // Setters
    public void setSessionID(Long sessionID) {
        this.sessionID = sessionID;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsRevoked(boolean isRevoked) {
        this.isRevoked = isRevoked;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
