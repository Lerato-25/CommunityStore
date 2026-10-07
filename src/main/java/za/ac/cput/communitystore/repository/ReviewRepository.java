package za.ac.cput.communitystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.communitystore.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProduct_ProductID(int productID);

    List<Review> findByReviewer_UserID(int userID);
}