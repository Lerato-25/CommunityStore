package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.ShoppingCart;
import za.ac.cput.communitystore.domain.User;

import java.time.LocalDateTime;

public class ShoppingCartFactory {

    private ShoppingCartFactory() {
        // static factory class, no instances
    }

    public static ShoppingCart createShoppingCart(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        return new ShoppingCart.Builder()
                .setUser(user)
                .setCreatedDate(LocalDateTime.now())
                .build();
    }
}
