package com.csci318.product.consumer;

import com.csci318.product.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class cartEventConsumer {
    @Autowired
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public cartEventConsumer(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    private void itemAdditionRequestedEvent(JsonNode jsonNode) {
        try {
            Long itemID = jsonNode.get("itemID").asLong();
            productService.retrieveProduct(itemID);
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    @Bean
    public Consumer<String> cartEventListener() {
        return message -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(message);

                String eventName = jsonNode.get("eventName").asText();

                switch (eventName) {
                    case "itemAdditionRequested":
                        itemAdditionRequestedEvent(jsonNode);
                        break;
                    default:
                        System.out.println("Unknown event type: " + eventName);
                        break;
                }
            } catch (Exception exception) {
                exception.printStackTrace(System.out);
            }
        };
    }
}
