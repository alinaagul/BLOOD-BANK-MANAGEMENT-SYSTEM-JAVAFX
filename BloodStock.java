package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BloodStock extends Application {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Oracle_1";
    private ListView<String> donorListView;
    private ObservableList<String> donorInfoList;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

        // Top section
        Label titleLabel = new Label("SEARCH BLOOD DONOR (Blood Group)");
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setStyle("-fx-font-weight: bold;");
        HBox topBox = new HBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 20, 0));
        mainLayout.setTop(topBox);

        // Middle section
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // City label, text field, and enter button
        Label cityLabel = new Label("Blood Group");
        cityLabel.setTextFill(Color.BLACK);
        TextField cityTextField = new TextField();
        Button enterButton = new Button("Enter");
        enterButton.setTextFill(Color.WHITE);
        enterButton.setStyle("-fx-background-color: #8B0000;");
        enterButton.setOnAction(event -> handleEnter(cityTextField.getText()));
        cityTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleEnter(cityTextField.getText());
            }
        });

        HBox cityBox = new HBox(cityLabel, cityTextField, enterButton);
        cityBox.setAlignment(Pos.CENTER);
        cityBox.setSpacing(10);
        gridPane.add(cityBox, 1, 0);

        donorListView = new ListView<>();
        donorInfoList = FXCollections.observableArrayList();
        donorListView.setItems(donorInfoList);

        gridPane.add(donorListView, 0, 2, 4, 1);


        // Close button
        Button closeButton = new Button("Close");
        closeButton.setTextFill(Color.WHITE);
        closeButton.setStyle("-fx-background-color: #8B0000;");
        closeButton.setOnAction(event -> primaryStage.close());
        closeButton.setMinSize(120, 40);

        // Layout for the close button
        HBox buttonBox = new HBox(closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 20, 0));

        // Add components to the main layout
        mainLayout.setCenter(gridPane);
        mainLayout.setBottom(buttonBox);

        // Set up the scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("Blood Donor Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleEnter(String bloodGroup) {
        // Assuming you have a database connection established earlier
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT full_name, city, mobile_number FROM Donors WHERE blood_group = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, bloodGroup);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    donorInfoList.clear(); // Clear existing items

                    while (resultSet.next()) {
                        String fullName = resultSet.getString("full_name");
                        String city = resultSet.getString("city");
                        String mobileNumber = resultSet.getString("mobile_number");

                        String donorInfo = "Name: " + fullName + "\n" +
                                "City: " + city + "\n" +
                                "Mobile Number: " + mobileNumber + "\n\n";

                        donorInfoList.add(donorInfo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showInformationBox("Error occurred while searching for donors.");
        }
    }

    private void showInformationBox(String message) {
        // Implement this method to show an information box or alert with the provided message
    }
}
