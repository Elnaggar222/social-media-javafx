package controller;

import dao.FriendDAO;
import dao.NotificationDAO;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.FriendRequest;
import model.User;
import util.Toast;

import java.util.List;

public class FriendRequestController {

	@FXML
	private ListView<FriendRequest> requestsListView;

	@FXML
	private TextField receiverField;

	@FXML
	private Label messageLabel;

	private final FriendDAO friendDAO = new FriendDAO();

	private User currentUser;

	// 🔹 This must be called after loading FXML
	public void setCurrentUser(User user) {
		this.currentUser = user;
		loadRequests();
	}

	private void loadRequests() {
		List<FriendRequest> requests = friendDAO.getPendingRequests(currentUser.getId());

		requestsListView.getItems().setAll(requests);

		// Custom display
		requestsListView.setCellFactory(list -> new ListCell<>() {
			@Override
			protected void updateItem(FriendRequest item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getSenderName());
				}
			}
		});
	}

	@FXML
	private void handleAccept() {

		FriendRequest selected = requestsListView.getSelectionModel().getSelectedItem();

		if (selected == null) {
			Toast.show("Select a request first", Toast.Type.ERROR);
			return;
		}

		if (friendDAO.acceptRequest(selected.getId())) {
			Toast.show("Friend request accepted", Toast.Type.SUCCESS);
			loadRequests();
		}
	}

	@FXML
	private void handleReject() {

		FriendRequest selected = requestsListView.getSelectionModel().getSelectedItem();

		if (selected == null) {
			Toast.show("Select a request first", Toast.Type.ERROR);
			return;
		}

		if (friendDAO.rejectRequest(selected.getId())) {
			Toast.show("Request rejected", Toast.Type.INFO);
			loadRequests();
		}
	}

	@FXML
	private void handleSendRequest() {

		String username = receiverField.getText().trim();

		if (username.isEmpty()) {
			messageLabel.setText("Enter username");
			return;
		}

		try {
			int receiverId = UserDAO.findUserIdByUsername(username);

			if (receiverId == currentUser.getId()) {
				messageLabel.setText("You can't add yourself");
				return;
			}

			boolean sent = friendDAO.sendRequest(currentUser.getId(), receiverId);

			if (sent) {
				// 🔔 Friend Request Notification
				NotificationDAO notificationDAO = new NotificationDAO();
				notificationDAO.createNotification(receiverId, currentUser.getId(), "FRIEND_REQUEST", 0,
						currentUser.getName() + " sent you a friend request");
				Toast.show("Request sent successfully", Toast.Type.SUCCESS);
				receiverField.clear();
				messageLabel.setText("");
			} else {
				messageLabel.setText("Request already exists");
			}

		} catch (Exception e) {
			messageLabel.setText("User not found");
		}
	}
}