package com.csci318.order.consumer;

import com.csci318.order.model.OrderStatus;
import com.csci318.order.model.entity.Item;
import com.csci318.order.model.entity.Order;
import com.csci318.order.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class CartEventConsumer {
    @Autowired
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public CartEventConsumer(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    private void checkoutRequestedEvent(JsonNode jsonNode) {
        try {
            Long cartID = jsonNode.get("cartID").asLong();
            Long customerID = jsonNode.get("customerID").asLong();
            Double totalPrice = jsonNode.get("totalPrice").asDouble();

            Order order = Order.createOrder(cartID, customerID, null, totalPrice, OrderStatus.PENDING, LocalDateTime.now(), null, null, new ArrayList<>());

            // Convert the itemsNode JSON array into a List<Item>
            JsonNode items = jsonNode.get("items");
            List<Item> convertedItems = new ArrayList<>();
            for (JsonNode item : items) {
                Long itemID = item.get("itemID").asLong();
                String itemName = item.get("itemName").asText();
                String itemDescription = item.get("itemDescription").asText();
                Integer quantity = item.get("quantity").asInt();
                Double pricePerItem = item.get("pricePerItem").asDouble();

                convertedItems.add(Item.createItem(itemID, itemName, itemDescription, quantity, pricePerItem, order));
            }

            // Update the item list of order
            order.setItems(convertedItems);
            orderService.createOrder(order);
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
                    case "checkoutRequested":
                        checkoutRequestedEvent(jsonNode);
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
