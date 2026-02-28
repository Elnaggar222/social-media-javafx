package dao;

import model.Post;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

	// Save a new post
	public boolean createPost(Post post) {
		String sql = "INSERT INTO posts (user_id, content, image_path, privacy) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, post.getUserId());
			stmt.setString(2, post.getContent());
			stmt.setString(3, post.getImagePath());
			stmt.setString(4, post.getPrivacy());

			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

// Get only posts from user and their accepted friends
	public List<Post> getPostsForUser(int userId) {

		List<Post> posts = new ArrayList<>();

		String sql = "SELECT p.id, p.user_id, u.name AS user_name, "
				+ "p.content, p.created_at, p.edited, p.edited_at, p.image_path, p.privacy " + "FROM posts p "
				+ "JOIN users u ON p.user_id = u.id " + "WHERE " +
				// Own posts (always visible)
				"p.user_id = ? " + "OR " +
				// PUBLIC posts from anyone
				"(p.privacy = 'PUBLIC') " + "OR " +
				// FRIENDS posts from accepted friends
				"(p.privacy = 'FRIENDS' AND p.user_id IN ( " + "   SELECT CASE "
				+ "       WHEN fr.sender_id = ? THEN fr.receiver_id " + "       ELSE fr.sender_id " + "   END "
				+ "   FROM friend_requests fr " + "   WHERE (fr.sender_id = ? OR fr.receiver_id = ?) "
				+ "   AND fr.status = 'ACCEPTED' " + ")) " + "ORDER BY p.created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			stmt.setInt(2, userId);
			stmt.setInt(3, userId);
			stmt.setInt(4, userId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				posts.add(new Post(rs.getInt("id"), rs.getInt("user_id"), rs.getString("user_name"),
						rs.getString("content"), rs.getTimestamp("created_at").toLocalDateTime(),
						rs.getBoolean("edited"),
						rs.getTimestamp("edited_at") != null ? rs.getTimestamp("edited_at").toLocalDateTime() : null,
						rs.getString("image_path"), rs.getString("privacy") // NEW
				));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}

	public boolean deletePost(int postId, int userId) {
		String sql = "DELETE FROM posts WHERE id = ? AND user_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, postId);
			stmt.setInt(2, userId);

			int rows = stmt.executeUpdate();
			return rows > 0; // true if deleted

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updatePost(int postId, int userId, String content, String imagePath, String privacy) {

		String sql = """
				UPDATE posts
				SET content = ?,
				image_path = ?,
				privacy = ?,
				edited = true,
				edited_at = CURRENT_TIMESTAMP
				WHERE id = ? AND user_id = ?
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, content);
			stmt.setString(2, imagePath);
			stmt.setString(3, privacy);
			stmt.setInt(4, postId);
			stmt.setInt(5, userId);

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Post> searchPosts(String keyword, int currentUserId) {

		List<Post> posts = new ArrayList<>();

		String sql = """
				    SELECT p.id, p.user_id, u.name AS user_name,
				           p.content, p.created_at,
				           p.edited, p.edited_at,
				           p.image_path, p.privacy
				    FROM posts p
				    JOIN users u ON p.user_id = u.id
				    WHERE p.content LIKE ?
				      AND (
				            p.privacy = 'PUBLIC'
				         OR (p.privacy = 'FRIENDS' AND p.user_id IN (
				                SELECT CASE
				                    WHEN fr.sender_id = ? THEN fr.receiver_id
				                    ELSE fr.sender_id
				                END
				                FROM friend_requests fr
				                WHERE (fr.sender_id = ? OR fr.receiver_id = ?)
				                AND fr.status = 'ACCEPTED'
				            ))
				         OR p.user_id = ?
				      )
				    ORDER BY p.created_at DESC
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, "%" + keyword + "%");
			stmt.setInt(2, currentUserId);
			stmt.setInt(3, currentUserId);
			stmt.setInt(4, currentUserId);
			stmt.setInt(5, currentUserId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				posts.add(new Post(rs.getInt("id"), rs.getInt("user_id"), rs.getString("user_name"),
						rs.getString("content"), rs.getTimestamp("created_at").toLocalDateTime(),
						rs.getBoolean("edited"),
						rs.getTimestamp("edited_at") != null ? rs.getTimestamp("edited_at").toLocalDateTime() : null,
						rs.getString("image_path"), rs.getString("privacy")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}
}