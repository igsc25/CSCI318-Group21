package com.csci318.order.model.event;

public class OrderEvent extends Event {
    private Long cartID;
    private Boolean orderResult;

    public OrderEvent() {}

    public OrderEvent(String eventName, Long cartID, Boolean orderResult) {
        super(eventName);
        this.cartID = cartID;
        this.orderResult = orderResult;
    }

    // Getters
    public Long getCartID() {
        return cartID;
    }

    public Boolean getOrderResult() {
        return orderResult;
    }

    // Setters
    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public void setOrderResult(Boolean orderSucceed) {
        this.orderResult = orderSucceed;
    }
}
