package application;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class bloodtype extends Application {
	  private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	    private static final String DB_USERNAME = "system";
	    private static final String DB_PASSWORD = "Oracle_1";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blood Type Management");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #FFDBB5;");

        Label titleLabel = new Label("BLOOD TYPE MANAGEMENT");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKRED);
        grid.add(titleLabel, 0, 0);
       ;
        Button viewButton = createStyledButton("View Blood Types");
        Button addButton = createStyledButton("Add Blood Type");
        Button deleteButton = createStyledButton("Delete Blood Type");
        Button searchButton = createStyledButton("Search Blood Type");

        grid.add(viewButton, 1, 1);
        grid.add(addButton, 1, 2);
        grid.add(deleteButton, 1, 3);
        grid.add(searchButton, 1, 4);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();

        // Set button actions with a simple fade-in animation
        setButtonActionWithAnimation(viewButton);
        setButtonActionWithAnimation(addButton);
        setButtonActionWithAnimation(deleteButton);
        setButtonActionWithAnimation(searchButton);
        viewButton.setOnAction(e -> viewBloodTypes());
        addButton.setOnAction(e -> addblood());
        deleteButton.setOnAction(e -> delblood());
        searchButton.setOnAction(e -> searchBlood());
    }
	private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #8B0000; -fx-font-size: 14pt; -fx-text-fill: #FFFFFF; -fx-border-radius: 10; -fx-border-color: #8B0000;");
        button.setPrefSize(200, 60);
        return button;
    }

    private void setButtonActionWithAnimation(Button button) {
        button.setOpacity(0);

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(button.opacityProperty(), 1))
        );
        fadeIn.play();
    }
    private void viewBloodTypes() {
        Stage viewStage = new Stage();
        viewStage.setTitle("View Blood Types");

        GridPane viewGrid = new GridPane();
        viewGrid.setPadding(new Insets(20));
        viewGrid.setHgap(20);
        viewGrid.setVgap(20);
        viewGrid.setAlignment(Pos.CENTER);
        viewGrid.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("VIEW BLOOD TYPES");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
        GridPane.setColumnSpan(titleLabel, 3);
        viewGrid.add(titleLabel, 0, 0);

        TableView<BloodType> bloodTypeTableView = new TableView<>();
        bloodTypeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BloodType, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bloodTypeId"));

        TableColumn<BloodType, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        bloodTypeTableView.getColumns().addAll(idColumn, typeColumn);
        bloodTypeTableView.setStyle("-fx-border-color: #dddddd; -fx-border-width: 1px;");
        viewGrid.add(bloodTypeTableView, 0, 1, 2, 1);

        ObservableList<BloodType> bloodTypeList = FXCollections.observableArrayList();
        bloodTypeList.clear();
        bloodTypeList.addAll(fetchBloodTypesFromDatabase());
        bloodTypeTableView.setItems(bloodTypeList);

        Scene viewScene = new Scene(viewGrid, 800, 600);
        viewStage.setScene(viewScene);
        viewStage.show();
    }

    private ObservableList<BloodType> fetchBloodTypesFromDatabase() {
        ObservableList<BloodType> bloodTypes = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
		     Statement statement = connection.createStatement();
		     ResultSet resultSet = statement.executeQuery("SELECT * FROM BloodTypes")) {

		    while (resultSet.next()) {
		        int bloodTypeId = resultSet.getInt("blood_type_id");
		        String type = resultSet.getString("type");

		        BloodType bloodType = new BloodType(bloodTypeId, type);
		        bloodTypes.add(bloodType);
		    }

		} catch (SQLException e) {
		    e.printStackTrace();
		}
        return bloodTypes;
    }

    public class BloodType {
        private int bloodTypeId;
        private String type;

        public BloodType(int bloodTypeId, String type) {
            this.bloodTypeId = bloodTypeId;
            this.type = type;
        }

        public int getBloodTypeId() {
            return bloodTypeId;
        }

        public String getType() {
            return type;
        }

        public void setType(String updatedType) {
            this.type = updatedType;
        }

        @Override
        public String toString() {
            return type; // Override toString() to return the blood type
        }
    }



    private void addblood() {
        Stage addStage = new Stage();
        addStage.setTitle("Add Blood Type");

        GridPane addGrid = new GridPane();
        addGrid.setPadding(new Insets(20));
        addGrid.setHgap(20);
        addGrid.setVgap(20);
        addGrid.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("ADD BLOOD TYPE");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
        GridPane.setColumnSpan(titleLabel, 2);
        addGrid.add(titleLabel, 0, 0);

        // Add input fields for blood type details
        TextField bloodTypeField = new TextField();
        addDetailRow(addGrid, "Blood Type:", bloodTypeField, 1);

        // Add Save button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: green; -fx-font-size: 16; -fx-text-fill: white;");
        saveButton.setOnAction(event -> {
            String type = bloodTypeField.getText();
            
            // Validate input
            if (!type.isEmpty()) {
                // Create BloodType object and save to the database
                BloodType bloodType = new BloodType(0, type);
                saveBloodTypeToDatabase(bloodType);

                // Inform the user
                showAlert("Blood type added successfully.");

                // Close the stage
                addStage.close();
            } else {
                showAlert("Please enter a valid blood type.");
            }
        });
        addGrid.add(saveButton, 0, 3);

        // Add Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: gray; -fx-font-size: 16; -fx-text-fill: white;");
        closeButton.setOnAction(event -> addStage.close());
        addGrid.add(closeButton, 1, 3);

        Scene addScene = new Scene(addGrid, 400, 200);
        addStage.setScene(addScene);
        addStage.show();
    }

    // Additional helper methods

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void saveBloodTypeToDatabase(BloodType bloodType) {
        
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD)) {
                String insertQuery = "INSERT INTO BloodTypes (type) VALUES (?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, bloodType.getType());

                    // Execute the update
                    preparedStatement.executeUpdate();
                }
            }
      catch (SQLException e) {
            e.printStackTrace();  } 

        }
    


    private void addDetailRow(GridPane gridPane, String label, Node control, int row) {
        Label detailLabel = new Label(label);
        detailLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");
        gridPane.add(detailLabel, 0, row);

        control.setStyle("-fx-font-size: 14;");
        gridPane.add(control, 1, row);
    
}

    private void delblood() {
        Stage deleteStage = new Stage();
        deleteStage.setTitle("Delete Blood Type");

        // Create UI elements for selecting blood type
        Label titleLabel = new Label("DELETE BLOOD TYPE");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");

        ComboBox<BloodType> bloodTypeComboBox = new ComboBox<>();
        bloodTypeComboBox.setPromptText("Select Blood Type");
        bloodTypeComboBox.getItems().addAll(fetchBloodTypesFromDatabase());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            BloodType selectedBloodType = bloodTypeComboBox.getValue();
            if (selectedBloodType != null) {
                if (showConfirmationDialog()) { // Ask for confirmation
                    deleteBloodType(selectedBloodType);
                }
            }
        });

        // Layout setup
        VBox deleteLayout = new VBox(20);
        deleteLayout.setAlignment(Pos.CENTER);
        deleteLayout.getChildren().addAll(titleLabel, bloodTypeComboBox, deleteButton);

        Scene deleteScene = new Scene(deleteLayout, 400, 300);
        deleteStage.setScene(deleteScene);
        deleteStage.show();
    }

    private boolean showConfirmationDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete the selected blood type?");

        // Custom styling for the confirmation dialog (optional)
        alert.getDialogPane().setStyle("-fx-background-color: #f4f4f4;");

        // Show and wait for user's response
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void deleteBloodType(BloodType bloodType) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM BloodTypes WHERE blood_type_id = ?")) {

            preparedStatement.setInt(1, bloodType.getBloodTypeId());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showSuccessAlert("Blood type deleted successfully.");
                // Optionally, update the UI or perform additional actions
            } else {
                showErrorAlert("Failed to delete blood type.");
                // Optionally, handle the failure
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, handle the exception
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void searchBlood() {
        Stage searchStage = new Stage();
        searchStage.setTitle("Search Blood Type");

        // Create UI elements for searching blood type
        Label titleLabel = new Label("SEARCH BLOOD TYPE");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Blood Type to Search");

        Button searchButton = new Button("Search");

        TableView<BloodType> resultTableView = new TableView<>();  // Move the initialization here

        TableColumn<BloodType, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bloodTypeId"));

        TableColumn<BloodType, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        resultTableView.getColumns().addAll(idColumn, typeColumn);

        searchButton.setOnAction(e -> {
            String searchQuery = searchField.getText().trim();
            if (!searchQuery.isEmpty()) {
                searchAndDisplayResults(searchQuery, resultTableView);
            } else {
                // Optionally, display a message or handle empty search
            }
        });

        // Layout setup
        VBox searchLayout = new VBox(20);
        searchLayout.setAlignment(Pos.CENTER);
        searchLayout.getChildren().addAll(titleLabel, searchField, searchButton, resultTableView);

        Scene searchScene = new Scene(searchLayout, 400, 400);
        searchStage.setScene(searchScene);
        searchStage.show();
    }

    private void searchAndDisplayResults(String searchQuery, TableView<BloodType> resultTableView) {
        ObservableList<BloodType> searchResults = FXCollections.observableArrayList();

        // Modify fetchBloodTypesFromDatabase to support searching
        searchResults.addAll(searchBloodTypesFromDatabase(searchQuery));

        // Update the TableView with search results
        resultTableView.setItems(searchResults);
    }

    private ObservableList<BloodType> searchBloodTypesFromDatabase(String searchQuery) {
        ObservableList<BloodType> searchResults = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BloodTypes WHERE type LIKE ?")) {

            preparedStatement.setString(1, "%" + searchQuery + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int bloodTypeId = resultSet.getInt("blood_type_id");
                String type = resultSet.getString("type");

                BloodType bloodType = new BloodType(bloodTypeId, type);
                searchResults.add(bloodType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, handle the exception
        }

        return searchResults;
    }}

