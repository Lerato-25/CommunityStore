package za.ac.cput.communitystore.factory;
import za.ac.cput.communitystore.domain.*;
import java.time.LocalDateTime;
public final class CommunityPostFactory {
    private CommunityPostFactory() { }
    public static CommunityPost createCommunityPost(User user, String title, String content) {
        if (user == null || user.getUserID() <= 0) throw new IllegalArgumentException("existing author is required");
        return new CommunityPost.Builder().setUser(user).setTitle(BackendValidation.text(title,"title",150))
            .setContent(BackendValidation.text(content,"content",10000)).setPostDate(LocalDateTime.now()).build();
    }
    public static CommunityPost withDetails(CommunityPost existing, String title, String content) {
        return new CommunityPost.Builder().copy(existing).setTitle(BackendValidation.text(title,"title",150))
            .setContent(BackendValidation.text(content,"content",10000)).build();
    }
}
