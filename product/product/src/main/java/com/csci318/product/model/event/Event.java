package com.csci318.product.model.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public abstract class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventID;
    private String eventName;

    public Event() {}

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
