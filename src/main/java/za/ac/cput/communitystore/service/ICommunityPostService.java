package za.ac.cput.communitystore.service;
import za.ac.cput.communitystore.domain.CommunityPost;
import java.util.List;
public interface ICommunityPostService {
    CommunityPost create(String title, String content);
    CommunityPost read(int id);
    CommunityPost update(int id, String title, String content);
    void delete(int id);
    List<CommunityPost> getAll();
}
