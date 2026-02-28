package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.User;
import util.ImageUtil;
import util.Toast;

import java.io.File;
import java.io.IOException;

public class ProfileController {

	@FXML
	private ImageView profileImageView;
	@FXML
	private TextField nameField;
	@FXML
	private TextField emailField;
	@FXML
	private TextArea bioField;
	@FXML
	private Label messageLabel;

	private User loggedInUser;
	private UserDAO userDAO = new UserDAO();

	// Image state tracking (SAME SAFE LOGIC)
	private String originalImagePath;
	private String tempImagePath;
	private boolean imageChanged = false;
	private boolean imageRemoved = false;

	public void setUser(User user) {
		this.loggedInUser = user;
		loadUserData();
	}

	private void loadUserData() {

		nameField.setText(loggedInUser.getName());
		emailField.setText(loggedInUser.getEmail());
		bioField.setText(loggedInUser.getBio());

		originalImagePath = loggedInUser.getProfileImagePath();
		tempImagePath = originalImagePath;

		if (originalImagePath != null) {
			profileImageView.setImage(new Image(new File(originalImagePath).toURI().toString()));
		}
	}

	@FXML
	private void handleUploadImage() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

		File file = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

		if (file != null) {
			try {

				String newTemp = ImageUtil.copyImageToUploads(file);

				profileImageView.setImage(new Image(new File(newTemp).toURI().toString()));

				tempImagePath = newTemp;
				imageChanged = true;
				imageRemoved = false;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void handleDeleteImage() {
	    if (tempImagePath != null) {
	        profileImageView.setImage(null);
	        imageRemoved = true;
	        imageChanged = false;
	        tempImagePath = null;
	        Toast.show("Profile Image Removed ❌", Toast.Type.INFO);
	    }
	}
	
	@FXML
	private void handleSaveProfile() {

		loggedInUser.setName(nameField.getText());
		loggedInUser.setEmail(emailField.getText());
		loggedInUser.setBio(bioField.getText());

		// If removed
		if (imageRemoved && originalImagePath != null) {
			ImageUtil.deleteFile(originalImagePath);
			tempImagePath = null;
		}

		// If replaced
		if (imageChanged && originalImagePath != null) {
			ImageUtil.deleteFile(originalImagePath);
		}

		loggedInUser.setProfileImagePath(tempImagePath);

		boolean updated = userDAO.updateUser(loggedInUser);

		if (updated) {
			originalImagePath = tempImagePath;
			imageChanged = false;
			imageRemoved = false;
			Toast.show("Profile Updated ✅", Toast.Type.SUCCESS);
		} else {
			Toast.show("Update Failed ❌", Toast.Type.ERROR);
		}
	}

	@FXML
	private void handleCancel() {

		// Reset fields
		loadUserData();

		// If temp image was newly selected, delete it
		if (imageChanged && tempImagePath != null && !tempImagePath.equals(originalImagePath)) {

			ImageUtil.deleteFile(tempImagePath);
		}

		imageChanged = false;
		imageRemoved = false;

		Toast.show("Changes Cancelled ❌", Toast.Type.INFO);
	}
}