package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import util.PasswordUtil;
import util.SceneManager;
import util.Toast;
import javafx.fxml.FXMLLoader;

public class LoginController {

	@FXML
	private TextField emailField;

	@FXML
	private PasswordField passwordField;
	// for error message text
	@FXML
	private Label messageLabel;

	private UserDAO userDAO = new UserDAO();

	@FXML
	private void handleLogin() {

		String email = emailField.getText();
		String password = passwordField.getText();
		
	    if (email.isEmpty() || password.isEmpty()) {
	        messageLabel.setText("Please fill all fields.");
	        return;
	    }

		String hashed = PasswordUtil.hashPassword(password);
		User user = userDAO.login(email, hashed);

		if (user != null) {
			Toast.show("Login successful ✅", Toast.Type.SUCCESS);
			openHomeScreen(user);
		} else {
			messageLabel.setText("Invalid email or password.");
			Toast.show("Invalid email or password ❌", Toast.Type.ERROR);
		}
	}

	@FXML
	private void handleRegister() {
		SceneManager.switchScene("/view/register.fxml", "Register");
	}

	private void openHomeScreen(User user) {

	    FXMLLoader loader = SceneManager.switchSceneWithLoader("/view/Menu.fxml", "Home");

	    if (loader != null) {
	        MenuController menuController = loader.getController();
	        menuController.setUser(user);
	    }
	}
}