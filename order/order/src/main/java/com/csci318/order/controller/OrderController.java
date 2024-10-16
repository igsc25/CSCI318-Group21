package com.csci318.order.controller;

import com.csci318.order.controller.dto.ItemDTO;
import com.csci318.order.controller.dto.OrderDTO;
import com.csci318.order.model.entity.Item;
import com.csci318.order.model.entity.Order;
import com.csci318.order.model.valueobj.BillingAddress;
import com.csci318.order.model.valueobj.ShippingAddress;
import com.csci318.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Helpers ----------

    private OrderDTO toDTO(Order order) {
        List<ItemDTO> itemDTOs = new ArrayList<ItemDTO>();

        if (order == null) {
            return null;
        }

        for (Item item: order.getItems()) {
            ItemDTO itemDTO = new ItemDTO();

            itemDTO.setItemID(item.getItemID());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemDescription(item.getItemDescription());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPricePerItem());

            itemDTOs.add(itemDTO);
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderID(order.getOrderID());
        orderDTO.setCartID(order.getCartID());
        orderDTO.setCustomerID(order.getCustomerID());
        orderDTO.setOrderNumber(order.getOrderNumber());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPlacedDate(order.getPlacedDate());
        orderDTO.setShippingAddress(order.getShippingAddress());
        orderDTO.setBillingAddress(order.getBillingAddress());
        orderDTO.setItems(itemDTOs);

        return orderDTO;
    }

    private Order toEntity(OrderDTO orderDTO) {
        List<Item> items = new ArrayList<>();

        if (orderDTO == null) {
            return null;
        }

        Order order = Order.createOrder(
                orderDTO.getCartID(),
                orderDTO.getCustomerID(),
                orderDTO.getOrderNumber(),
                orderDTO.getTotalPrice(),
                orderDTO.getStatus(),
                orderDTO.getPlacedDate(),
                orderDTO.getShippingAddress(),
                orderDTO.getBillingAddress(),
                new ArrayList<>()
        );

        for (ItemDTO itemDTO : orderDTO.getItems()) {
            Item item = Item.createItem(
                    itemDTO.getItemID(),
                    itemDTO.getItemName(),
                    itemDTO.getItemDescription(),
                    itemDTO.getQuantity(),
                    itemDTO.getPrice(),
                    order
            );

            items.add(item);
        }

        // Associate the items with the Order
        order.setItems(items);

        return order;
    }

    // CRUD ----------

    // Order creation handled by cart service checkout endpoint

    @GetMapping("/{orderID}")
    OrderDTO findOrderByID(@PathVariable Long orderID) {
        Order order = orderService.findOrderByID(orderID);
        return toDTO(order);
    }

    @PutMapping("/{orderID}")
    OrderDTO updateOrderInformation(@PathVariable Long orderID, @Valid @RequestBody OrderDTO newOrder) {
        Order order = orderService.updateOrderInformation(orderID, Objects.requireNonNull(toEntity(newOrder)));
        return toDTO(order);
    }

    @DeleteMapping("/{orderID}")
    void deleteOrder(@PathVariable Long orderID) {
        orderService.deleteOrder(orderID);
    }

    // Logics ----------

    @GetMapping("find/{orderNumber}")
    OrderDTO findOrderByOrderNumber(@PathVariable String orderNumber) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        return toDTO(order);
    }

    @PutMapping("cancel/{orderNumber}")
    void cancelOrder(@PathVariable String orderNumber) {
        orderService.cancelOrder(orderNumber);
    }

    @PostMapping("/{orderNumber}/confirm")
    void proceedPayment(@PathVariable String orderNumber, @RequestBody ShippingAddress shippingAddress, @RequestBody BillingAddress billingShippingAddress) {
        orderService.proceedPayment(orderNumber, shippingAddress, billingShippingAddress);
    }
}
