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

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    // for error message text 
    @FXML
    private Label messageLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("All fields are required!");
            return;
        }

        String hashed = PasswordUtil.hashPassword(password);
        User newUser = new User(name, email, hashed);

        boolean success = userDAO.register(newUser);

        if (success) {
        	Toast.show("Registration successful 🎉", Toast.Type.SUCCESS);
            goToLogin();
        } else {
        	messageLabel.setText("Email already exists.");
        	Toast.show("Email already exists ❌", Toast.Type.ERROR);
        }
    }

    @FXML
    private void goToLogin() {
        SceneManager.switchScene("/view/login.fxml", "Login");
    }
}