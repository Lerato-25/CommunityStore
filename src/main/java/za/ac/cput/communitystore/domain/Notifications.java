package za.ac.cput.communitystore.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="Notifications")
public class Notifications {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="notificationId")
    private int notificationID;
    @ManyToOne @JoinColumn(name="userId", nullable=false)
    private User user;
    @Column(nullable=false, columnDefinition="TEXT")
    private String message;
    @Column(name="isRead", nullable=false)
    private boolean isRead;
    @Column(nullable=false)
    private LocalDateTime createdDate;
    protected Notifications() { }
    private Notifications(Builder b) {
        this.notificationID = b.notificationID;
        this.user = b.user;
        this.message = b.message;
        this.isRead = b.isRead;
        this.createdDate = b.createdDate;
    }
    public int getNotificationID() { return notificationID; }
    public User getUser() { return user; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public static class Builder {
        private int notificationID;
        private User user;
        private String message;
        private boolean isRead;
        private LocalDateTime createdDate;
        public Builder setNotificationID(int value) { this.notificationID = value; return this; }
        public Builder setUser(User value) { this.user = value; return this; }
        public Builder setMessage(String value) { this.message = value; return this; }
        public Builder setIsRead(boolean value) { this.isRead = value; return this; }
        public Builder setCreatedDate(LocalDateTime value) { this.createdDate = value; return this; }
        public Builder copy(Notifications value) {
            this.notificationID = value.notificationID;
            this.user = value.user;
            this.message = value.message;
            this.isRead = value.isRead;
            this.createdDate = value.createdDate;
            return this;
        }
        public Notifications build() { return new Notifications(this); }
    }
}
