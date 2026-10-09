package za.ac.cput.communitystore.service;
import za.ac.cput.communitystore.domain.Notifications;
import java.util.List;
public interface INotificationsService {
    Notifications create(int userId,String message);
    Notifications read(int id);
    Notifications markRead(int id);
    void delete(int id);
    List<Notifications> getAll(boolean unreadOnly);
}
