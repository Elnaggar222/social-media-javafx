package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Comment;
import model.Post;
import model.User;
import util.ImageUtil;
import util.Toast;
import util.constants;
import util.getRelativeTime;
import dao.CommentDAO;
import dao.LikeDAO;
import dao.NotificationDAO;
import dao.PostDAO;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;

public class NewFeedsController {
	// comments and likes
	private LikeDAO likeDAO = new LikeDAO();
	private CommentDAO commentDAO = new CommentDAO();
	// Post
	private PostDAO postDAO = new PostDAO();

	@FXML
	private TextArea postContentArea;

	@FXML
	private VBox feedVBox;

	@FXML
	private ComboBox<String> privacyComboBox;

	private User loggedInUser;

	// For Post Image
	@FXML
	private ImageView previewImageView;
	@FXML
	private Button removeImageBtn;
	private String selectedImagePath = null;
	@FXML
	private Label imageNameLabel;

	@FXML
	public void initialize() {
		if (privacyComboBox != null) {
			privacyComboBox.getItems().addAll("PUBLIC", "FRIENDS", "PRIVATE");
			privacyComboBox.setValue("PUBLIC");
		}
	}

	// ================== IMAGE UPLOAD ==================
	@FXML
	private void handleAddImage() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			try {

				// ✅ Delete previous temp image ONLY if not yet saved in DB
				if (selectedImagePath != null) {
					ImageUtil.deleteFile(selectedImagePath);
				}

				selectedImagePath = ImageUtil.copyImageToUploads(file);

				ImageUtil.setPreviewImage(previewImageView, selectedImagePath, constants.POST_IMAGE_WIDTH,
						constants.POST_IMAGE_HEIGHT);
				previewImageView.setVisible(true);
				imageNameLabel.setText(file.getName());
				imageNameLabel.setVisible(true);
				removeImageBtn.setVisible(true);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void handleRemoveImage() {
		ImageUtil.deleteFile(selectedImagePath);
		selectedImagePath = null;
		ImageUtil.clearPreview(previewImageView);
		imageNameLabel.setText(null);
		imageNameLabel.setVisible(false);
		removeImageBtn.setVisible(false);
		Toast.show("Image Removed ❌", Toast.Type.INFO);
	}

	// ================== LOGIN ==================
	// This will be called after login
	public void setUser(User user) {
		this.loggedInUser = user;
		loadPosts();
	}

	// ================== POST CARD ==================
	private VBox createPostCard(Post post) {

		VBox postCard = new VBox();
		postCard.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; "
				+ "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5,0,0,2);");
		postCard.setSpacing(5);

		// Username
		Label userName = new Label(post.getUserName());
		userName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: black;");

		// Content
		Label content = null;
		if (post.getContent() != null && !post.getContent().trim().isEmpty()) {
			content = new Label(post.getContent());
			content.setWrapText(true);
			content.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");
		}

		// Post Image PreView
		ImageView imageView = ImageUtil.createImageView(post.getImagePath(), constants.POST_IMAGE_WIDTH,
				constants.POST_IMAGE_HEIGHT);

		// Timestamp
		Label timestamp;

		if (post.isEdited() && post.getEditedAt() != null) {

			String editedText = "Edited " + getRelativeTime.formatTimeAgo(post.getEditedAt());
			timestamp = new Label(editedText);

		} else {

			String createdText = getRelativeTime.formatTimeAgo(post.getCreatedAt());
			timestamp = new Label(createdText);
		}
		timestamp.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

		// Privacy
		Label privacyLabel = new Label(post.getPrivacy());
		privacyLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

		// LIKE BUTTON
		Button likeBtn = new Button();
		likeBtn.setStyle("-fx-background-color: #4267B2; -fx-text-fill: white;");

		if (likeDAO.hasUserLiked(post.getId(), loggedInUser.getId())) {
			likeBtn.setText("Unlike (" + likeDAO.getLikesCount(post.getId()) + ")");
		} else {
			likeBtn.setText("Like (" + likeDAO.getLikesCount(post.getId()) + ")");
		}

		likeBtn.setOnAction(e -> {

			if (likeDAO.hasUserLiked(post.getId(), loggedInUser.getId())) {
				likeDAO.removeLike(post.getId(), loggedInUser.getId());
			} else {
				likeDAO.addLike(post.getId(), loggedInUser.getId());
				// Like Notfication
				NotificationDAO notificationDAO = new NotificationDAO();

				if (post.getUserId() != loggedInUser.getId()) {
					notificationDAO.createNotification(post.getUserId(), loggedInUser.getId(), "LIKE", post.getId(),
							loggedInUser.getName() + " liked your post");
				}
			}

			int updatedCount = likeDAO.getLikesCount(post.getId());

			if (likeDAO.hasUserLiked(post.getId(), loggedInUser.getId())) {
				likeBtn.setText("Unlike (" + updatedCount + ")");
			} else {
				likeBtn.setText("Like (" + updatedCount + ")");
			}
		});

		// COMMENT BUTTON
		Button commentBtn = new Button("Comment (" + commentDAO.getCommentsCount(post.getId()) + ")");
		commentBtn.setStyle("-fx-background-color: #888; -fx-text-fill: white;");

		VBox commentsBox = new VBox();
		commentsBox.setSpacing(3);
		commentsBox.setStyle("-fx-padding: 5 0 0 10;");

		List<Comment> comments = commentDAO.getComments(post.getId());

		for (Comment comment : comments) {
			Label commentLabel = new Label(comment.getUserName() + ": " + comment.getContent());
			commentLabel.setWrapText(true);
			commentLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
			commentsBox.getChildren().add(commentLabel);
		}

		commentBtn.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Add Comment");
			dialog.setHeaderText("Add a comment:");
			dialog.setContentText("Comment:");

			dialog.showAndWait().ifPresent(text -> {

				if (!text.isEmpty()) {

					Comment newComment = new Comment(0, post.getId(), loggedInUser.getId(), loggedInUser.getName(),
							text, null);

					if (commentDAO.addComment(newComment)) {
						// 🔔 Comment Notification
						if (post.getUserId() != loggedInUser.getId()) {

						    NotificationDAO notificationDAO = new NotificationDAO();

						    notificationDAO.createNotification(
						            post.getUserId(),
						            loggedInUser.getId(),
						            "COMMENT",
						            post.getId(),
						            loggedInUser.getName() + " commented on your post"
						    );
						}
						Label newCommentLabel = new Label(loggedInUser.getName() + ": " + text);
						newCommentLabel.setWrapText(true);
						newCommentLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");

						commentsBox.getChildren().add(newCommentLabel);

						commentBtn.setText("Comment (" + commentDAO.getCommentsCount(post.getId()) + ")");
					}
				}
			});
		});

		// DELETE BUTTON
		Button deleteBtn = new Button("Delete");
		deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

		if (post.getUserId() == loggedInUser.getId()) {

			deleteBtn.setOnAction(e -> {
				if (postDAO.deletePost(post.getId(), loggedInUser.getId())) {
					// delete image file if exists
					ImageUtil.deleteFile(post.getImagePath());
					feedVBox.getChildren().remove(postCard);
					Toast.show("Post Deleted ✅", Toast.Type.INFO);
				}
			});

		} else {
			deleteBtn.setVisible(false);
		}
		// EDIT BUTTON
		Button editBtn = new Button("Edit");
		editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
		if (post.getUserId() == loggedInUser.getId()) {
			editBtn.setOnAction(e -> openEditDialog(post));

		} else {
			editBtn.setVisible(false);
		}

		HBox buttonBox = new HBox(10, likeBtn, commentBtn, editBtn, deleteBtn);

		if (imageView != null && content != null) {
			postCard.getChildren().addAll(userName, content, imageView, timestamp, privacyLabel, buttonBox,
					commentsBox);
		} else if (imageView != null) {
			postCard.getChildren().addAll(userName, imageView, timestamp, privacyLabel, buttonBox, commentsBox);
		} else {
			postCard.getChildren().addAll(userName, content, timestamp, privacyLabel, buttonBox, commentsBox);
		}
		return postCard;
	}

	private void openEditDialog(Post post) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit Post");

		VBox dialogVBox = new VBox(10);

		TextArea editTextArea = new TextArea(post.getContent());

		ComboBox<String> editPrivacyCombo = new ComboBox<>();
		editPrivacyCombo.getItems().addAll("PUBLIC", "FRIENDS", "PRIVATE");
		editPrivacyCombo.setValue(post.getPrivacy());

		// ✅ If post has no image, create empty ImageView
		ImageView editImageView;

		if (post.getImagePath() != null) {
			editImageView = ImageUtil.createImageView(post.getImagePath(), constants.POST_IMAGE_WIDTH,
					constants.POST_IMAGE_HEIGHT);
		} else {
			editImageView = new ImageView();
			editImageView.setFitWidth(constants.POST_IMAGE_WIDTH);
			editImageView.setFitHeight(constants.POST_IMAGE_HEIGHT);
			editImageView.setPreserveRatio(true);
		}

		// Track image states safely
		String originalImagePath = post.getImagePath();
		final String[] tempImagePath = { originalImagePath };
		final boolean[] imageRemoved = { false };
		final boolean[] imageChanged = { false };

		Button changeImageBtn = new Button("Change Image");
		Button removeImageBtnDialog = new Button("Remove Image");

		// ✅ Change Image (only temp copy)
		changeImageBtn.setOnAction(ev -> {

			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters()
					.add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

			File file = fileChooser.showOpenDialog(dialog.getOwner());

			if (file != null) {
				try {

					String newTempPath = ImageUtil.copyImageToUploads(file);

					editImageView.setImage(new Image(new File(newTempPath).toURI().toString()));

					editImageView.setFitWidth(constants.POST_IMAGE_WIDTH);
					editImageView.setFitHeight(constants.POST_IMAGE_HEIGHT);
					editImageView.setPreserveRatio(true);

					tempImagePath[0] = newTempPath;
					imageChanged[0] = true;
					imageRemoved[0] = false;

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});

		// ✅ Just mark removed (do NOT delete file yet)
		removeImageBtnDialog.setOnAction(ev -> {

			editImageView.setImage(null);
			tempImagePath[0] = null;
			imageRemoved[0] = true;
			imageChanged[0] = false;
		});

		HBox imageButtons = new HBox(10, changeImageBtn, removeImageBtnDialog);
		dialogVBox.getChildren().addAll(editTextArea, editPrivacyCombo, editImageView, imageButtons);

		dialog.getDialogPane().setContent(dialogVBox);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(result -> {

			if (result == ButtonType.OK) {

				String newText = editTextArea.getText();
				newText = newText != null ? newText.trim() : "";
				// Reject only if both empty
				if (newText.isEmpty() && tempImagePath[0] == null) {
					Toast.show("Post must contain text or image ❗", Toast.Type.INFO);
					return;
				}

				// ✅ If removed → delete old file
				if (imageRemoved[0] && originalImagePath != null) {
					ImageUtil.deleteFile(originalImagePath);
				}

				// ✅ If replaced → delete old file
				if (imageChanged[0] && originalImagePath != null) {
					ImageUtil.deleteFile(originalImagePath);
				}

				String updatedPrivacy = editPrivacyCombo.getValue();

				boolean updated = postDAO.updatePost(post.getId(), loggedInUser.getId(), newText, tempImagePath[0],
						updatedPrivacy);

				if (updated) {
					loadPosts();
					Toast.show("Post Updated ✅", Toast.Type.SUCCESS);
				}

			} else {
				// ❌ If user cancelled AND image was changed
				// delete the newly copied temp image
				if (imageChanged[0] && tempImagePath[0] != null && !tempImagePath[0].equals(originalImagePath)) {

					ImageUtil.deleteFile(tempImagePath[0]);
				}
			}
		});
	}

	private void loadPosts() {

		feedVBox.getChildren().clear();

		if (loggedInUser == null)
			return;

		List<Post> posts = postDAO.getPostsForUser(loggedInUser.getId());

		for (Post post : posts) {
			feedVBox.getChildren().add(createPostCard(post));
		}
	}

	@FXML
	private void handlePost() {

		String content = postContentArea.getText() != null ? postContentArea.getText().trim() : "";

		// ❌ Reject only if BOTH are empty
		if (content.isEmpty() && selectedImagePath == null) {
			Toast.show("Post must contain text or image ❗", Toast.Type.INFO);
			return;
		}

		String privacy = privacyComboBox.getValue();

		Post newPost = new Post(loggedInUser.getId(), content, selectedImagePath, privacy);

		if (postDAO.createPost(newPost)) {
			loadPosts();
			Toast.show("Post Created ✅", Toast.Type.SUCCESS);

			// Clear UI
			selectedImagePath = null;
			ImageUtil.clearPreview(previewImageView);
			previewImageView.setVisible(false);
			removeImageBtn.setVisible(false);
			imageNameLabel.setVisible(false);
			postContentArea.clear();
		}
	}

}