package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.Order;
import za.ac.cput.communitystore.domain.User;
import za.ac.cput.communitystore.util.Helper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFactory {

    // must match the CHECK constraint ck_orders_status in the database
    public static final List<String> VALID_STATUSES =
            List.of("Pending", "Paid", "Processing", "Shipped", "Completed", "Cancelled");

    private OrderFactory() {
        // static factory class, no instances
    }

    public static Order createOrder(User buyer, String shippingAddress, BigDecimal totalAmount) {
        if (buyer == null) {
            throw new IllegalArgumentException("buyer cannot be null");
        }
        if (Helper.isNullOrEmpty(shippingAddress)) {
            throw new IllegalArgumentException("shippingAddress cannot be null or empty");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("totalAmount cannot be null or negative");
        }

        return new Order.Builder()
                .setBuyer(buyer)
                .setOrderDate(LocalDateTime.now())
                .setOrderStatus("Pending")
                .setTotalAmount(totalAmount)
                .setShippingAddress(shippingAddress)
                .build();
    }

    // Order is immutable, so a status change builds a changed copy
    public static Order withStatus(Order existing, String newStatus) {
        if (existing == null) {
            throw new IllegalArgumentException("order cannot be null");
        }
        if (Helper.isNullOrEmpty(newStatus) || !VALID_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException("orderStatus is not valid: " + newStatus);
        }

        return new Order.Builder()
                .copy(existing)
                .setOrderStatus(newStatus)
                .build();
    }
}
