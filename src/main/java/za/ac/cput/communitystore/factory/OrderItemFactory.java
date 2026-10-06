package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.Order;
import za.ac.cput.communitystore.domain.OrderItem;
import za.ac.cput.communitystore.domain.Product;
import za.ac.cput.communitystore.util.Helper;

import java.math.BigDecimal;

public class OrderItemFactory {

    private OrderItemFactory() {
        // static factory class, no instances
    }

    public static OrderItem createOrderItem(Order order,
                                            Product product,
                                            int quantity,
                                            BigDecimal unitPrice) {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }
        if (product == null) {
            throw new IllegalArgumentException("product cannot be null");
        }
        if (!Helper.isPositive(quantity)) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("unitPrice cannot be null or negative");
        }

        return new OrderItem.Builder()
                .setOrder(order)
                .setProduct(product)
                .setQuantity(quantity)
                .setUnitPrice(unitPrice)
                .build();
    }
}
