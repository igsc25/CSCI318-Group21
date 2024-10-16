package com.csci318.order.model.entity;

import com.csci318.order.model.OrderStatus;
import com.csci318.order.model.valueobj.BillingAddress;
import com.csci318.order.model.valueobj.ShippingAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_order")
public class Order extends AbstractAggregateRoot<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;
    private Long cartID;
    private Long customerID;
    private String orderNumber;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime placedDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "streetNumber", column = @Column(name = "shipping_street_number")),
            @AttributeOverride(name = "streetName", column = @Column(name = "shipping_street_name")),
            @AttributeOverride(name = "district", column = @Column(name = "shipping_district")),
            @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
            @AttributeOverride(name = "state", column = @Column(name = "shipping_state")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "shipping_zipcode")),
            @AttributeOverride(name = "country", column = @Column(name = "shipping_country"))
    })
    private ShippingAddress shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "streetNumber", column = @Column(name = "billing_street_number")),
            @AttributeOverride(name = "streetName", column = @Column(name = "billing_street_name")),
            @AttributeOverride(name = "district", column = @Column(name = "billing_district")),
            @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
            @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "billing_zipcode")),
            @AttributeOverride(name = "country", column = @Column(name = "billing_country"))
    })
    private BillingAddress billingShippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();

    protected Order() {}

    protected Order(Long cartID, Long customerID, String orderNumber, Double totalPrice, OrderStatus status, LocalDateTime placedDate, ShippingAddress shippingAddress, BillingAddress billingShippingAddress, List<Item> items) {
        // Let JPA generate IDs
        this.cartID = cartID;
        this.customerID = customerID;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.status = status;
        this.placedDate = placedDate;
        this.shippingAddress = shippingAddress;
        this.billingShippingAddress = billingShippingAddress;
        this.items = items;
    }

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

    public List<Item> getItems() {
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

    public void setItems(List<Item> items) {
        this.items = items;
    }

    // Factory method
    public static Order createOrder(Long cartID, Long customerID, String orderNumber, Double totalPrice, OrderStatus status, LocalDateTime placedDate, ShippingAddress shippingAddress, BillingAddress billingShippingAddress, List<Item> items) {
        Order order = new Order(cartID, customerID, orderNumber, totalPrice, status, placedDate, shippingAddress, billingShippingAddress, items);

        // Set order reference in items
        items.forEach(item -> item.setOrder(order));

        return order;
    }

    // Business logics ----------

    public void cancelOrder(String orderNumber) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order cannot be canceled as it is not in 'PENDING' status");
        }

        this.status = OrderStatus.CANCELLED;
    }
}
