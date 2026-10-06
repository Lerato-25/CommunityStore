package za.ac.cput.communitystore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "CartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private int cartItemID;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    private ShoppingCart cart;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected CartItem() {
        // required by JPA
    }

    private CartItem(Builder builder) {
        this.cartItemID = builder.cartItemID;
        this.cart = builder.cart;
        this.product = builder.product;
        this.quantity = builder.quantity;
    }

    public int getCartItemID() {
        return cartItemID;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return cartItemID == cartItem.cartItemID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemID);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemID=" + cartItemID +
                ", cart=" + cart +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public static class Builder {
        private int cartItemID;
        private ShoppingCart cart;
        private Product product;
        private int quantity;

        public Builder setCartItemID(int cartItemID) {
            this.cartItemID = cartItemID;
            return this;
        }

        public Builder setCart(ShoppingCart cart) {
            this.cart = cart;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder copy(CartItem cartItem) {
            this.cartItemID = cartItem.cartItemID;
            this.cart = cartItem.cart;
            this.product = cartItem.product;
            this.quantity = cartItem.quantity;
            return this;
        }

        public CartItem build() {
            return new CartItem(this);
        }
    }
}
