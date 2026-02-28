package model;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int postId;
    private int userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;

    public Comment(int id, int postId, int userId, String userName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public int getPostId() { return postId; }
    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}