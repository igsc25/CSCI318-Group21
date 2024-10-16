package com.csci318.user.model.event;

import java.io.Serializable;

public class RegistrationEvent extends Event implements Serializable {
    private String email;

    public RegistrationEvent(String eventName, String email) {
        super(eventName);
        this.email = email;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }
}