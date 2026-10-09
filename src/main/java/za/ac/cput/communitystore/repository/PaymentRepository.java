package za.ac.cput.communitystore.repository;
import za.ac.cput.communitystore.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select p from Payment p where p.paymentID = :id")
    java.util.Optional<Payment> lockById(@org.springframework.data.repository.query.Param("id") int id);
    java.util.Optional<Payment> findByOrder_OrderID(int orderId);
    java.util.List<Payment> findByOrder_Buyer_UserID(int userId);
}
