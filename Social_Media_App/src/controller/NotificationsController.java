package controller;

import dao.NotificationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import model.Notification;
import model.User;
import util.getRelativeTime;

import java.util.List;

public class NotificationsController {

	@FXML
	private ListView<Notification> notificationsListView;

	@FXML
	private Label messageLabel;

	private User currentUser;
	private final NotificationDAO notificationDAO = new NotificationDAO();

	public void setUser(User user) {
		this.currentUser = user;
		loadNotifications();
	}

	@FXML
	private void loadNotifications() {

		List<Notification> list = notificationDAO.getNotifications(currentUser.getId());

		if (list.isEmpty()) {
			messageLabel.setText("No notifications yet.");
			notificationsListView.getItems().clear();
			return;
		}

		messageLabel.setText("");

		// Put objects directly inside ListView
		notificationsListView.getItems().setAll(list);

		// Control how each notification looks
		notificationsListView.setCellFactory(listView -> new ListCell<>() {
			@Override
			protected void updateItem(Notification item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setText(null);
				} else {

					String text = (item.isRead() ? "✔ " : "🔴 ") + item.getMessage() + " • "
							+ getRelativeTime.formatTimeAgo(item.getCreatedAt());

					setText(text);
				}
			}
		});
	}

	@FXML
	private void handleMarkAsRead() {

		Notification selected = notificationsListView.getSelectionModel().getSelectedItem();

		if (selected == null) {
			messageLabel.setText("Select a notification first.");
			return;
		}

		notificationDAO.markAsRead(selected.getId());

		messageLabel.setText("Marked as read ✅");

		loadNotifications();
	}
}