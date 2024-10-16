package com.csci318.order.controller.dto;

import com.csci318.order.model.OrderStatus;
import com.csci318.order.model.valueobj.BillingAddress;
import com.csci318.order.model.valueobj.ShippingAddress;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long orderID;
    private Long cartID;
    private Long customerID;
    private String orderNumber;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime placedDate;
    private ShippingAddress shippingAddress;
    private BillingAddress billingShippingAddress;
    private List<ItemDTO> items = new ArrayList<>();

    // Getters
    public Long getOrderID() {
        return orderID;
    }

    public Long getCartID() {
        return cartID;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getPlacedDate() {
        return placedDate;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public BillingAddress getBillingAddress() {
        return billingShippingAddress;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    // Setters
    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setPlacedDate(LocalDateTime placedDate) {
        this.placedDate = placedDate;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBillingAddress(BillingAddress billingShippingAddress) {
        this.billingShippingAddress = billingShippingAddress;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
