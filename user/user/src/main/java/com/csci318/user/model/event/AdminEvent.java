package com.csci318.user.model.event;

import java.io.Serializable;

public class AdminEvent extends UserEvent implements Serializable {
    public AdminEvent() {}

    public AdminEvent(String eventName, Long userID, String firstName, String lastName, String email, String password, String passwordSalt) {
        super(eventName, userID, firstName, lastName, email, password, passwordSalt);
    }
}
