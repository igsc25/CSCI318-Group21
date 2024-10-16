package com.csci318.product.producer;

import com.csci318.product.model.entity.Product;
import com.csci318.product.model.event.ProductEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class ProductEventProducer {
    private final StreamBridge streamBridge;

    @Autowired
    public ProductEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    private ProductEvent toProductEvent(Product product, String eventName) {
        ProductEvent productEvent = new ProductEvent();

        productEvent.setEventName(eventName);
        productEvent.setProductID(product.getProductID());
        productEvent.setProductName(product.getProductName());
        productEvent.setProductDescription(product.getProductDescription());
        productEvent.setBrand(product.getBrand());
        productEvent.setPrice(product.getPrice());
        productEvent.setAvailableQuantity(product.getAvailableQuantity());
        productEvent.setCategories(product.getCategories());

        return productEvent;
    }

    public void publishProductCreatedEvent(Product product) {
        ProductEvent productEvent = toProductEvent(product, "productCreated");
        streamBridge.send("product-out-0", productEvent);
    }

    public void publishItemRetrievedEvent(Product product) {
        ProductEvent productEvent = toProductEvent(product, "productRetrieved");
        streamBridge.send("product-out-0", productEvent);
    }
}
