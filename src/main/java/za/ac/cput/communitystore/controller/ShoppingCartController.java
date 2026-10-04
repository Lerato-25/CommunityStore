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
import za.ac.cput.communitystore.domain.ShoppingCart;
import za.ac.cput.communitystore.factory.ShoppingCartFactory;
import za.ac.cput.communitystore.service.ShoppingCartService;

import java.util.List;

@RestController
@RequestMapping("/api/shoppingcarts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/create")
    public ShoppingCart create(@RequestBody ShoppingCart cart) {
        try {
            // factory validates the input and sets createdDate
            ShoppingCart valid = ShoppingCartFactory.createShoppingCart(cart.getUser());
            return shoppingCartService.create(valid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public ShoppingCart read(@PathVariable int id) {
        ShoppingCart cart = shoppingCartService.read(id);
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping cart not found: " + id);
        }
        return cart;
    }

    @PutMapping("/update")
    public ShoppingCart update(@RequestBody ShoppingCart cart) {
        try {
            ShoppingCartFactory.createShoppingCart(cart.getUser()); // validation only
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        ShoppingCart updated = shoppingCartService.update(cart);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Shopping cart not found: " + cart.getCartID());
        }
        return updated;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        shoppingCartService.delete(id);
    }

    @GetMapping("/getAll")
    public List<ShoppingCart> getAll() {
        return shoppingCartService.getAll();
    }

    @GetMapping("/findByUserId/{userId}")
    public ShoppingCart findByUserId(@PathVariable int userId) {
        ShoppingCart cart = shoppingCartService.findByUserId(userId);
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No cart found for user: " + userId);
        }
        return cart;
    }
}
