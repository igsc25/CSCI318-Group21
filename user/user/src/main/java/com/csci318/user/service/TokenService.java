package com.csci318.user.service;

import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private String token;

    public synchronized void setToken(String token) {
        this.token = token;
    }

    public synchronized String getToken() {
        return token;
    }

    public synchronized void clearToken() {
        this.token = null;
    }
}
