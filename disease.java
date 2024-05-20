package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class disease extends Application {

    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USERNAME = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Disease Information");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Label nameLabel = new Label("Enter Name:");
        TextField nameField = new TextField();

        Button showDiseaseButton = new Button("Show Disease Information");
        showDiseaseButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        showDiseaseButton.setOnAction(event -> {
            String enteredName = nameField.getText();
            String[] diseaseInfo = getDiseaseInformation(enteredName);

            if (diseaseInfo != null) {
                // Display disease information with a different color
                String message = "Donor ID: " + diseaseInfo[0] +
                        "\nFull Name: " + diseaseInfo[1] +
                        "\nDisease ID: " + diseaseInfo[2] +
                        "\nDisease Name: " + diseaseInfo[3] +
                        "\nType: " + diseaseInfo[4];
                showInformationDialog("Disease Information", message, Color.BLUE);
            } else {
                // If no information found, display in red
                showInformationDialog("Disease Information", "No information found for the entered name.", Color.RED);
            }
        });

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(showDiseaseButton, 1, 1);
        grid.setStyle("-fx-background-color:#FFD289;");
        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private String[] getDiseaseInformation(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);

            String query = "SELECT Donors.donor_id, Donors.full_name, Disease.DiseaseID, Disease.DiseaseName, Disease.Type " +
                    "FROM Donors " +
                    "JOIN Disease ON Donors.DiseaseID = Disease.DiseaseID " +
                    "WHERE Donors.full_name = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new String[]{
                        resultSet.getString("donor_id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("DiseaseID"),
                        resultSet.getString("DiseaseName"),
                        resultSet.getString("Type")
                };
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return null;
    }

    private void showInformationDialog(String title, String message, Color color) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        dialogGrid.setPadding(new Insets(20, 20, 20, 20));

        Label messageLabel = new Label(message);
        messageLabel.setTextFill(color);  // Set text color
        dialogGrid.add(messageLabel, 0, 0);

        Scene dialogScene = new Scene(dialogGrid, 400, 150);
        dialogStage.setScene(dialogScene);

        dialogStage.show();
    }
}
