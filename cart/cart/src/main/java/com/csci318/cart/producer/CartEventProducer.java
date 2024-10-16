package com.csci318.cart.producer;

import com.csci318.cart.model.entity.Item;
import com.csci318.cart.model.event.CheckoutEvent;
import com.csci318.cart.model.event.ItemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class CartEventProducer {
    private final StreamBridge streamBridge;

    @Autowired
    public CartEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    private ItemEvent toItemEvent(Item item, String eventName) {
        ItemEvent itemEvent = new ItemEvent();

        itemEvent.setEventName(eventName);
        itemEvent.setItemID(item.getItemID());

        return itemEvent;
    }

    public void publishItemAdditionEvent(Item item) {
        ItemEvent itemEvent = toItemEvent(item, "itemAdditionRequested");
        streamBridge.send("cart-out-0", itemEvent);
    }

    public void publishCheckoutEvent(CheckoutEvent checkoutEvent) {
        streamBridge.send("cart-out-0", checkoutEvent);
    }
}
