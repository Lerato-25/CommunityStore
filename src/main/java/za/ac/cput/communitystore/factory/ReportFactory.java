package za.ac.cput.communitystore.factory;
import za.ac.cput.communitystore.domain.*;
import java.time.LocalDateTime;
import java.util.Set;
public final class ReportFactory {
    public static final Set<String> TARGET_TYPES = Set.of("Product","Review","CommunityPost","User");
    public static final Set<String> STATUSES = Set.of("Pending","Reviewed","ActionTaken","Dismissed");
    private ReportFactory() { }
    public static Report createReport(User reporter, String type, int targetId, String reason, String description) {
        if (reporter == null || reporter.getUserID() <= 0) throw new IllegalArgumentException("existing reporter is required");
        if (!TARGET_TYPES.contains(type) || targetId <= 0) throw new IllegalArgumentException("invalid report target");
        if (description != null && description.length() > 10000) throw new IllegalArgumentException("description is too long");
        return new Report.Builder().setReporter(reporter).setTargetType(type).setTargetID(targetId)
            .setReason(BackendValidation.text(reason,"reason",100)).setDescription(description)
            .setStatus("Pending").setCreatedDate(LocalDateTime.now()).build();
    }
    public static Report withReview(Report existing, User reviewer, String status) {
        if (reviewer == null || !STATUSES.contains(status) || "Pending".equals(status))
            throw new IllegalArgumentException("invalid review status");
        return new Report.Builder().copy(existing).setReviewedBy(reviewer).setStatus(status)
            .setResolvedDate(Set.of("ActionTaken","Dismissed").contains(status) ? LocalDateTime.now() : null).build();
    }
}
