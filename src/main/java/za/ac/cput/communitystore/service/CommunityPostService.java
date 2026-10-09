package za.ac.cput.communitystore.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.communitystore.domain.CommunityPost;
import za.ac.cput.communitystore.factory.CommunityPostFactory;
import za.ac.cput.communitystore.repository.CommunityPostRepository;
import java.util.List;
@Service
@Transactional
public class CommunityPostService implements ICommunityPostService {
    private final CommunityPostRepository posts;
    private final Backend3Access access;
    public CommunityPostService(CommunityPostRepository posts, Backend3Access access) { this.posts=posts; this.access=access; }
    public CommunityPost create(String title,String content) { return posts.save(CommunityPostFactory.createCommunityPost(access.current(),title,content)); }
    @Transactional(readOnly=true) public CommunityPost read(int id) { return access.required(posts.findById(id),"Post"); }
    @Transactional(readOnly=true) public List<CommunityPost> getAll() { return posts.findAllByOrderByPostDateDesc(); }
    public CommunityPost update(int id,String title,String content) {
        var existing=read(id); access.ownerOrAdmin(existing.getUser());
        return posts.save(CommunityPostFactory.withDetails(existing,title,content));
    }
    public void delete(int id) { var existing=read(id); access.ownerOrAdmin(existing.getUser()); posts.delete(existing); }
}
