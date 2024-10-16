package com.csci318.cart.service;

import com.csci318.cart.model.entity.Cart;
import com.csci318.cart.model.entity.Item;
import com.csci318.cart.model.event.CheckoutEvent;
import com.csci318.cart.producer.CartEventProducer;
import com.csci318.cart.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartEventProducer cartEventProducer;
    private final Map<Long, CompletableFuture<Item>> futureItems = new ConcurrentHashMap<>();

    // AtomicInteger for thread-safe autoGuestID generation
    private final AtomicInteger autoGuestID = new AtomicInteger(-1);

    public CartService(CartRepository cartRepository, CartEventProducer cartEventProducer) {
        this.cartRepository = cartRepository;
        this.cartEventProducer = cartEventProducer;
    }

    // Helpers ----------

    private Long generateGuestCustomerID() {
        // Decrease the autoGuestID for every new guest
        return (long) autoGuestID.getAndDecrement();
    }

    // Async methods ----------

    // Call when item result comes back
    public void completeItem(Item item) {
        CompletableFuture<Item> futureItem = futureItems.remove(item.getItemID());

        if (futureItem != null) {
            futureItem.complete(item);
        } else {
            System.out.println("No future found for item ID: " + item.getItemID());
        }
    }

    // CRUD ----------

    public void createCart(Cart newCart) {
        if (newCart.getCustomerID() == null) {
            // Customer is a guest
            newCart.setCustomerID(generateGuestCustomerID());
        }

        Cart cart = Cart.createCart(
          newCart.getCustomerID(),
          newCart.getTotalPrice(),
          newCart.getItems()
        );

        cartRepository.save(cart);
    }

    public Cart findCartByID(Long cartID) {
        return cartRepository.findById(cartID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
    }

    public Cart updateCartInformation(Long cartID, Cart newCart) {
        Cart cart = findCartByID(cartID);

        cart.setCustomerID(newCart.getCustomerID());
        cart.setTotalPrice(newCart.getTotalPrice());
        cart.setItems(newCart.getItems());

        return cartRepository.save(cart);
    }

    public void deleteCart(Long cartID) {
        Cart cart = findCartByID(cartID);
        cartRepository.delete(cart);
    }

    // Others ----------

    public void addItemToCart(Long cartID, Item newItem) {
        Cart cart = findCartByID(cartID);

        // Create a CompletableFuture to wait for the product details
        CompletableFuture<Item> futureItem = new CompletableFuture<>();
        futureItems.put(newItem.getItemID(), futureItem);

        cartEventProducer.publishItemAdditionEvent(newItem);

        if (cart != null) {
            // When the future completes, asynchronously update the cart with the new item
            // Timeout (e.g., 10 seconds) for receiving the item information
            futureItem.orTimeout(10, TimeUnit.SECONDS)
                    .thenApply(item -> {
                        cart.addItem(item);
                        return cartRepository.save(cart);
                    }).whenComplete((result, exception) -> {
                        // Cleanup the futureItems map whether successful or failed
                        futureItems.remove(newItem.getItemID());

                        if (exception != null) {
                            exception.printStackTrace(System.out);
                        }
                    });
        } else {
            throw new EntityNotFoundException("Cart not found with ID: " + cartID);
        }
    }

    public void removeItemFromCart(Long cartID, Item currentItem) {
        Cart cart = findCartByID(cartID);
        cart.removeItem(currentItem);
        cartRepository.save(cart);
    }

    public void clearCart(Long cartID) {
        Cart cart = findCartByID(cartID);
        cart.clearCart();
    }

    public void checkout(Long cartID, Long customerID) {
        Cart cart = findCartByID(cartID);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty or does not exist");
        }

        if (!cart.getCustomerID().equals(customerID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customer ID does not match the cart");
        }

        CheckoutEvent checkoutEvent = new CheckoutEvent(
                "checkoutRequested",
                cart.getCartID(),
                cart.getCustomerID(),
                cart.getTotalPrice(),
                cart.getItems().stream()
                        .map(item -> Item.createItem(item.getItemID(), item.getItemName(), item.getItemDescription(), item.getQuantity(), item.getPricePerItem(), cart))
                        .collect(Collectors.toList())
        );
        cartEventProducer.publishCheckoutEvent(checkoutEvent);
    }
}
