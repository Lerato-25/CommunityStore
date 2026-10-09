package za.ac.cput.communitystore.repository;
import za.ac.cput.communitystore.domain.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    java.util.List<Notifications> findByUser_UserIDOrderByCreatedDateDesc(int userId);
    java.util.List<Notifications> findByUser_UserIDAndIsReadFalseOrderByCreatedDateDesc(int userId);
}
