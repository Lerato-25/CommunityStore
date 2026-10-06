package za.ac.cput.communitystore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.communitystore.domain.CartItem;
import za.ac.cput.communitystore.factory.CartItemFactory;
import za.ac.cput.communitystore.service.CartItemService;

import java.util.List;

@RestController
@RequestMapping("/api/cartitems")
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/create")
    public CartItem create(@RequestBody CartItem cartItem) {
        try {
            CartItem valid = CartItemFactory.createCartItem(
                    cartItem.getCart(), cartItem.getProduct(), cartItem.getQuantity());
            return cartItemService.create(valid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public CartItem read(@PathVariable int id) {
        CartItem cartItem = cartItemService.read(id);
        if (cartItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found: " + id);
        }
        return cartItem;
    }

    @PutMapping("/update")
    public CartItem update(@RequestBody CartItem cartItem) {
        CartItem valid;
        try {
            // validates the quantity and keeps the id
            valid = CartItemFactory.withQuantity(cartItem, cartItem.getQuantity());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        CartItem updated = cartItemService.update(valid);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Cart item not found: " + cartItem.getCartItemID());
        }
        return updated;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        cartItemService.delete(id);
    }

    @GetMapping("/getAll")
    public List<CartItem> getAll() {
        return cartItemService.getAll();
    }

    @GetMapping("/findByCartId/{cartId}")
    public List<CartItem> findByCartId(@PathVariable int cartId) {
        return cartItemService.findByCartId(cartId);
    }
}
