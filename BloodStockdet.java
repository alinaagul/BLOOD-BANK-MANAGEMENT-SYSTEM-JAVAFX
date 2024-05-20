package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BloodStockdet extends Application {

	private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USERNAME = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blood Stock");

        // Create a GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.TOP_LEFT);

        // Set background color
        gridPane.setStyle("-fx-background-color: #FFD289;");

        // Add title label
        Label titleLabel = new Label("BLOOD STOCK");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: BLACK;");
        GridPane.setColumnSpan(titleLabel, 2);
        gridPane.add(titleLabel, 0, 0, 2, 1);

        // Add column headers
        Label bloodGroupLabel = new Label("Blood Group");
        bloodGroupLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: BLACK;");
        gridPane.add(bloodGroupLabel, 0, 1);

        Label perUnitLabel = new Label("Per Unit");
        perUnitLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: BLACK;");
        gridPane.add(perUnitLabel, 1, 1);

        // Fetch data from the database
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM BloodStock")) {

            int row = 2; // Start from the third row (zero-based index)
            while (resultSet.next()) {
                Label bloodGroup = new Label(resultSet.getString("BloodGroup"));
                bloodGroup.setStyle("-fx-font-size: 14; -fx-text-fill: BLACK;");
                gridPane.add(bloodGroup, 0, row);

                Label perUnit = new Label(resultSet.getString("PerUnit"));
                perUnit.setStyle("-fx-font-size: 14; -fx-text-fill: BLACK;");
                gridPane.add(perUnit, 1, row);

                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add Close button to the right
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #8B0000; -fx-font-size: 16; -fx-text-fill: WHITE;");
        closeButton.setOnAction(event -> primaryStage.close());
        gridPane.add(closeButton, 1, 10, 1, 1);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
