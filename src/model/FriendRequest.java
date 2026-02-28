package model;

import java.time.LocalDateTime;

public class FriendRequest {

    private int id;
    private int senderId;
    private int receiverId;
    private String senderName;
    private LocalDateTime createdAt;
    private String status;

    public FriendRequest(int id, int senderId, int receiverId,
                         String senderName,
                         LocalDateTime createdAt,
                         String status) {

        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getId() { return id; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getSenderName() { return senderName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
}