package com.csci318.cart.model.event;

import com.csci318.cart.model.entity.Item;

import java.util.List;

public class CheckoutEvent extends Event {
    private Long cartID;
    private Long customerID;
    private Double totalPrice;
    private List<Item> items;

    public CheckoutEvent() {}

    public CheckoutEvent(String eventName, Long cartID, Long customerID, Double totalPrice, List<Item> items) {
        super(eventName);
        this.cartID = cartID;
        this.customerID = customerID;
    }

    // Getters
    public Long getCartID() {
        return cartID;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public List<Item> getItems() {
        return items;
    }

    // Setters
    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
