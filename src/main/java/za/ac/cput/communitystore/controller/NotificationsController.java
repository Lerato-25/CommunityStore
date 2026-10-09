package za.ac.cput.communitystore.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import za.ac.cput.communitystore.service.INotificationsService;
import static za.ac.cput.communitystore.controller.Backend3Views.*;
@RestController @RequestMapping("/api/notifications")
public class NotificationsController {
    private final INotificationsService service;
    public NotificationsController(INotificationsService service) { this.service=service; }
    public record NotificationRequest(@Positive int userId,@NotBlank @Size(max=10000) String message) { }
    @PostMapping({"","/create"}) @ResponseStatus(HttpStatus.CREATED) public NotificationView create(@Valid @RequestBody NotificationRequest r) { return NotificationView.of(service.create(r.userId(),r.message())); }
    @GetMapping({"/{id}","/read/{id}"}) public NotificationView read(@PathVariable int id) { return NotificationView.of(service.read(id)); }
    @GetMapping({"","/getAll"}) public List<NotificationView> all(@RequestParam(defaultValue="false") boolean unreadOnly) { return service.getAll(unreadOnly).stream().map(NotificationView::of).toList(); }
    @PatchMapping("/{id}/read") public NotificationView markRead(@PathVariable int id) { return NotificationView.of(service.markRead(id)); }
    @DeleteMapping({"/{id}","/delete/{id}"}) @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable int id) { service.delete(id); }
}
