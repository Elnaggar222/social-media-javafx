package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import model.User;
import util.SceneManager;
import util.Toast;
import javafx.scene.layout.Priority;

public class MenuController {

	@FXML
	private VBox contentArea;

	private User loggedInUser;

	public void setUser(User user) {
		this.loggedInUser = user;
		loadNewsFeed();
	}

	@FXML
	private void goToNewsFeed() {
		loadNewsFeed();
	}

	private void loadNewsFeed() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newsfeed.fxml"));
			Parent view = loader.load();

			NewFeedsController controller = loader.getController();
			controller.setUser(loggedInUser);

			contentArea.getChildren().setAll(view);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToProfile() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profile.fxml"));
			Parent view = loader.load();

			ProfileController controller = loader.getController();
			controller.setUser(loggedInUser);

			VBox.setVgrow(view, Priority.ALWAYS);
			contentArea.getChildren().setAll(view);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToFriends() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/friends.fxml"));
			Parent view = loader.load();

			FriendsController controller = loader.getController();
			controller.setCurrentUser(loggedInUser);

			VBox.setVgrow(view, Priority.ALWAYS);
			contentArea.getChildren().setAll(view);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToFriendRequests() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/friendrequest.fxml"));
			Parent view = loader.load();

			FriendRequestController controller = loader.getController();
			controller.setCurrentUser(loggedInUser);

			VBox.setVgrow(view, Priority.ALWAYS);
			contentArea.getChildren().setAll(view);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToNotifications() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/notification.fxml"));

			Parent view = loader.load();

			NotificationsController controller = loader.getController();

			controller.setUser(loggedInUser);

			VBox.setVgrow(view, Priority.ALWAYS);
			contentArea.getChildren().setAll(view);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToSearch() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
			Parent view = loader.load();

			SearchController controller = loader.getController();
			controller.setUser(loggedInUser);

			VBox.setVgrow(view, Priority.ALWAYS);
			contentArea.getChildren().setAll(view);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToLogin() {
		System.out.println("Logout clicked");
	}

	@FXML
	private void handleLogout() {
		loggedInUser = null;
		SceneManager.switchScene("/view/login.fxml", "Login");
		Toast.show("Logout successful ✅", Toast.Type.SUCCESS);
	}

}
