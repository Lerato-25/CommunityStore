package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.CartItem;
import za.ac.cput.communitystore.domain.Product;
import za.ac.cput.communitystore.domain.ShoppingCart;
import za.ac.cput.communitystore.util.Helper;

public class CartItemFactory {

    private CartItemFactory() {
        // static factory class, no instances
    }

    public static CartItem createCartItem(ShoppingCart cart, Product product, int quantity) {
        if (cart == null) {
            throw new IllegalArgumentException("cart cannot be null");
        }
        if (product == null) {
            throw new IllegalArgumentException("product cannot be null");
        }
        if (!Helper.isPositive(quantity)) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        return new CartItem.Builder()
                .setCart(cart)
                .setProduct(product)
                .setQuantity(quantity)
                .build();
    }

    // CartItem is immutable, so "changing" the quantity means building a changed copy
    public static CartItem withQuantity(CartItem existing, int newQuantity) {
        if (existing == null) {
            throw new IllegalArgumentException("cart item cannot be null");
        }
        if (!Helper.isPositive(newQuantity)) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        return new CartItem.Builder()
                .copy(existing)
                .setQuantity(newQuantity)
                .build();
    }
}
