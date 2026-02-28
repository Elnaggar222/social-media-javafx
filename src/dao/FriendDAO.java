package dao;

import model.User;
import model.FriendRequest;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {

    // Send Friend Request
    public boolean sendRequest(int senderId, int receiverId) {

        String sql = "INSERT INTO friend_requests (sender_id, receiver_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    // Get Pending Requests for current user
    public List<FriendRequest> getPendingRequests(int userId) {

        List<FriendRequest> list = new ArrayList<>();

        String sql = """
            SELECT fr.*, u.name AS sender_name
            FROM friend_requests fr
            JOIN users u ON fr.sender_id = u.id
            WHERE fr.receiver_id = ? AND fr.status = 'PENDING'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new FriendRequest(
                        rs.getInt("id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("sender_name"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Accept Request
    public boolean acceptRequest(int requestId) {
        return updateStatus(requestId, "ACCEPTED");
    }

    // Reject Request
    public boolean rejectRequest(int requestId) {
        return updateStatus(requestId, "REJECTED");
    }

    private boolean updateStatus(int requestId, String status) {

        String sql = "UPDATE friend_requests SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, requestId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get Friends List
    public List<User> getFriends(int userId) {

        List<User> friends = new ArrayList<>();

        String sql = """
            SELECT u.*
            FROM users u
            JOIN friend_requests fr
              ON (u.id = fr.sender_id OR u.id = fr.receiver_id)
            WHERE (fr.sender_id = ? OR fr.receiver_id = ?)
              AND fr.status = 'ACCEPTED'
              AND u.id != ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                friends.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("bio"),
                        rs.getString("profile_image")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }
}