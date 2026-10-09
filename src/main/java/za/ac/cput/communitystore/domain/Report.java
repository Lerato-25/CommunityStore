package za.ac.cput.communitystore.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="Reports")
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="reportId")
    private int reportID;
    @ManyToOne @JoinColumn(name="reporterId", nullable=false)
    private User reporter;
    @ManyToOne @JoinColumn(name="reviewedBy")
    private User reviewedBy;
    @Column(nullable=false, length=30)
    private String targetType;
    @Column(name="targetId", nullable=false)
    private int targetID;
    @Column(nullable=false, length=100)
    private String reason;
    @Column(columnDefinition="TEXT")
    private String description;
    @Column(nullable=false, length=20)
    private String status;
    @Column(nullable=false)
    private LocalDateTime createdDate;

    private LocalDateTime resolvedDate;
    protected Report() { }
    private Report(Builder b) {
        this.reportID = b.reportID;
        this.reporter = b.reporter;
        this.reviewedBy = b.reviewedBy;
        this.targetType = b.targetType;
        this.targetID = b.targetID;
        this.reason = b.reason;
        this.description = b.description;
        this.status = b.status;
        this.createdDate = b.createdDate;
        this.resolvedDate = b.resolvedDate;
    }
    public int getReportID() { return reportID; }
    public User getReporter() { return reporter; }
    public User getReviewedBy() { return reviewedBy; }
    public String getTargetType() { return targetType; }
    public int getTargetID() { return targetID; }
    public String getReason() { return reason; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getResolvedDate() { return resolvedDate; }
    public static class Builder {
        private int reportID;
        private User reporter;
        private User reviewedBy;
        private String targetType;
        private int targetID;
        private String reason;
        private String description;
        private String status;
        private LocalDateTime createdDate;
        private LocalDateTime resolvedDate;
        public Builder setReportID(int value) { this.reportID = value; return this; }
        public Builder setReporter(User value) { this.reporter = value; return this; }
        public Builder setReviewedBy(User value) { this.reviewedBy = value; return this; }
        public Builder setTargetType(String value) { this.targetType = value; return this; }
        public Builder setTargetID(int value) { this.targetID = value; return this; }
        public Builder setReason(String value) { this.reason = value; return this; }
        public Builder setDescription(String value) { this.description = value; return this; }
        public Builder setStatus(String value) { this.status = value; return this; }
        public Builder setCreatedDate(LocalDateTime value) { this.createdDate = value; return this; }
        public Builder setResolvedDate(LocalDateTime value) { this.resolvedDate = value; return this; }
        public Builder copy(Report value) {
            this.reportID = value.reportID;
            this.reporter = value.reporter;
            this.reviewedBy = value.reviewedBy;
            this.targetType = value.targetType;
            this.targetID = value.targetID;
            this.reason = value.reason;
            this.description = value.description;
            this.status = value.status;
            this.createdDate = value.createdDate;
            this.resolvedDate = value.resolvedDate;
            return this;
        }
        public Report build() { return new Report(this); }
    }
}
