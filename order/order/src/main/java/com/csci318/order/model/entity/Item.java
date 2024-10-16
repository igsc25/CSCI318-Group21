package com.csci318.order.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemID;
    private String itemName;
    private String itemDescription;
    private Integer quantity;
    private Double pricePerItem;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    protected Item() {}

    protected Item(Long itemID, String itemName, String itemDescription, Integer quantity, Double pricePerItem, Order order) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.order = order;
    }

    // Getters
    public Long getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPricePerItem() {
        return pricePerItem;
    }

    public Order getOrder() {
        return order;
    }

    // Setters
    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPricePerItem(Double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // Factory method
    public static Item createItem(Long itemID, String itemName, String itemDescription, Integer quantity, Double pricePerItem, Order order) {
        return new Item(itemID, itemName, itemDescription, quantity, pricePerItem, order);
    }
}
