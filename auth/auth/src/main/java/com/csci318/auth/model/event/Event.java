package com.csci318.auth.model.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public abstract class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventID;
    private String eventName;

    public Event(String eventName) {
        this.eventName = eventName;
    }

    // Getters
    public Long getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    // Setters
    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
