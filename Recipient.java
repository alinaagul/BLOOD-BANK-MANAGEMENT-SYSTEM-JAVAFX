package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Recipient extends Application {

    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Recipient Details");

        // Create a GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #FFD289;");

        // Add title label
        Label titleLabel = new Label("RECIPIENT DETAILS");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: BLACK;");
        GridPane.setColumnSpan(titleLabel, 3);
        gridPane.add(titleLabel, 0, 0);

        // Add details fields
        TextField fullNameTextField = new TextField();
        TextField addressTextField = new TextField();
        TextField contactTextField = new TextField();
        TextField emailTextField = new TextField();
        TextField ageTextField = new TextField();

        addDetailRow(gridPane, "Full Name:", fullNameTextField, 1);
        addDetailRow(gridPane, "Address:", addressTextField, 2);
        addDetailRow(gridPane, "Contact:", contactTextField, 3);
        addDetailRow(gridPane, "Email Address:", emailTextField, 4);
        addDetailRow(gridPane, "Age:", ageTextField, 5);

        // Add gender choice box
        Label genderLabel = new Label("Gender:");
        genderLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: BLACK;");
        gridPane.add(genderLabel, 0, 6);

        ChoiceBox<String> genderChoiceBox = new ChoiceBox<>();
        genderChoiceBox.getItems().addAll("Male", "Female");
        genderChoiceBox.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
        gridPane.add(genderChoiceBox, 1, 6);

        // Add blood type choice box
        Label bloodTypeLabel = new Label("Blood Type:");
        bloodTypeLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: BLACK;");
        gridPane.add(bloodTypeLabel, 0, 7);

        ChoiceBox<String> bloodTypeChoiceBox = new ChoiceBox<>();
        bloodTypeChoiceBox.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodTypeChoiceBox.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
        gridPane.add(bloodTypeChoiceBox, 1, 7);

        // Add Save button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #8B0000; -fx-font-size: 16; -fx-text-fill: white;");
        saveButton.setOnAction(event -> {
            // Get recipient details from the form
            String fullName = fullNameTextField.getText();
            String address = addressTextField.getText();
            String contact = contactTextField.getText();
            String emailAddress = emailTextField.getText();
            int age = Integer.parseInt(ageTextField.getText());
            String gender = genderChoiceBox.getValue();
            String bloodType = bloodTypeChoiceBox.getValue();

            // Store the recipient details in the database
            addRecipient(fullName, address, contact, emailAddress, age, gender, bloodType);

            // Inform the user
            showAlert("Recipient details saved successfully.");

            // Close the stage
            primaryStage.close();
        });
        gridPane.add(saveButton, 0, 8);

        // Add Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: gray; -fx-font-size: 16; -fx-text-fill: BLACK;");
        closeButton.setOnAction(event -> primaryStage.close());
        gridPane.add(closeButton, 1, 8);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addDetailRow(GridPane gridPane, String label, Control control, int row) {
        Label detailLabel = new Label(label);
        detailLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: BLACK;");
        gridPane.add(detailLabel, 0, row);

        control.setStyle("-fx-font-size: 14;");
        gridPane.add(control, 1, row, 2, 1);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addRecipient(String fullName, String address, String contact, String emailAddress, int age, String gender, String bloodType) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

            String query = "INSERT INTO Recipients (full_name, address, contact, email_address, age, gender, blood_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, contact);
            preparedStatement.setString(4, emailAddress);
            preparedStatement.setInt(5, age);
            preparedStatement.setString(6, gender);
            preparedStatement.setString(7, bloodType);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
