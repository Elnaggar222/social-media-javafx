package model;

import java.time.LocalDateTime;

public class Post {

	private int id;
	private int userId;
	private String userName; // NEW FIELD
	private String content;
	private String imagePath;
	private boolean edited;
	private LocalDateTime createdAt;
	private LocalDateTime editedAt;
	private String privacy;

	// Constructor for new posts
	public Post(int userId, String content, String imagePath, String privacy) {
	    this.userId = userId;
	    this.content = content;
	    this.imagePath = imagePath;
	    this.privacy = privacy;
	}

	// Constructor for posts from DB (with username)
	public Post(int id, int userId, String userName,
            String content,
            LocalDateTime createdAt,
            boolean edited,
            LocalDateTime editedAt,
            String imagePath,
            String privacy
			) {

    this.id = id;
    this.userId = userId;
    this.userName = userName;
    this.content = content;
    this.createdAt = createdAt;
    this.edited = edited;
    this.editedAt = editedAt;
    this.imagePath = imagePath;
    this.privacy = privacy;
}
	// Setters
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	public void setEditedAt(LocalDateTime editedAt) {
	    this.editedAt = editedAt;
	}

	// Getters
	public int getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	} // NEW

	public String getContent() {
		return content;
	}
	public String getImagePath() {
	    return imagePath;
	}

	public boolean isEdited() {
		return edited;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public LocalDateTime getEditedAt() {
	    return editedAt;
	}
	public String getPrivacy() {
	    return privacy;
	}
}