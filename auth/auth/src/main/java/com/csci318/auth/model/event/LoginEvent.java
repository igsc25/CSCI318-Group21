package com.csci318.auth.model.event;

import java.io.Serializable;

public class LoginEvent extends Event implements Serializable {
    private String email;
    private String password;
    private String role;

    public LoginEvent(String eventName, String email, String password, String role) {
        super(eventName);
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public void setRole(String role) {
        this.role = role;
    }
}
