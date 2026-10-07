package za.ac.cput.communitystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.communitystore.domain.Review;
import za.ac.cput.communitystore.repository.ReviewRepository;

import java.util.List;

@Service
public class IReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public IReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    public Review read(int id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Review update(Review review) {
        if (reviewRepository.existsById(review.getReviewID())) {
            return reviewRepository.save(review);
        }
        return null;
    }

    public void delete(int id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByProductId(int productID) {
        return reviewRepository.findByProduct_ProductID(productID);
    }

    public List<Review> findByReviewerId(int userID) {
        return reviewRepository.findByReviewer_UserID(userID);
    }
}