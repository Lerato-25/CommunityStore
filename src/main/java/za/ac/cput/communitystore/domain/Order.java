package za.ac.cput.communitystore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private int orderID;

    @ManyToOne
    @JoinColumn(name = "buyerId", nullable = false)
    private User buyer;

    @Column(name = "orderDate", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "orderStatus", nullable = false)
    private String orderStatus;

    @Column(name = "totalAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "shippingAddress", nullable = false)
    private String shippingAddress;

    protected Order() {
        // required by JPA
    }

    private Order(Builder builder) {
        this.orderID = builder.orderID;
        this.buyer = builder.buyer;
        this.orderDate = builder.orderDate;
        this.orderStatus = builder.orderStatus;
        this.totalAmount = builder.totalAmount;
        this.shippingAddress = builder.shippingAddress;
    }

    public int getOrderID() {
        return orderID;
    }

    public User getBuyer() {
        return buyer;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderID == order.orderID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", buyer=" + buyer +
                ", orderDate=" + orderDate +
                ", orderStatus='" + orderStatus + '\'' +
                ", totalAmount=" + totalAmount +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }

    public static class Builder {
        private int orderID;
        private User buyer;
        private LocalDateTime orderDate;
        private String orderStatus;
        private BigDecimal totalAmount;
        private String shippingAddress;

        public Builder setOrderID(int orderID) {
            this.orderID = orderID;
            return this;
        }

        public Builder setBuyer(User buyer) {
            this.buyer = buyer;
            return this;
        }

        public Builder setOrderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder copy(Order order) {
            this.orderID = order.orderID;
            this.buyer = order.buyer;
            this.orderDate = order.orderDate;
            this.orderStatus = order.orderStatus;
            this.totalAmount = order.totalAmount;
            this.shippingAddress = order.shippingAddress;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
