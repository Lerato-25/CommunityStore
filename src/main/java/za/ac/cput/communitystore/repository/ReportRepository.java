package za.ac.cput.communitystore.repository;
import za.ac.cput.communitystore.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReportRepository extends JpaRepository<Report, Integer> {
    java.util.List<Report> findByReporter_UserIDOrderByCreatedDateDesc(int userId);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select r from Report r where r.reportID = :id")
    java.util.Optional<Report> lockById(@org.springframework.data.repository.query.Param("id") int id);
}
