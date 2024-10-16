package com.csci318.order.service;

import com.csci318.order.model.OrderStatus;
import com.csci318.order.model.entity.Order;
import com.csci318.order.model.event.OrderEvent;
import com.csci318.order.model.valueobj.BillingAddress;
import com.csci318.order.model.valueobj.ShippingAddress;
import com.csci318.order.producer.OrderEventProducer;
import com.csci318.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    // Helpers ----------

    private String generateOrderNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000;
        return "#" + randomNumber;
    }

    private Boolean generateRandomPaymentResult() {
        Random random = new Random();
        return random.nextBoolean();
    }

    // CRUD ----------

    public void createOrder(Order newOrder) {
        Order order = Order.createOrder(
                newOrder.getCartID(),
                newOrder.getCustomerID(),
                generateOrderNumber(),
                newOrder.getTotalPrice(),
                newOrder.getStatus(),
                newOrder.getPlacedDate(),
                newOrder.getShippingAddress(),
                newOrder.getBillingAddress(),
                newOrder.getItems()
        );

        orderRepository.save(order);
    }

    public Order findOrderByID(Long orderID) {
        return orderRepository.findById(orderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public Order findOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public Order updateOrderInformation(Long orderID, Order newOrder) {
        Order order = findOrderByID(orderID);
        order.setCustomerID(newOrder.getCustomerID());
        order.setOrderNumber(newOrder.getOrderNumber());
        order.setTotalPrice(newOrder.getTotalPrice());
        order.setStatus(newOrder.getStatus());
        order.setPlacedDate(newOrder.getPlacedDate());
        order.setShippingAddress(newOrder.getShippingAddress());
        order.setBillingAddress(newOrder.getBillingAddress());
        order.setItems(newOrder.getItems());

        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderID) {
        Order order = findOrderByID(orderID);
        orderRepository.delete(order);
    }

    // Logics ----------

    public void cancelOrder(String orderNumber) {
        Order order = findOrderByOrderNumber(orderNumber);
        order.cancelOrder(order.getOrderNumber());
        orderRepository.save(order);
    }

    public void proceedPayment(String orderNumber, ShippingAddress shippingAddress, BillingAddress billingShippingAddress) {
        Order order = findOrderByOrderNumber(orderNumber);

        // Simulate payment processing - random result
        boolean paymentResult = generateRandomPaymentResult();

        if (paymentResult) {
            // Update order status and addresses
            order.setStatus(OrderStatus.PAID);
            order.setShippingAddress(shippingAddress);
            order.setBillingAddress(billingShippingAddress);
            orderRepository.save(order);

            OrderEvent orderEvent = new OrderEvent("orderSucceed", order.getCartID(), true);
            orderEventProducer.publishOrderResult(orderEvent);
        } else {
            OrderEvent orderEvent = new OrderEvent("orderFailed", order.getCartID(), false);
            orderEventProducer.publishOrderResult(orderEvent);

            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment failed");
        }
    }
}
