package com.csci318.user.model.event;

import java.io.Serializable;

public class LoginEvent extends Event implements Serializable {
    private String email;
    private String inputPassword;
    private String databasePassword;
    private String passwordSalt;
    private String role;

    public LoginEvent(String eventName, String email, String inputPassword, String databasePassword, String passwordSalt, String role) {
        super(eventName);
        this.email = email;
        this.inputPassword = inputPassword;
        this.databasePassword = databasePassword;
        this.passwordSalt = passwordSalt;
        this.role = role;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public String getDatabasePassword() {
        return databasePassword;
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

    public void setInputPassword(String password) {
        this.inputPassword = password;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
