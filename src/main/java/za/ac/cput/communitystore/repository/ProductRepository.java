package za.ac.cput.communitystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.communitystore.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategory_CategoryID(int categoryID);

    List<Product> findBySeller_UserID(int userID);
}