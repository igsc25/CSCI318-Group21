package com.csci318.auth.model.event;

public class TokenRetrievedEvent extends Event {
    private String token;

    public TokenRetrievedEvent(String eventName, String token) {
        super(eventName);
        this.token = token;
    }

    // Getters
    public String getToken() {
        return token;
    }

    // Setters
    public void setToken(String token) {
        this.token = token;
    }
}
