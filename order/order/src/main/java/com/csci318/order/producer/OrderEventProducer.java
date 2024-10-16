package com.csci318.order.producer;

import com.csci318.order.model.event.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private final StreamBridge streamBridge;

    @Autowired
    public OrderEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishOrderResult(OrderEvent orderEvent) {
        streamBridge.send("order-out-0", orderEvent);
    }
}
