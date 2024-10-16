package com.csci318.auth.model.event;

import java.io.Serializable;

public class RegistrationEvent extends Event implements Serializable {
    private String email;
    private String password;
    private String passwordSalt;
    private String role;

    public RegistrationEvent(String eventName, String email, String password, String passwordSalt, String role) {
        super(eventName);
        this.email = email;
        this.password = password;
        this.passwordSalt = passwordSalt;
        this.role = role;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
