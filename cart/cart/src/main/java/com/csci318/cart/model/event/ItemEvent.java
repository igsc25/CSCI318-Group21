package com.csci318.cart.model.event;

public class ItemEvent extends Event {
    private Long itemID;

    public ItemEvent() {}

    public ItemEvent(String eventName, Long itemID) {
        super(eventName);
        this.itemID = itemID;
    }

    // Getters
    public Long getItemID() {
        return itemID;
    }

    // Setters
    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }
}
