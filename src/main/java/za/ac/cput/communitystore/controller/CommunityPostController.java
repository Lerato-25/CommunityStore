package za.ac.cput.communitystore.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import za.ac.cput.communitystore.service.ICommunityPostService;
import static za.ac.cput.communitystore.controller.Backend3Views.*;
@RestController @RequestMapping("/api/community-posts")
public class CommunityPostController {
    private final ICommunityPostService service;
    public CommunityPostController(ICommunityPostService service) { this.service=service; }
    public record PostRequest(@NotBlank @Size(max=150) String title,@NotBlank @Size(max=10000) String content) { }
    @PostMapping({"","/create"}) @ResponseStatus(HttpStatus.CREATED) public PostView create(@Valid @RequestBody PostRequest r) { return PostView.of(service.create(r.title(),r.content())); }
    @GetMapping({"/{id}","/read/{id}"}) public PostView read(@PathVariable int id) { return PostView.of(service.read(id)); }
    @GetMapping({"","/getAll"}) public List<PostView> all() { return service.getAll().stream().map(PostView::of).toList(); }
    @PutMapping({"/{id}","/update/{id}"}) public PostView update(@PathVariable int id,@Valid @RequestBody PostRequest r) { return PostView.of(service.update(id,r.title(),r.content())); }
    @DeleteMapping({"/{id}","/delete/{id}"}) @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable int id) { service.delete(id); }
}
