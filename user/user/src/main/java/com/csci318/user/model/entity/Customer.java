package com.csci318.user.model.entity;

import com.csci318.user.model.valueobj.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends EndUser {
    @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number format")
    private String phoneNumber;

    @ElementCollection
    private List<Address> addresses;

    protected Customer() {}

    protected Customer(String firstName, String lastName, String email, String password, String passwordSalt, String phoneNumber, List<Address> addresses) {
        super(firstName, lastName, email, password, passwordSalt);
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

    // Factory methods
    public static Customer createUser(String firstName, String lastName, String email, String password, String passwordSalt, String phoneNumber, List<Address> addresses) {
        return new Customer(firstName, lastName, email, password, passwordSalt, phoneNumber, addresses);
    }
}
