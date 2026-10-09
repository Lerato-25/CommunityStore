package za.ac.cput.communitystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.communitystore.domain.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByBuyer_UserID(int userId);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select o from Order o where o.orderID = :id")
    java.util.Optional<Order> lockById(@org.springframework.data.repository.query.Param("id") int id);
}
