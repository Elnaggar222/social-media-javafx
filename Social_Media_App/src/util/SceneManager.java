package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
	private static final double APP_WIDTH = 1000;
	private static final double APP_HEIGHT = 700;
    private static Stage primaryStage;

    // Initialize once from Main class
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void switchScene(String fxmlPath, String title) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource(fxmlPath)
            );

            Parent root = loader.load();

            Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
         // Add CSS file
            scene.getStylesheets().add(
                    SceneManager.class.getResource("/view/styles.css").toExternalForm()
            );
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            // 🔒 Lock size
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Special method when we need controller access (like Home with user)
    public static FXMLLoader switchSceneWithLoader(String fxmlPath, String title) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource(fxmlPath)
            );

            Parent root = loader.load();
            Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
            // Add CSS file
            scene.getStylesheets().add(
                    SceneManager.class.getResource("/view/styles.css").toExternalForm()
            );
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

            return loader;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}