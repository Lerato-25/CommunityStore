package za.ac.cput.communitystore.factory;
import za.ac.cput.communitystore.domain.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
public final class PaymentFactory {
    public static final Set<String> STATUSES = Set.of("Pending", "Successful", "Failed", "Refunded");
    private PaymentFactory() { }
    public static Payment createPayment(Order order, BigDecimal amount, String method) {
        if (order == null || order.getOrderID() <= 0) throw new IllegalArgumentException("existing order is required");
        return new Payment.Builder().setOrder(order).setAmount(BackendValidation.money(amount))
            .setPaymentMethod(BackendValidation.text(method,"paymentMethod",50)).setPaymentStatus("Pending").build();
    }
    public static Payment withResult(Payment payment, String status, String reference) {
        if (payment == null || !STATUSES.contains(status)) throw new IllegalArgumentException("invalid payment status");
        return new Payment.Builder().copy(payment).setPaymentStatus(status)
            .setTransactionReference(BackendValidation.text(reference,"transactionReference",100))
            .setPaymentDate(LocalDateTime.now()).build();
    }
}
