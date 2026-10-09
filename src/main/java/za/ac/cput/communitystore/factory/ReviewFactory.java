package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.Product;
import za.ac.cput.communitystore.domain.Review;
import za.ac.cput.communitystore.domain.User;

import java.time.LocalDateTime;

public class ReviewFactory {

    public static Review createReview(
            Product product,
            User reviewer,
            int rating,
            String comment) {

        validate(product, reviewer, rating, comment);

        return new Review.Builder()
                .setProduct(product)
                .setReviewer(reviewer)
                .setRating(rating)
                .setComment(comment)
                .setReviewDate(LocalDateTime.now())
                .build();
    }

    public static Review withDetails(
            Review review,
            Product product,
            User reviewer,
            int rating,
            String comment) {

        validate(product, reviewer, rating, comment);

        return new Review.Builder()
                .setReviewID(review.getReviewID())
                .setProduct(product)
                .setReviewer(reviewer)
                .setRating(rating)
                .setComment(comment)
                .setReviewDate(review.getReviewDate())
                .build();
    }

    private static void validate(
            Product product,
            User reviewer,
            int rating,
            String comment) {

        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }

        if (reviewer == null) {
            throw new IllegalArgumentException("Reviewer is required");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment is required");
        }
    }
}