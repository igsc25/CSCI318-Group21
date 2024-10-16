package com.csci318.product.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.List;

@Entity
public class Product extends AbstractAggregateRoot<Product> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;
    private String productName;
    private String productDescription;
    private String brand;
    private Double price;
    private Integer availableQuantity;
    private List<String> categories;

    protected Product() {}

    protected Product(String productName, String productDescription, String brand, Double price, Integer availableQuantity, List<String> categories) {
        // Let JPA generate IDs
        this.productName = productName;
        this.productDescription = productDescription;
        this.brand = brand;
        this.price = price;
        this.availableQuantity = availableQuantity;
        this.categories = categories;
    }

    // Getters
    public Long getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getBrand() {
        return brand;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public List<String> getCategories() {
        return categories;
    }

    // Setters
    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setCategory(List<String> categories) {
        this.categories = categories;
    }

    // Factory method
    public static Product createProduct(String productName, String productDescription, String brand, Double price, Integer availableQuantity, List<String> categories) {
        return new Product(productName, productDescription, brand, price, availableQuantity, categories);
    }
}
