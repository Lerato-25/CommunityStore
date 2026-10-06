package za.ac.cput.communitystore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ShoppingCart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private int cartID;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user;

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate;

    protected ShoppingCart() {
        // required by JPA
    }

    private ShoppingCart(Builder builder) {
        this.cartID = builder.cartID;
        this.user = builder.user;
        this.createdDate = builder.createdDate;
    }

    public int getCartID() {
        return cartID;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCart)) return false;
        ShoppingCart that = (ShoppingCart) o;
        return cartID == that.cartID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartID);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "cartID=" + cartID +
                ", user=" + user +
                ", createdDate=" + createdDate +
                '}';
    }

    public static class Builder {
        private int cartID;
        private User user;
        private LocalDateTime createdDate;

        public Builder setCartID(int cartID) {
            this.cartID = cartID;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setCreatedDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder copy(ShoppingCart cart) {
            this.cartID = cart.cartID;
            this.user = cart.user;
            this.createdDate = cart.createdDate;
            return this;
        }

        public ShoppingCart build() {
            return new ShoppingCart(this);
        }
    }
}
