package za.ac.cput.communitystore.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.communitystore.domain.Report;
import za.ac.cput.communitystore.factory.ReportFactory;
import za.ac.cput.communitystore.repository.*;
import java.util.List;
@Service
@Transactional
public class ReportService implements IReportService {
    private final ReportRepository reports;
    private final ProductRepository products;
    private final ReviewRepository reviews;
    private final CommunityPostRepository posts;
    private final UserRepository users;
    private final Backend3Access access;
    public ReportService(ReportRepository reports,ProductRepository products,ReviewRepository reviews,
            CommunityPostRepository posts,UserRepository users,Backend3Access access) {
        this.reports=reports; this.products=products; this.reviews=reviews; this.posts=posts; this.users=users; this.access=access;
    }
    public Report create(String type,int targetId,String reason,String description) {
        var report=ReportFactory.createReport(access.current(),type,targetId,reason,description);
        boolean exists=switch(type) {
            case "Product" -> products.existsById(targetId);
            case "Review" -> reviews.existsById(targetId);
            case "CommunityPost" -> posts.existsById(targetId);
            case "User" -> users.existsById(targetId);
            default -> false;
        };
        if (!exists) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Report target not found");
        return reports.save(report);
    }
    @Transactional(readOnly=true) public Report read(int id) {
        var report=access.required(reports.findById(id),"Report"); access.ownerOrAdmin(report.getReporter()); return report;
    }
    @Transactional(readOnly=true) public List<Report> getAll() {
        var actor=access.current();
        return access.isAdmin(actor) ? reports.findAll() : reports.findByReporter_UserIDOrderByCreatedDateDesc(actor.getUserID());
    }
    public Report review(int id,String status) {
        access.admin(); var existing=access.required(reports.lockById(id),"Report");
        if (java.util.Set.of("ActionTaken","Dismissed").contains(existing.getStatus()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Report is already resolved");
        var result=reports.save(ReportFactory.withReview(existing,access.current(),status));
        return result;
    }
}
