package com.csci318.product.controller.dto;

import java.util.List;

public class ProductDTO {
    private Long productID;
    private String productName;
    private String productDescription;
    private String brand;
    private Double price;
    private Integer availableQuantity;
    private List<String> category;

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

    public List<String> getCategory() {
        return category;
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

    public void setCategories(List<String> category) {
        this.category = category;
    }
}
