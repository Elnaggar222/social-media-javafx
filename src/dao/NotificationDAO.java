package dao;

import model.Notification;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

	public void createNotification(int userId, int senderId, String type, int referenceId, String message) {

		String sql = """
				    INSERT INTO notifications
				    (user_id, sender_id, type, reference_id, message)
				    VALUES (?, ?, ?, ?, ?)
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			stmt.setInt(2, senderId);
			stmt.setString(3, type);
			stmt.setInt(4, referenceId);
			stmt.setString(5, message);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Notification> getNotifications(int userId) {

		List<Notification> list = new ArrayList<>();

		String sql = """
				    SELECT n.*, u.name AS sender_name
				    FROM notifications n
				    JOIN users u ON n.sender_id = u.id
				    WHERE n.user_id = ?
				    ORDER BY n.created_at DESC
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(new Notification(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("sender_id"),
						rs.getString("sender_name"), rs.getString("type"), rs.getString("message"),
						rs.getBoolean("is_read"), rs.getTimestamp("created_at").toLocalDateTime()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void markAsRead(int notificationId) {

		String sql = "UPDATE notifications SET is_read = true WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, notificationId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getUnreadCount(int userId) {

		String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = false";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
}