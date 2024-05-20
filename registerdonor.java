package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class registerdonor extends Application {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add New Donor");

        // Create layout
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-color: #FFD289;");
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        

        gridPane.add(new Label("Email:"), 0, 0);
        TextField emailTextField = new TextField();
        gridPane.add(emailTextField, 1, 0);

        gridPane.add(new Label("Full Name:"), 0, 1);
        TextField fullNameTextField = new TextField();
        gridPane.add(fullNameTextField, 1, 1);

        gridPane.add(new Label("Father Name:"), 0, 2);
        TextField fatherNameTextField = new TextField();
        gridPane.add(fatherNameTextField, 1, 2);

        gridPane.add(new Label("Blood Group:"), 0, 3);
        TextField bloodGroupTextField = new TextField();
        gridPane.add(bloodGroupTextField, 1, 3);

        gridPane.add(new Label("Mother Name:"), 0, 4);
        TextField motherNameTextField = new TextField();
        gridPane.add(motherNameTextField, 1, 4);

        gridPane.add(new Label("City:"), 0, 5);
        TextField cityTextField = new TextField();
        gridPane.add(cityTextField, 1, 5);

        gridPane.add(new Label("Complete Address:"), 0, 6);
        TextArea completeAddressTextArea = new TextArea();
        gridPane.add(completeAddressTextArea, 1, 6);

        gridPane.add(new Label("Mobile Number:"), 0, 7);
        TextField mobileNumberTextField = new TextField();
        gridPane.add(mobileNumberTextField, 1, 7);

        gridPane.add(new Label("Gender:"), 0, 8);
        ComboBox<String> genderComboBox = new ComboBox<>(FXCollections.observableArrayList("Male", "Female"));
        genderComboBox.setId("Gender");
        gridPane.add(genderComboBox, 1, 8);

       

        // Buttons
        Button saveButton = createStyledButton("Save");
        Button resetButton = createStyledButton("Reset");
        Button closeButton = createStyledButton("Close");

        HBox buttonBox = new HBox(10, saveButton, resetButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        gridPane.add(buttonBox, 1, 11);
        borderPane.setCenter(gridPane);

        // Set the scene
        Scene scene = new Scene(borderPane, 800, 600);
        scene.setFill(javafx.scene.paint.Color.BLACK); // Set background color to black
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();

        // Set button actions
        saveButton.setOnAction(e -> {
            String email = emailTextField.getText();
            String fullName = fullNameTextField.getText();
            String fatherName = fatherNameTextField.getText();
            String bloodGroup = bloodGroupTextField.getText();
            String motherName = motherNameTextField.getText();
            String city = cityTextField.getText();
            String completeAddress = completeAddressTextArea.getText();
            String mobileNumber = mobileNumberTextField.getText();
            String gender = genderComboBox.getValue();

            saveDonorInformation(email, fullName, fatherName, bloodGroup, motherName, city, completeAddress, mobileNumber, gender);
        });

        resetButton.setOnAction(e -> resetForm(gridPane));
        closeButton.setOnAction(e -> primaryStage.close());
    }

    private Button createStyledButton(String text) {
    	 Button button = new Button(text);
         button.setStyle("-fx-background-color: #8B0000; -fx-font-size: 14pt; -fx-text-fill: #FFFFFF; -fx-border-radius: 10; -fx-border-color: #8B0000;");
         button.setMinSize(60, 20);
         return button;
        
    }

    private void saveDonorInformation(String email, String fullName, String fatherName, String bloodGroup, String motherName, String city, String completeAddress, String mobileNumber, String gender) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

            // Prepare SQL query
            String query = "INSERT INTO Donors (email, full_name, father_name, blood_group, " +
                    "mother_name, city, complete_address, mobile_number, gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, fullName);
            preparedStatement.setString(3, fatherName);
            preparedStatement.setString(4, bloodGroup);
            preparedStatement.setString(5, motherName);
            preparedStatement.setString(6, city);
            preparedStatement.setString(7, completeAddress);
            preparedStatement.setString(8, mobileNumber);
            preparedStatement.setString(9, gender);

            preparedStatement.executeUpdate();
            showInformationBox("Information saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showInformationBox("Error saving information.");
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

    private void showInformationBox(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetForm(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            } else if (node instanceof TextArea) {
                ((TextArea) node).clear();
            } else if (node instanceof ComboBox) {
                ((ComboBox<?>) node).getSelectionModel().selectFirst();
            }
        }
    }
}
