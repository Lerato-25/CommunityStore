package za.ac.cput.communitystore.service;
import za.ac.cput.communitystore.domain.Payment;
import java.math.BigDecimal;
import java.util.List;
public interface IPaymentService {
    Payment create(int orderId, BigDecimal amount, String method);
    Payment read(int id);
    List<Payment> getAll();
    Payment completeDemo(int id, boolean successful);
}
