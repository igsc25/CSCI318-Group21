package com.csci318.order.model.valueobj;

import jakarta.persistence.Embeddable;

@Embeddable
public class BillingAddress {
    private Integer streetNumber;
    private String streetName;
    private String district;
    private String city;
    private String state;
    private Integer zipcode;
    private String country;

    // Getters
    public Integer getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public String getCountry() {
        return country;
    }

    // Setters
    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
