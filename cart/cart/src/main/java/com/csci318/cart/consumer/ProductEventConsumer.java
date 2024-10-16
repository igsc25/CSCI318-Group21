package com.csci318.cart.consumer;

import com.csci318.cart.model.entity.Item;
import com.csci318.cart.service.CartService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class ProductEventConsumer {
    @Autowired
    private final CartService cartService;
    private final ObjectMapper objectMapper;

    public ProductEventConsumer(CartService cartService, ObjectMapper objectMapper) {
        this.cartService = cartService;
        this.objectMapper = objectMapper;
    }

    private void productRetrievedEvent(JsonNode jsonNode) {
        try {
            Long itemID = jsonNode.get("productID").asLong();
            String itemName = jsonNode.get("productName").asText();
            String itemDescription = jsonNode.get("productDescription").asText();
            Double pricePerItem = jsonNode.get("price").asDouble();

            // Create Item from the event data (without cart association yet)
            Item item = Item.createItem(itemID, itemName, itemDescription, 1, pricePerItem, null);

            // Notify CartService that the product details have been retrieved
            cartService.completeItem(item);
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    private void orderSucceedEvent(JsonNode jsonNode) {
        try {
            Long cartID = jsonNode.get("cartID").asLong();
            Boolean orderResult = jsonNode.get("orderResult").asBoolean();

            if (orderResult) {
                cartService.clearCart(cartID);
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    @Bean
    public Consumer<String> productEventListener() {
        return message -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(message);

                String eventName = jsonNode.get("eventName").asText();

                switch (eventName) {
                    case "productRetrieved":
                        productRetrievedEvent(jsonNode);
                        break;
                    case "orderSucceed":
                        orderSucceedEvent(jsonNode);
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
