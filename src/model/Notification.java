package model;

import java.time.LocalDateTime;

public class Notification {

	private int id;
	private int userId;
	private int senderId;
	private String senderName;
	private String type;
	private String message;
	private boolean isRead;
	private LocalDateTime createdAt;

	public Notification(int id, int userId, int senderId, String senderName, String type, String message,
			boolean isRead, LocalDateTime createdAt) {

		this.id = id;
		this.userId = userId;
		this.senderId = senderId;
		this.senderName = senderName;
		this.type = type;
		this.message = message;
		this.isRead = isRead;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public boolean isRead() {
		return isRead;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}