package com.csci318.cart.controller;

import com.csci318.cart.controller.dto.CartDTO;
import com.csci318.cart.controller.dto.ItemDTO;
import com.csci318.cart.model.entity.Cart;
import com.csci318.cart.model.entity.Item;
import com.csci318.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Helpers ----------

    private CartDTO toDTO(Cart cart) {
        List<ItemDTO> itemDTOs = new ArrayList<ItemDTO>();

        if (cart == null) {
            return null;
        }

        for (Item item: cart.getItems()) {
            ItemDTO itemDTO = new ItemDTO();

            itemDTO.setItemID(item.getItemID());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemDescription(item.getItemDescription());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPricePerItem());

            itemDTOs.add(itemDTO);
        }

        CartDTO cartDTO = new CartDTO();

        cartDTO.setCartID(cart.getCartID());
        cartDTO.setCustomerID(cart.getCustomerID());
        cartDTO.setTotalPrice(cart.getTotalPrice());
        cartDTO.setItems(itemDTOs);

        return cartDTO;
    }

    private Cart toEntity(CartDTO cartDTO) {
        List<Item> items = new ArrayList<>();

        if (cartDTO == null) {
            return null;
        }

        Cart cart = Cart.createCart(
                cartDTO.getCustomerID(),
                cartDTO.getTotalPrice(),
                new ArrayList<>()
        );

        for (ItemDTO itemDTO : cartDTO.getItems()) {
            Item item = Item.createItem(
                    itemDTO.getItemID(),
                    itemDTO.getItemName(),
                    itemDTO.getItemDescription(),
                    itemDTO.getQuantity(),
                    itemDTO.getPrice(),
                    cart
            );

            items.add(item);
        }

        // Associate the items with the Cart
        cart.setItems(items);

        return cart;
    }

    // CRUDs ----------

    @PostMapping
    CartDTO createCart(@Valid @RequestBody Cart newCart) {
        cartService.createCart(newCart);
        return toDTO(newCart);
    }

    @GetMapping("/{cartID}")
    CartDTO findCartByID(@PathVariable Long cartID) {
        Cart cart = cartService.findCartByID(cartID);
        return toDTO(cart);
    }

    @PutMapping("/{cartID}")
    CartDTO updateCartInformation(@PathVariable Long cartID, @Valid @RequestBody CartDTO newCart) {
        Cart cart = cartService.updateCartInformation(cartID, Objects.requireNonNull(toEntity(newCart)));
        return toDTO(cart);
    }

    @DeleteMapping("/{cartID}")
    void deleteCart(@PathVariable Long cartID) {
        cartService.deleteCart(cartID);
    }

    // Logics ----------

    @PostMapping("/{cartID}")
    void addItemToCart(@PathVariable Long cartID, @RequestBody Item newItem) {
        cartService.addItemToCart(cartID, newItem);
    }

    @DeleteMapping("/{cartID}/items")
    void removeItemFromCart(@PathVariable Long cartID, Item currentItem) {
        cartService.removeItemFromCart(cartID, currentItem);
    }

    @PostMapping("/checkout")
    void checkout(@RequestParam Long cartID, @RequestParam Long customerID) {
        cartService.checkout(cartID, customerID);
    }
}
