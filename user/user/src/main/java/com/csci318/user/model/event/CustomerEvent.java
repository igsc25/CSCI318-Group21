package com.csci318.user.model.event;

import com.csci318.user.model.valueobj.Address;

import java.io.Serializable;
import java.util.List;

public class CustomerEvent extends UserEvent implements Serializable {
    private String phoneNumber;
    private List<Address> addresses;

    public CustomerEvent() {}

    public CustomerEvent(String eventName, Long userID, String firstName, String lastName, String email, String password, String passwordSalt, String phoneNumber, List<Address> addresses) {
        super(eventName, userID, firstName, lastName, email, password, passwordSalt);
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
    }

    // Getters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    // Setters
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
