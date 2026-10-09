package za.ac.cput.communitystore.service;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import za.ac.cput.communitystore.domain.User;
import za.ac.cput.communitystore.repository.UserRepository;
@Component
public class Backend3Access {
    private final UserRepository users;
    public Backend3Access(UserRepository users) { this.users = users; }
    public User current() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return users.findByEmail(auth.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }
    public boolean isAdmin(User user) { return user.getRole() != null && "Admin".equalsIgnoreCase(user.getRole().getRoleName()); }
    public void ownerOrAdmin(User owner) {
        User actor = current();
        if (actor.getUserID() != owner.getUserID() && !isAdmin(actor)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    public void admin() { if (!isAdmin(current())) throw new ResponseStatusException(HttpStatus.FORBIDDEN); }
    public <T> T required(java.util.Optional<T> value, String label) {
        return value.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, label + " not found"));
    }
}
