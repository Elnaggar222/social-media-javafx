package controller;

import dao.PostDAO;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Post;
import model.User;

import java.util.List;

public class SearchController {

	@FXML
	private TextField searchField;

	@FXML
	private ListView<String> resultsListView;

	@FXML
	private Label messageLabel;

	private User loggedInUser;

	private final UserDAO userDAO = new UserDAO();
	private final PostDAO postDAO = new PostDAO();

	public void setUser(User user) {
		this.loggedInUser = user;
	}

	// ================= SEARCH USERS =================
	@FXML
	private void handleSearchUsers() {

		String keyword = searchField.getText().trim();

		if (keyword.isEmpty()) {
			messageLabel.setText("Enter search keyword");
			return;
		}

		List<User> users = userDAO.searchUsers(keyword);

		resultsListView.getItems().clear();

		if (users.isEmpty()) {
			messageLabel.setText("No users found");
			return;
		}

		messageLabel.setText("");

		for (User user : users) {
			resultsListView.getItems().add("👤 " + user.getName() + " (" + user.getEmail() + ")");
		}
	}

	// ================= SEARCH POSTS =================
	@FXML
	private void handleSearchPosts() {

		String keyword = searchField.getText().trim();

		if (keyword.isEmpty()) {
			messageLabel.setText("Enter search keyword");
			return;
		}

		List<Post> posts = postDAO.searchPosts(keyword, loggedInUser.getId());

		resultsListView.getItems().clear();

		if (posts.isEmpty()) {
			messageLabel.setText("No posts found");
			return;
		}

		messageLabel.setText("");

		for (Post post : posts) {

			String preview = post.getContent() != null && post.getContent().length() > 40
					? post.getContent().substring(0, 40) + "..."
					: post.getContent();

			resultsListView.getItems()
					.add("📝 " + post.getUserName() + ": " + preview + "  [" + post.getPrivacy() + "]");
		}
	}
}