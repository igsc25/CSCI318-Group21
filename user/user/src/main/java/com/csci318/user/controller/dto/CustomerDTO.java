package com.csci318.user.controller.dto;

import com.csci318.user.model.valueobj.Address;

import java.util.List;

public class CustomerDTO extends UserDTO {
    private String phoneNumber;
    private List<Address> addresses;

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
