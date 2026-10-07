package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.Product;
import za.ac.cput.communitystore.domain.ProductImage;

public class ProductImageFactory {

    public static ProductImage createProductImage(
            Product product,
            String imageURL,
            boolean isPrimary) {

        validate(product, imageURL);

        return new ProductImage.Builder()
                .setProduct(product)
                .setImageURL(imageURL)
                .setPrimary(isPrimary)
                .build();
    }

    public static ProductImage withDetails(
            ProductImage image,
            Product product,
            String imageURL,
            boolean isPrimary) {

        validate(product, imageURL);

        return new ProductImage.Builder()
                .setImageId(image.getImageId())
                .setProduct(product)
                .setImageURL(imageURL)
                .setPrimary(isPrimary)
                .build();
    }

    private static void validate(Product product, String imageURL) {

        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }

        if (imageURL == null || imageURL.trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL is required");
        }
    }
}