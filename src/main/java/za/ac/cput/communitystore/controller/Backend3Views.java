package za.ac.cput.communitystore.controller;
import za.ac.cput.communitystore.domain.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public final class Backend3Views {
    private Backend3Views() { }
    public record PaymentView(int paymentId,int orderId,BigDecimal amount,String paymentMethod,String paymentStatus,LocalDateTime paymentDate,String transactionReference) {
        static PaymentView of(Payment p) { return new PaymentView(p.getPaymentID(),p.getOrder().getOrderID(),p.getAmount(),p.getPaymentMethod(),p.getPaymentStatus(),p.getPaymentDate(),p.getTransactionReference()); }
    }
    public record PostView(int postId,int userId,String author,String title,String content,LocalDateTime postDate) {
        static PostView of(CommunityPost p) { return new PostView(p.getPostID(),p.getUser().getUserID(),p.getUser().getFirstName(),p.getTitle(),p.getContent(),p.getPostDate()); }
    }
    public record NotificationView(int notificationId,int userId,String message,boolean isRead,LocalDateTime createdDate) {
        static NotificationView of(Notifications n) { return new NotificationView(n.getNotificationID(),n.getUser().getUserID(),n.getMessage(),n.isRead(),n.getCreatedDate()); }
    }
    public record ReportView(int reportId,int reporterId,Integer reviewedBy,String targetType,int targetId,String reason,String description,String status,LocalDateTime createdDate,LocalDateTime resolvedDate) {
        static ReportView of(Report r) { return new ReportView(r.getReportID(),r.getReporter().getUserID(),r.getReviewedBy()==null ? null : r.getReviewedBy().getUserID(),r.getTargetType(),r.getTargetID(),r.getReason(),r.getDescription(),r.getStatus(),r.getCreatedDate(),r.getResolvedDate()); }
    }
}
