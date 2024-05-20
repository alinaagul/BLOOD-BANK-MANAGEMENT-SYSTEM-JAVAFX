package application;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Exit extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Exit");

        StackPane exitPane = new StackPane();
        exitPane.setPadding(new Insets(20));

        Button exitButton = createExitButton();
        exitPane.getChildren().addAll(createBackground(), exitButton);

        Scene exitScene = new Scene(exitPane, 300, 200);
        addExitButtonAnimation(exitButton);

        primaryStage.setScene(exitScene);
        primaryStage.show();
    }

    private StackPane createBackground() {
        StackPane backgroundPane = new StackPane();
        backgroundPane.setStyle("-fx-background-color: #FFD289;");
        Button backgroundText = new Button("EXIT");
        backgroundText.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");
        backgroundText.setMouseTransparent(true);
        backgroundPane.getChildren().add(backgroundText);
        return backgroundPane;
    }

    private Button createExitButton() {
        String EXIT = "EXIT";
        Button button = new Button(EXIT);
        button.setStyle("-fx-font-size: 14; -fx-background-color: linear-gradient(to bottom, #8B0000, #000000);");
        button.setTextFill(Color.WHITE);
        button.setMinSize(150, 50);
        button.setEffect(new DropShadow(10, Color.GRAY));

        button.setOnAction(e -> showExitConfirmation());

        return button;
    }

    private void showExitConfirmation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Do you want to exit?");
        alert.setContentText("Press OK to EXIT, or CANCEL to cancel the EXIT.");

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            System.out.println("Exiting the system...");
            System.exit(0);
        } else {
            System.out.println("Exit action canceled.");
        }
    }

    private void addExitButtonAnimation(Button exitButton) {
        FillTransition fillTransition = new FillTransition(Duration.millis(500), Color.web("#FF6347"), Color.web("#8A2BE2"));
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);

        exitButton.setOnMouseEntered(event -> {
            fillTransition.playFromStart();
        });
    }
}
