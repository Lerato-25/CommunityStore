package za.ac.cput.communitystore.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="CommunityPosts")
public class CommunityPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="postId")
    private int postID;
    @ManyToOne @JoinColumn(name="userId", nullable=false)
    private User user;
    @Column(nullable=false, length=150)
    private String title;
    @Column(nullable=false, columnDefinition="TEXT")
    private String content;
    @Column(nullable=false)
    private LocalDateTime postDate;
    protected CommunityPost() { }
    private CommunityPost(Builder b) {
        this.postID = b.postID;
        this.user = b.user;
        this.title = b.title;
        this.content = b.content;
        this.postDate = b.postDate;
    }
    public int getPostID() { return postID; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getPostDate() { return postDate; }
    public static class Builder {
        private int postID;
        private User user;
        private String title;
        private String content;
        private LocalDateTime postDate;
        public Builder setPostID(int value) { this.postID = value; return this; }
        public Builder setUser(User value) { this.user = value; return this; }
        public Builder setTitle(String value) { this.title = value; return this; }
        public Builder setContent(String value) { this.content = value; return this; }
        public Builder setPostDate(LocalDateTime value) { this.postDate = value; return this; }
        public Builder copy(CommunityPost value) {
            this.postID = value.postID;
            this.user = value.user;
            this.title = value.title;
            this.content = value.content;
            this.postDate = value.postDate;
            return this;
        }
        public CommunityPost build() { return new CommunityPost(this); }
    }
}
