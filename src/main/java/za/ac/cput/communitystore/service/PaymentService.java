package za.ac.cput.communitystore.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.communitystore.domain.*;
import za.ac.cput.communitystore.factory.*;
import za.ac.cput.communitystore.repository.*;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class PaymentService implements IPaymentService {
    private final PaymentRepository payments;
    private final OrderRepository orders;
    private final NotificationsRepository notifications;
    private final Backend3Access access;
    private final Environment environment;
    @jakarta.persistence.PersistenceContext private jakarta.persistence.EntityManager entityManager;
    public PaymentService(PaymentRepository payments, OrderRepository orders, NotificationsRepository notifications,
            Backend3Access access, Environment environment) {
        this.payments=payments; this.orders=orders; this.notifications=notifications; this.access=access; this.environment=environment;
    }
    private Optional<za.ac.cput.communitystore.domain.Order> lockedOrder(int id) {
        return Optional.ofNullable(entityManager.find(za.ac.cput.communitystore.domain.Order.class,id,
            jakarta.persistence.LockModeType.PESSIMISTIC_WRITE));
    }
    public Payment create(int orderId, BigDecimal amount, String method) {
        var order=access.required(lockedOrder(orderId),"Order");
        access.ownerOrAdmin(order.getBuyer());
        if (!"Pending".equals(order.getOrderStatus())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Order is not awaiting payment");
        if (BackendValidation.money(amount).compareTo(order.getTotalAmount()) != 0)
            throw new IllegalArgumentException("amount must match the order total");
        if (payments.findByOrder_OrderID(orderId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Order already has a payment");
        // No provider session is created here. Only the explicitly enabled demo can settle payments.
        if (!"Demo".equals(method)) throw new IllegalArgumentException("Only Demo payments are configured; no live gateway is connected");
        return payments.saveAndFlush(PaymentFactory.createPayment(order,amount,method));
    }
    @Transactional(readOnly=true)
    public Payment read(int id) {
        var payment=access.required(payments.findById(id),"Payment");
        access.ownerOrAdmin(payment.getOrder().getBuyer());
        return payment;
    }
    @Transactional(readOnly=true)
    public List<Payment> getAll() {
        var actor=access.current();
        return access.isAdmin(actor) ? payments.findAll() : payments.findByOrder_Buyer_UserID(actor.getUserID());
    }
    public Payment completeDemo(int id, boolean successful) {
        if (!environment.acceptsProfiles(Profiles.of("demo"))) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        var payment=access.required(payments.lockById(id),"Payment");
        access.ownerOrAdmin(payment.getOrder().getBuyer());
        var order=access.required(lockedOrder(payment.getOrder().getOrderID()),"Order");
        // Lock the payment before reading its status so simultaneous results are serialized.
        if (!"Demo".equals(payment.getPaymentMethod())) throw new ResponseStatusException(HttpStatus.CONFLICT);
        if (!"Pending".equals(payment.getPaymentStatus())) {
            if (payment.getPaymentStatus().equals(successful ? "Successful" : "Failed")) return payment;
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Payment already has a final result");
        }
        if (!"Pending".equals(order.getOrderStatus())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Order is no longer awaiting payment");
        var result=payments.save(PaymentFactory.withResult(payment,successful ? "Successful" : "Failed","DEMO-"+UUID.randomUUID()));
        if (successful) orders.save(OrderFactory.withStatus(order,"Paid"));
        notifications.save(NotificationsFactory.createNotifications(order.getBuyer(),
            "Demo payment for order #"+order.getOrderID()+ (successful ? " succeeded." : " failed. No money was charged.")));
        return result;
    }
}
