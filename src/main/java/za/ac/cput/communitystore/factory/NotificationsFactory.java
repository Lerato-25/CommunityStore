package za.ac.cput.communitystore.factory;
import za.ac.cput.communitystore.domain.*;
import java.time.LocalDateTime;
public final class NotificationsFactory {
    private NotificationsFactory() { }
    public static Notifications createNotifications(User user, String message) {
        if (user == null || user.getUserID() <= 0) throw new IllegalArgumentException("existing recipient is required");
        return new Notifications.Builder().setUser(user).setMessage(BackendValidation.text(message,"message",10000))
            .setIsRead(false).setCreatedDate(LocalDateTime.now()).build();
    }
    public static Notifications markRead(Notifications existing) {
        return new Notifications.Builder().copy(existing).setIsRead(true).build();
    }
}
