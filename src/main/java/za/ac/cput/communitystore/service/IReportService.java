package za.ac.cput.communitystore.service;
import za.ac.cput.communitystore.domain.Report;
import java.util.List;
public interface IReportService {
    Report create(String type,int targetId,String reason,String description);
    Report read(int id);
    Report review(int id,String status);
    List<Report> getAll();
}
