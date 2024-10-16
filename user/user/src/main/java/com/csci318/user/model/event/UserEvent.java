package com.csci318.user.model.event;

import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public abstract class UserEvent extends Event implements Serializable {
    private Long userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordSalt;

    public UserEvent() {}

    public UserEvent(String eventName, Long userID, String firstName, String lastName, String email, String password, String passwordSalt) {
        super(eventName);
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordSalt = passwordSalt;
    }

    // Getters
    public Long getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    // Setters
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
}
