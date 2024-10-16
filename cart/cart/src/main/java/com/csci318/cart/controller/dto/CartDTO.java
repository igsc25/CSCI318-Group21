package com.csci318.cart.controller.dto;

import com.csci318.cart.model.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private Long cartID;
    private Long customerID;
    private Double totalPrice;
    private List<ItemDTO> items = new ArrayList<>();

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

    public List<ItemDTO> getItems() {
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

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
