package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.Category;
import za.ac.cput.communitystore.domain.Product;
import za.ac.cput.communitystore.domain.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductFactory {

    public static Product createProduct(
            User seller,
            Category category,
            String productName,
            String description,
            BigDecimal price,
            int quantity,
            String condition,
            String status) {

        validate(seller, category, productName, price, quantity, condition, status);

        return new Product.Builder()
                .setSeller(seller)
                .setCategory(category)
                .setProductName(productName)
                .setDescription(description)
                .setPrice(price)
                .setQuantity(quantity)
                .setCondition(condition)
                .setStatus(status)
                .setDatePosted(LocalDateTime.now())
                .build();
    }

    public static Product withDetails(
            Product product,
            User seller,
            Category category,
            String productName,
            String description,
            BigDecimal price,
            int quantity,
            String condition,
            String status) {

        validate(seller, category, productName, price, quantity, condition, status);

        return new Product.Builder()
                .setProductID(product.getProductID())
                .setSeller(seller)
                .setCategory(category)
                .setProductName(productName)
                .setDescription(description)
                .setPrice(price)
                .setQuantity(quantity)
                .setCondition(condition)
                .setStatus(status)
                .setDatePosted(product.getDatePosted())
                .build();
    }

    private static void validate(
            User seller,
            Category category,
            String productName,
            BigDecimal price,
            int quantity,
            String condition,
            String status) {

        if (seller == null) {
            throw new IllegalArgumentException("Seller is required");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category is required");
        }

        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be zero or greater");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Condition is required");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }
    }
}