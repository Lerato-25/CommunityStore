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
import java.util.Objects;

@Entity
@Table(name = "OrderItems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderItemId")
    private int orderItemID;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // price of the product at the time of purchase (snapshot)
    @Column(name = "unitPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    protected OrderItem() {
        // required by JPA
    }

    private OrderItem(Builder builder) {
        this.orderItemID = builder.orderItemID;
        this.order = builder.order;
        this.product = builder.product;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
    }

    public int getOrderItemID() {
        return orderItemID;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem that = (OrderItem) o;
        return orderItemID == that.orderItemID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemID);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemID=" + orderItemID +
                ", order=" + order +
                ", product=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }

    public static class Builder {
        private int orderItemID;
        private Order order;
        private Product product;
        private int quantity;
        private BigDecimal unitPrice;

        public Builder setOrderItemID(int orderItemID) {
            this.orderItemID = orderItemID;
            return this;
        }

        public Builder setOrder(Order order) {
            this.order = order;
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

        public Builder setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder copy(OrderItem item) {
            this.orderItemID = item.orderItemID;
            this.order = item.order;
            this.product = item.product;
            this.quantity = item.quantity;
            this.unitPrice = item.unitPrice;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
