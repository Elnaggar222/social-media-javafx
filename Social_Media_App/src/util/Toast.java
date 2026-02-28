package util;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {

    public enum Type {
        SUCCESS, ERROR, INFO
    }

    public static void show(String message, Type type) {

        Stage stage = SceneManager.getPrimaryStage();

        Popup popup = new Popup();
        Label label = new Label(message);

        // 🎨 Color Based On Type
        String backgroundColor;

        switch (type) {
            case SUCCESS:
                backgroundColor = "#2ecc71"; // green
                break;
            case ERROR:
                backgroundColor = "#e74c3c"; // red
                break;
            default:
                backgroundColor = "#3498db"; // blue
        }

        label.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 15 30 15 30;" +
                "-fx-background-radius: 20;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;"
        );

        popup.getContent().add(label);
        popup.setAutoHide(true);
        popup.show(stage);

        // 📍 Position Top Center
        popup.setX(stage.getX() + stage.getWidth() / 2 - 150);
        popup.setY(stage.getY() + 40);

        // 🎬 Slide Down Animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), label);
        slide.setFromY(-20);
        slide.setToY(0);
        slide.play();

        // ⏳ Fade Out After 5 Seconds
        FadeTransition fade = new FadeTransition(Duration.seconds(5), label);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDelay(Duration.seconds(4));

        fade.setOnFinished(e -> popup.hide());
        fade.play();
    }
}