package za.ac.cput.communitystore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.communitystore.domain.Review;
import za.ac.cput.communitystore.factory.ReviewFactory;
import za.ac.cput.communitystore.service.IReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final IReviewService reviewService;

    @Autowired
    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public Review create(@RequestBody Review review) {
        try {
            Review valid = ReviewFactory.createReview(
                    review.getProduct(),
                    review.getReviewer(),
                    review.getRating(),
                    review.getComment());

            return reviewService.create(valid);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public Review read(@PathVariable int id) {

        Review review = reviewService.read(id);

        if (review == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Review not found: " + id);
        }

        return review;
    }

    @PutMapping("/update")
    public Review update(@RequestBody Review review) {

        try {
            Review valid = ReviewFactory.withDetails(
                    review,
                    review.getProduct(),
                    review.getReviewer(),
                    review.getRating(),
                    review.getComment());

            Review updated = reviewService.update(valid);

            if (updated == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found: " +
                                review.getReviewID());
            }

            return updated;

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        reviewService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Review> getAll() {
        return reviewService.getAll();
    }

    @GetMapping("/findByProductId/{productID}")
    public List<Review> findByProductId(
            @PathVariable int productID) {

        return reviewService.findByProductId(productID);
    }

    @GetMapping("/findByReviewerId/{userID}")
    public List<Review> findByReviewerId(
            @PathVariable int userID) {

        return reviewService.findByReviewerId(userID);
    }
}