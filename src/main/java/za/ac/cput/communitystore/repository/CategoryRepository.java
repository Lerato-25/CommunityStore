package za.ac.cput.communitystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.communitystore.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}