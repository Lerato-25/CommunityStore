package za.ac.cput.communitystore.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.communitystore.domain.Notifications;
import za.ac.cput.communitystore.factory.NotificationsFactory;
import za.ac.cput.communitystore.repository.*;
import java.util.List;
@Service
@Transactional
public class NotificationsService implements INotificationsService {
    private final NotificationsRepository notifications;
    private final UserRepository users;
    private final Backend3Access access;
    public NotificationsService(NotificationsRepository notifications,UserRepository users,Backend3Access access) {
        this.notifications=notifications; this.users=users; this.access=access;
    }
    public Notifications create(int userId,String message) {
        access.admin();
        return notifications.save(NotificationsFactory.createNotifications(access.required(users.findById(userId),"Recipient"),message));
    }
    public Notifications read(int id) {
        var n=access.required(notifications.findById(id),"Notification"); access.ownerOrAdmin(n.getUser()); return n;
    }
    public Notifications markRead(int id) { return notifications.save(NotificationsFactory.markRead(read(id))); }
    public void delete(int id) { notifications.delete(read(id)); }
    @Transactional(readOnly=true) public List<Notifications> getAll(boolean unreadOnly) {
        int id=access.current().getUserID();
        return unreadOnly ? notifications.findByUser_UserIDAndIsReadFalseOrderByCreatedDateDesc(id)
            : notifications.findByUser_UserIDOrderByCreatedDateDesc(id);
    }
}
