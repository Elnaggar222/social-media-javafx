package model;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;

    // NEW FIELDS
    private String bio;
    private String profileImagePath;

    // Constructor for registration
    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Full constructor
    public User(int id, String name, String email, String passwordHash,
                LocalDateTime createdAt,
                String bio,
                String profileImagePath) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.bio = bio;
        this.profileImagePath = profileImagePath;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getBio() { return bio; }
    public String getProfileImagePath() { return profileImagePath; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setBio(String bio) { this.bio = bio; }
    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}