package com.csci318.cart.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart extends AbstractAggregateRoot<Cart> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartID;
    private Long customerID;
    private Double totalPrice = 0.0;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();

    protected Cart() {}

    protected Cart(Long customerID, Double totalPrice, List<Item> items) {
        // Let JPA generate IDs
        this.customerID = customerID;
        this.totalPrice = totalPrice;
        this.items = items;
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

    // Factory method
    public static Cart createCart(Long customerID, Double totalPrice, List<Item> items) {
        Cart cart = new Cart(customerID, totalPrice, items);

        // Set cart reference in items
        items.forEach(item -> item.setCart(cart));

        return cart;
    }

    // Business logics ----------

    private void recalculateTotalPrice() {
        this.totalPrice = items.stream()
                .mapToDouble(item -> item.getPricePerItem() * item.getQuantity())
                .sum();
    }

    public void addItem(Item newItem) {
        for (Item existingItem : items) {
            if (existingItem.getItemID().equals(newItem.getItemID())) {
                // If the item already exists, increment its quantity
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                recalculateTotalPrice();

                return;
            }
        }

        // If the item is new, set its quantity to 1 and add it to the cart
        newItem.setQuantity(1);
        newItem.setCart(this);
        items.add(newItem);

        // Recalculate the total price
        recalculateTotalPrice();
    }

    public void removeItem(Item currentItem) {
        for (Item existingItem : items) {
            if (existingItem.getItemID().equals(currentItem.getItemID())) {
                // Decrease quantity if greater than 1
                if (existingItem.getQuantity() > 1) {
                    existingItem.setQuantity(existingItem.getQuantity() - 1);
                } else {
                    // Remove the item entirely if quantity reaches 1
                    items.remove(existingItem);
                    existingItem.setCart(null);  // Break the reference
                }

                recalculateTotalPrice();
                return;
            }
        }

        // If the item was not found, do nothing (or throw an exception if needed)
        throw new IllegalArgumentException("Item not found in cart");
    }

    public void clearCart() {
        items.clear();
        recalculateTotalPrice();
    }
}
