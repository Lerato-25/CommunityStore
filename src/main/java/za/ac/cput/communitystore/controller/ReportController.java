package za.ac.cput.communitystore.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import za.ac.cput.communitystore.service.IReportService;
import static za.ac.cput.communitystore.controller.Backend3Views.*;
@RestController @RequestMapping("/api/reports")
public class ReportController {
    private final IReportService service;
    public ReportController(IReportService service) { this.service=service; }
    public record ReportRequest(@NotBlank String targetType,@Positive int targetId,@NotBlank @Size(max=100) String reason,@Size(max=10000) String description) { }
    public record ReviewRequest(@NotBlank String status) { }
    @PostMapping({"","/create"}) @ResponseStatus(HttpStatus.CREATED) public ReportView create(@Valid @RequestBody ReportRequest r) { return ReportView.of(service.create(r.targetType(),r.targetId(),r.reason(),r.description())); }
    @GetMapping({"/{id}","/read/{id}"}) public ReportView read(@PathVariable int id) { return ReportView.of(service.read(id)); }
    @GetMapping({"","/getAll"}) public List<ReportView> all() { return service.getAll().stream().map(ReportView::of).toList(); }
    @PatchMapping("/{id}/review") public ReportView review(@PathVariable int id,@Valid @RequestBody ReviewRequest r) { return ReportView.of(service.review(id,r.status())); }
}
