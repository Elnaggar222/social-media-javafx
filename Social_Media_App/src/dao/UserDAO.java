package dao;

import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class UserDAO {

	// Register User
	public boolean register(User user) {

		String sql = "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPasswordHash());

			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			System.out.println("❌ Registration failed");
			e.printStackTrace();
			return false;
		}
	}

	// Login User
	public User login(String email, String passwordHash) {

		String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			stmt.setString(2, passwordHash);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
						rs.getString("password_hash"), rs.getTimestamp("created_at").toLocalDateTime(),
						rs.getString("bio"), rs.getString("profile_image"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean updateUser(User user) {

		String sql = "UPDATE users SET name=?, email=?, bio=?, profile_image=? WHERE id=?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getBio());
			stmt.setString(4, user.getProfileImagePath());
			stmt.setInt(5, user.getId());

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Find user ID by username
	public static int findUserIdByUsername(String username) throws Exception {
		String sql = "SELECT id FROM users WHERE name = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			} else {
				throw new Exception("User not found");
			}
		}
	}

	// Find username by ID
	public static String findUsernameById(int id) throws Exception {
	    String sql = "SELECT name FROM users WHERE id = ?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("name");
	        } else {
	            throw new Exception("User not found");
	        }
	    }
	}

	public List<User> searchUsers(String keyword) {

		List<User> users = new java.util.ArrayList<>();

		String sql = "SELECT * FROM users WHERE name LIKE ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, "%" + keyword + "%");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
						rs.getString("password_hash"), rs.getTimestamp("created_at").toLocalDateTime(),
						rs.getString("bio"), rs.getString("profile_image")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}
}