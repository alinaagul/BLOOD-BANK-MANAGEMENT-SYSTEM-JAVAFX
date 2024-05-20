package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactUs extends Application {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Contact Us - Blood Bank Management System");

        HBox mainPane = createMainPane();
        Scene contactScene = new Scene(mainPane, 800, 400);

        primaryStage.setScene(contactScene);
        primaryStage.show();
    }

    private HBox createMainPane() {
        HBox mainPane = new HBox();
        mainPane.setSpacing(20);
        mainPane.setAlignment(Pos.CENTER);

        VBox imagePane = createImagePane();
        VBox contactPane = createContactPane();

        mainPane.getChildren().addAll(imagePane, contactPane);

        return mainPane;
    }

    private VBox createImagePane() {
        VBox imagePane = new VBox();
        imagePane.setAlignment(Pos.CENTER);

        Image image = new Image("https://img.freepik.com/free-vector/contact-us-concept-illustration_114360-3147.jpg?w=740&t=st=1701519966~exp=1701520566~hmac=a17dc54b54a75f495625e9b8520f863d844f46dd4cbcf1e094ad2d196311d244"); // Replace with the path to your image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(230);
        imageView.setFitHeight(380);

        imagePane.getChildren().add(imageView);

        return imagePane;
    }

    private VBox createContactPane() {
        VBox contactPane = new VBox(20);
        contactPane.setPadding(new Insets(20));
        contactPane.setAlignment(Pos.CENTER);
        contactPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel = new Label("GET IN TOUCH");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("CONTACT US");
        subtitleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;");

        GridPane formGrid = createFormGrid();

        Button submitButton = createSubmitButton(
                (TextField) formGrid.getChildren().get(1), // Assuming nameField is the second child
                (TextField) formGrid.getChildren().get(3), // Assuming emailField is the fourth child
                (TextArea) formGrid.getChildren().get(5)    // Assuming messageArea is the sixth child
        );
        
        VBox submitButtonContainer = new VBox(submitButton);
        submitButtonContainer.setAlignment(Pos.CENTER);

        contactPane.getChildren().addAll(titleLabel, subtitleLabel, formGrid, submitButtonContainer);

        return contactPane;
    }

  

	private Button createSubmitButton(TextField nameField, TextField emailField, TextArea messageArea) {
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-font-size: 16; -fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold;");
        submitButton.setOnAction(e -> handleFormSubmission(
                nameField.getText(),
                emailField.getText(),
                messageArea.getText()
        ));
        return submitButton;
    }

 

    private GridPane createFormGrid() {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label messageLabel = new Label("Message:");
        TextArea messageArea = new TextArea();

        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(emailLabel, 0, 1);
        formGrid.add(emailField, 1, 1);
        formGrid.add(messageLabel, 0, 2);
        formGrid.add(messageArea, 1, 2);

        return formGrid;
    }

   

    private void handleFormSubmission(String name, String email, String message) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO ContactUsForm (name, email, message) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, message);
                preparedStatement.executeUpdate();
            }
            showAlert("Request submitted", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error submitting request", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Form Submission");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
