package za.ac.cput.communitystore.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="Payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="paymentId")
    private int paymentID;
    @ManyToOne @JoinColumn(name="orderId", nullable=false, unique=true)
    private Order order;
    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal amount;
    @Column(nullable=false, length=50)
    private String paymentMethod;
    @Column(nullable=false, length=30)
    private String paymentStatus;

    private LocalDateTime paymentDate;
    @Column(unique=true, length=100)
    private String transactionReference;
    protected Payment() { }
    private Payment(Builder b) {
        this.paymentID = b.paymentID;
        this.order = b.order;
        this.amount = b.amount;
        this.paymentMethod = b.paymentMethod;
        this.paymentStatus = b.paymentStatus;
        this.paymentDate = b.paymentDate;
        this.transactionReference = b.transactionReference;
    }
    public int getPaymentID() { return paymentID; }
    public Order getOrder() { return order; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getTransactionReference() { return transactionReference; }
    public static class Builder {
        private int paymentID;
        private Order order;
        private BigDecimal amount;
        private String paymentMethod;
        private String paymentStatus;
        private LocalDateTime paymentDate;
        private String transactionReference;
        public Builder setPaymentID(int value) { this.paymentID = value; return this; }
        public Builder setOrder(Order value) { this.order = value; return this; }
        public Builder setAmount(BigDecimal value) { this.amount = value; return this; }
        public Builder setPaymentMethod(String value) { this.paymentMethod = value; return this; }
        public Builder setPaymentStatus(String value) { this.paymentStatus = value; return this; }
        public Builder setPaymentDate(LocalDateTime value) { this.paymentDate = value; return this; }
        public Builder setTransactionReference(String value) { this.transactionReference = value; return this; }
        public Builder copy(Payment value) {
            this.paymentID = value.paymentID;
            this.order = value.order;
            this.amount = value.amount;
            this.paymentMethod = value.paymentMethod;
            this.paymentStatus = value.paymentStatus;
            this.paymentDate = value.paymentDate;
            this.transactionReference = value.transactionReference;
            return this;
        }
        public Payment build() { return new Payment(this); }
    }
}
