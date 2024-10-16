package com.csci318.product.service;

import com.csci318.product.model.entity.Product;
import com.csci318.product.producer.ProductEventProducer;
import com.csci318.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductEventProducer productEventProducer;

    public ProductService(ProductRepository productRepository, ProductEventProducer productEventProducer) {
        this.productRepository = productRepository;
        this.productEventProducer = productEventProducer;
    }

    // CRUD ----------

    public void createProduct(Product newProduct) {
        if (productRepository.existsById(newProduct.getProductID())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product with the same ID already exists");
        }

        Product product = Product.createProduct(
                newProduct.getProductName(),
                newProduct.getProductDescription(),
                newProduct.getBrand(),
                newProduct.getPrice(),
                newProduct.getAvailableQuantity(),
                newProduct.getCategories()
        );

        productRepository.save(product);
        productEventProducer.publishProductCreatedEvent(product);
    }

    public Product findProductByID(Long productID) {
        return productRepository.findById(productID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public List<Product> findProductsByCategory(List<String> categories) {
        // Return an empty list if no categories are provided
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }

        return findAllProducts().stream()
                .filter(product -> product.getCategories() != null && new HashSet<>(product.getCategories()).containsAll(categories))
                .collect(Collectors.toList());
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProductInformation(Long productID, Product newProduct) {
        Product product = findProductByID(productID);
        product.setProductName(newProduct.getProductName());
        product.setProductDescription(newProduct.getProductDescription());
        product.setBrand(newProduct.getBrand());
        product.setPrice(newProduct.getPrice());
        product.setAvailableQuantity(newProduct.getAvailableQuantity());
        product.setCategory(newProduct.getCategories());

        return productRepository.save(product);
    }

    public void deleteProduct(Long productID) {
        Product product = findProductByID(productID);
        productRepository.delete(product);
    }

    // Others ----------

    public void retrieveProduct(Long productID) {
        Product foundProduct = findProductByID(productID);
        productEventProducer.publishItemRetrievedEvent(foundProduct);
    }
}
