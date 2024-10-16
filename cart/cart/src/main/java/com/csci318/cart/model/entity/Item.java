package com.csci318.cart.model.entity;

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
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    protected Item() {}

    protected Item(Long itemID, String itemName, String itemDescription, Integer quantity, Double pricePerItem, Cart cart) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.cart = cart;
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

    public Cart getCart() {
        return cart;
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

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    // Factory method
    public static Item createItem(Long itemID, String itemName, String itemDescription, Integer quantity, Double pricePerItem, Cart cart) {
        return new Item(itemID, itemName, itemDescription, quantity, pricePerItem, cart);
    }
}
