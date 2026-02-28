# SocialApp - JavaFX Social Media Application

## Project Overview
**SocialApp** is a desktop social media application built with **JavaFX** and **MySQL**. It allows users to register, log in, create posts with text and images, manage their profile, interact with posts via likes and comments, send and accept friend requests, search for users and posts, and receive notifications. The app follows an MVC architecture with clear separation of concerns (Model, View, Controller, DAO, Utilities).

---

## Table of Contents
1. [Requirements](#requirements)  
2. [Project Structure](#project-structure)  
3. [Features](#features)  
   - [Core Features](#core-features)  
   - [Advanced Features](#advanced-features)  
4. [Technologies Used](#technologies-used)  
5. [Database Setup](#database-setup)  
6. [Usage Instructions](#usage-instructions)  
7. [Future Improvements](#future-improvements)  
8. [Screenshots](#screenshots)

---

## Requirements
- Java 17 or higher  
- JavaFX 17 (or bundled with JDK)  
- MySQL Server 8+  
- IDE: IntelliJ / Eclipse / VS Code (optional)  
- CSS for styling (included)

---

## Project Structure
```
src/
│
├─ controller/
│ ├─ LoginController.java
│ ├─ RegisterController.java
│ ├─ MenuController.java
│ ├─ NewFeedsController.java
│ ├─ ProfileController.java
│ ├─ NotificationsController.java
│ ├─ FriendsController.java
│ ├─ FriendRequestController.java
│ ├─ SearchController.java
│
├─ dao/
│ ├─ UserDAO.java
│ ├─ PostDAO.java
│ ├─ CommentDAO.java
│ ├─ LikeDAO.java
│ ├─ FriendDAO.java
│ ├─ NotificationDAO.java
│
├─ model/
│ ├─ User.java
│ ├─ Post.java
│ ├─ Comment.java
│ ├─ Notification.java
│ ├─ FriendRequest.java
│
├─ util/
│ ├─ DBConnection.java
│ ├─ PasswordUtil.java
│ ├─ ImageUtil.java
│ ├─ SceneManager.java
│ ├─ Toast.java
│ ├─ getRelativeTime.java
│ ├─ constants.java
│
├─ MainApp.java (launcher)
│
└─ view/
   ├─ login.fxml
   ├─ register.fxml
   ├─ Menu.fxml
   ├─ newsfeed.fxml
   ├─ profile.fxml
   ├─ notification.fxml
   ├─ friends.fxml
   ├─ friendrequest.fxml
   ├─ search.fxml
   └─ styles.css
```

---

## Features

### Core Features
- **User Registration & Login**  
  - Secure password hashing (SHA‑256)  
  - Email and password validation  
  - Persistent user sessions

- **User Profile Management**  
  - Update name, email, bio  
  - Upload / change / delete profile picture  
  - Image stored locally and path saved in database

- **Posting Updates**  
  - Create posts with text and optional image  
  - Edit own posts (text, image, privacy)  
  - Delete own posts  
  - Choose privacy level: **PUBLIC**, **FRIENDS**, **PRIVATE**

- **News Feed**  
  - Dynamic feed showing posts from:
    - The logged‑in user
    - Friends (if post is set to **FRIENDS**)
    - All users (if post is **PUBLIC**)  
  - Posts sorted by newest first  
  - Relative timestamps (e.g., “5 mins ago”, “Just now”)

- **Likes and Comments**  
  - Like / unlike posts  
  - Comment on posts  
  - Real‑time update of like and comment counts  
  - View all comments under each post

- **Friend System**  
  - Send friend requests to other users by username  
  - Accept / reject incoming friend requests  
  - View list of friends  
  - Friend relationships stored in database with status tracking

### Advanced Features
- **Notifications**  
  - Receive notifications for:
    - Likes on your posts  
    - Comments on your posts  
    - Friend requests  
  - Unread / read status  
  - Mark individual notifications as read  
  - Notifications displayed with sender name and relative time

- **Search Functionality**  
  - Search for users by name (real-time results)  
  - Search for posts by content (respecting privacy settings)  
  - Results shown in a list view with previews

- **Privacy Settings**  
  - Every post can be set to **PUBLIC**, **FRIENDS**, or **PRIVATE**  
  - **PRIVATE** posts visible only to the owner  
  - **FRIENDS** posts visible only to accepted friends  
  - Privacy enforced in both feed display and search

---

## Technologies Used
- **Java 17** – Core language  
- **JavaFX 17** – Graphical user interface  
- **MySQL** – Relational database  
- **JDBC** – Database connectivity  
- **MVC Architecture** – Modular code organisation  
- **CSS** – Styling of JavaFX components  
- **File I/O** – Image upload and management

---

## Database Setup
1. Create a MySQL database named `social_media`.
2. Run the provided `social_media.sql` script to create all tables:
   - `users`
   - `posts`
   - `comments`
   - `likes`
   - `friend_requests`
   - `notifications`
3. Update database credentials in `util/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/social_media";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

---

## Usage Instructions

### Compile and Run
Ensure Java 17 and JavaFX are installed.
Run `MainApp.java` from your IDE or using the provided JAR.

### First Time
Register a new account (name, email, password). After registration, log in with your credentials.

### Navigation
The left sidebar gives access to all main sections:
- **News Feed** – Create posts, view feed, like, comment, edit/delete your posts.
- **Profile** – Update your profile information and picture.
- **Notifications** – View and manage your notifications.
- **Friend Requests** – See incoming requests and send new ones.
- **Friends** – List your current friends.
- **Search** – Search for users or posts.
- **Logout** – End your session.

### Posting
On the news feed, write your post, optionally add an image, choose privacy, and click Post.
To edit a post, click the Edit button on your own post.
To delete, click Delete.

### Interacting
Click Like / Unlike on any post.
Click Comment to add a comment.

### Friend Management
Go to Friend Requests, enter a username, and send a request.
Incoming requests appear in the left panel; accept or reject them.
Accepted friends appear in Friends.

### Search
Enter a keyword and choose Users or Posts. Results are displayed in the list.

---

## Future Improvements
- Real‑Time Chat – Implement a messaging system using sockets or WebSockets.
- Group Posts – Allow sharing posts with specific friend groups.
- Email Verification – Send confirmation emails on registration.
- Password Reset – Forgot password functionality.
- Dark Mode – Theme toggle.
- Post Pagination – Load posts in batches for performance.

---

## Screenshots
- **News Feed**  
  <img width="1920" height="1080" alt="newsFeed" src="https://github.com/user-attachments/assets/4e46ab16-f770-4ed4-b155-78f92c389d64" />
  Main feed showing posts, like/comment buttons, and post creation area.

- **Profile Page**  
   <img width="1920" height="1080" alt="profile" src="https://github.com/user-attachments/assets/c39e2379-8f2c-4796-8f8d-bd8332696859" />
  User profile with editable fields and profile picture upload.

- **Friend Requests**  
  <img width="1920" height="1080" alt="friendRequest" src="https://github.com/user-attachments/assets/1f670d31-4841-46a0-9a6c-820070fe764c" />
  Incoming friend requests and the option to send new requests.

- **Friends List**  
  <img width="1920" height="1080" alt="Friends" src="https://github.com/user-attachments/assets/e738b05c-2536-4825-879b-62b580533883" />
  List of accepted friends.

- **Notifications**  
  <img width="1920" height="1080" alt="Notifications" src="https://github.com/user-attachments/assets/e608c76d-6b23-458c-bf6b-d19b40bf9e09" />
  Likes , Comments and Friend Requests Notifications
  
- **Search Results**  
  <img width="1920" height="1080" alt="user_post_search" src="https://github.com/user-attachments/assets/d2d9235b-f9de-476a-ba84-97a9bdb185bc" />
  Search for users or posts with results displayed.
