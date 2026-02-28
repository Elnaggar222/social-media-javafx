import javafx.application.Application;
import javafx.stage.Stage;
import util.SceneManager;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

	    SceneManager.setPrimaryStage(primaryStage);

	    SceneManager.switchScene("/view/login.fxml", "Login");
	}

    public static void main(String[] args) {
        launch();
    }
}