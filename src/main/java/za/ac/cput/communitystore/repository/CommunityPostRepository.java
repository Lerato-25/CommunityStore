package za.ac.cput.communitystore.repository;
import za.ac.cput.communitystore.domain.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Integer> {
    java.util.List<CommunityPost> findAllByOrderByPostDateDesc();
}
