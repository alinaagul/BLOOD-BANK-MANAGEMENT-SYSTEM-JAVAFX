package application;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;
import java.util.Optional;

public class recact extends Application {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USERNAME = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    private TableView<Recipient> tableView;
    private TextArea resultTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Recipient Management");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #FFDBB5;");

        Label titleLabel = new Label("RECIPIENT MANAGEMENT");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKRED);
        grid.add(titleLabel, 0, 0);
       ;
        Button viewButton = createStyledButton("View Recipient");
        Button addButton = createStyledButton("Add Recipient");
        Button editButton = createStyledButton("Edit Recipient");
        Button deleteButton = createStyledButton("Delete Recipient");
        Button searchButton = createStyledButton("Search Recipient");

        grid.add(viewButton, 1, 1);
        grid.add(addButton, 1, 2);
        grid.add(editButton, 1, 3);
        grid.add(deleteButton, 1, 4);
        grid.add(searchButton, 1, 5);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();

        // Set button actions with a simple fade-in animation
        setButtonActionWithAnimation(viewButton);
        setButtonActionWithAnimation(addButton);
        setButtonActionWithAnimation(editButton);
        setButtonActionWithAnimation(deleteButton);
        setButtonActionWithAnimation(searchButton);
        viewButton.setOnAction(e -> viewRecipients());
        addButton.setOnAction(e -> addRecipient());
        editButton.setOnAction(e -> editRecipient());
        deleteButton.setOnAction(e -> deleteRecipient());
        searchButton.setOnAction(e -> searchRecipient());
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

    private void viewRecipients() {
        Stage viewStage = new Stage();
        viewStage.setTitle("View Recipients");

        GridPane viewGrid = new GridPane();
        viewGrid.setPadding(new Insets(20));
        viewGrid.setHgap(20);
        viewGrid.setVgap(20);
        viewGrid.setAlignment(Pos.CENTER);
        viewGrid.setStyle("-fx-background-color: #f4f4f4;");

        // Add title label
        Label titleLabel = new Label("VIEW RECIPIENTS");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
        GridPane.setColumnSpan(titleLabel, 3);
        viewGrid.add(titleLabel, 0, 0);

        // Initialize TableView
        TableView<Recipient> viewTableView = new TableView<>();
        viewTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        viewTableView.setStyle("-fx-background-color: #ffffff;");

        TableColumn<Recipient, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Recipient, String> nameColumn = new TableColumn<>("Full Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        // Add more TableColumn for other properties
        viewTableView.getColumns().addAll(idColumn, nameColumn /* Add other columns */);
        viewTableView.setStyle("-fx-border-color: #dddddd; -fx-border-width: 1px;");
        viewGrid.add(viewTableView, 0, 1, 2, 1);

        ObservableList<Recipient> viewRecipientList = FXCollections.observableArrayList();
        viewRecipientList.clear();
        viewRecipientList.addAll(fetchRecipientsFromDatabase());
        viewTableView.setItems(viewRecipientList);

        Scene viewScene = new Scene(viewGrid, 800, 600);
        viewStage.setScene(viewScene);
        viewStage.show();
    }

    private ObservableList<Recipient> fetchRecipientsFromDatabase() {
        ObservableList<Recipient> recipients = FXCollections.observableArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // Load the Oracle JDBC driver
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM Recipients")) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("recipient_id");
                    String fullName = resultSet.getString("full_name");
                    String address = resultSet.getString("address");
                    String contact = resultSet.getString("contact");
                    String email = resultSet.getString("email_address");
                    int age = resultSet.getInt("age");
                    String gender = resultSet.getString("gender");
                    String bloodType = resultSet.getString("blood_type");

                    Recipient recipient = new Recipient(id, fullName, address, contact, email, age, gender, bloodType);
                    recipients.add(recipient);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recipients;
    }
    private void addRecipient() {
        Stage primaryStage = new Stage(); // Create a new stage

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #f4f4f4;");
        // Add title label
        Label titleLabel = new Label("RECIPIENT DETAILS");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: black;");
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
        genderLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: black;");
        gridPane.add(genderLabel, 0, 6);

        ChoiceBox<String> genderChoiceBox = new ChoiceBox<>();
        genderChoiceBox.getItems().addAll("Male", "Female");
        genderChoiceBox.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
        gridPane.add(genderChoiceBox, 1, 6);

        // Add blood type choice box
        Label bloodTypeLabel = new Label("Blood Type:");
        bloodTypeLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: black;");
        gridPane.add(bloodTypeLabel, 0, 7);

        ChoiceBox<String> bloodTypeChoiceBox = new ChoiceBox<>();
        bloodTypeChoiceBox.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodTypeChoiceBox.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
        gridPane.add(bloodTypeChoiceBox, 1, 7);

        // Add Save button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: green; -fx-font-size: 16; -fx-text-fill: white;");
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
        closeButton.setStyle("-fx-background-color: red; -fx-font-size: 16; -fx-text-fill: white;");
        closeButton.setOnAction(event -> primaryStage.close());
        gridPane.add(closeButton, 1, 8);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


     private void addDetailRow(GridPane gridPane, String label, Control control, int row) {
         Label detailLabel = new Label(label);
         detailLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: black;");
         gridPane.add(detailLabel, 0, row);

         control.setStyle("-fx-font-size: 14;");
         gridPane.add(control, 1, row, 2, 1);
     }

    

     private void addRecipient(String fullName, String address, String contact, String emailAddress, int age, String gender, String bloodType) {
         Connection connection = null;
         PreparedStatement preparedStatement = null;

         try {
             connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);

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

     private void editRecipient() {
    	    TextInputDialog fullNameDialog = new TextInputDialog();
    	    fullNameDialog.setTitle("Edit Recipient");
    	    fullNameDialog.setHeaderText("Enter Full Name of Recipient to Edit:");
    	    fullNameDialog.setContentText("Full Name:");

    	    Optional<String> fullNameResult = fullNameDialog.showAndWait();
    	    fullNameResult.ifPresent(fullName -> {
    	        if (isRecipientExists(fullName)) {
    	            Stage editStage = new Stage();
    	            editStage.setTitle("Edit Recipient Details");

    	            GridPane editGrid = new GridPane();
    	            editGrid.setPadding(new Insets(10));
    	            editGrid.setHgap(20);
    	            editGrid.setVgap(10);
    	            editGrid.setAlignment(Pos.CENTER);
    	            editGrid.setStyle("-fx-background-color: #1a1a1a;");

    	            // Add title label
    	            Label titleLabel = new Label("EDIT RECIPIENT DETAILS");
    	            titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");
    	            GridPane.setColumnSpan(titleLabel, 3);
    	            editGrid.add(titleLabel, 0, 0);

    	            // Add details fields
    	            TextField addressTextField = new TextField();
    	            TextField contactTextField = new TextField();
    	            TextField emailTextField = new TextField();
    	            TextField ageTextField = new TextField();
    	            addDetailRow(editGrid, "Full Name:", new Label(fullName), 1);
    	            addDetailRow(editGrid, "Address:", addressTextField, 2);
    	            addDetailRow(editGrid, "Contact:", contactTextField, 3);
    	            addDetailRow(editGrid, "Email Address:", emailTextField, 4);
    	            addDetailRow(editGrid, "Age:", ageTextField, 5);

    	            // Add gender choice box
    	            Label genderLabel = new Label("Gender:");
    	            genderLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white;");
    	            editGrid.add(genderLabel, 0, 6);

    	            ChoiceBox<String> genderChoiceBox = new ChoiceBox<>();
    	            genderChoiceBox.getItems().addAll("Male", "Female");
    	            genderChoiceBox.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
    	            editGrid.add(genderChoiceBox, 1, 6);

    	            // Add blood type choice box
    	            Label bloodTypeLabel = new Label("Blood Type:");
    	            bloodTypeLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white;");
    	            editGrid.add(bloodTypeLabel, 0, 7);

    	            ChoiceBox<String> bloodTypeChoiceBox = new ChoiceBox<>();
    	            bloodTypeChoiceBox.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
    	            bloodTypeChoiceBox.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
    	            editGrid.add(bloodTypeChoiceBox, 1, 7);

    	            // Add Save button
    	            Button saveButton = new Button("Save");
    	            saveButton.setStyle("-fx-background-color: green; -fx-font-size: 16; -fx-text-fill: white;");
    	            saveButton.setOnAction(event -> {
    	                int selectedRecipientId = getSelectedRecipientId();
    	                String address = addressTextField.getText();
    	                String contact = contactTextField.getText();
    	                String emailAddress = emailTextField.getText();
    	                int age = Integer.parseInt(ageTextField.getText());
    	                String gender = genderChoiceBox.getValue();
    	                String bloodType = bloodTypeChoiceBox.getValue();

    	                // Update recipient details in the database
    	                updateRecipient(selectedRecipientId, fullName, address, contact, age, gender, bloodType);

    	                // Inform the user
    	                showAlert("Recipient details updated successfully.");

    	                // Close the stage
    	                editStage.close();
    	                
    	            });
    	            editGrid.add(saveButton, 0, 8);

    	            // Add Close button
    	            Button closeButton = new Button("Close");
    	            closeButton.setStyle("-fx-background-color: gray; -fx-font-size: 16; -fx-text-fill: white;");
    	            closeButton.setOnAction(event -> editStage.close());
    	            editGrid.add(closeButton, 1, 8);

    	            // Create scene and set stage
    	            Scene editScene = new Scene(editGrid, 600, 400);
    	            editStage.setScene(editScene);
    	            editStage.show();
    	        } else {
    	            showAlert("Recipient not found. Please enter a valid full name.");
    	        }
    	    });
    	}


     private int getSelectedRecipientId() {
    
             try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                  PreparedStatement statement = connection.prepareStatement(
                          "SELECT recipient_id FROM Recipients WHERE full_name = ?")) {

                 String fullName = null;
				statement.setString(1, fullName);
                 ResultSet resultSet = statement.executeQuery();

                 if (resultSet.next()) {
                     return resultSet.getInt("recipient_id");
                 }

             } catch (SQLException e) {
                 e.printStackTrace();
             }
             return -1;
         }
	

	private void addDetailRow(GridPane gridPane, String label, Node control, int row) {
         Label detailLabel = new Label(label);
         detailLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white;");
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

     private boolean isRecipientExists(String fullName) {
         try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
              PreparedStatement statement = connection.prepareStatement(
                      "SELECT * FROM Recipients WHERE full_name = ?")) {

             statement.setString(1, fullName);
             ResultSet resultSet = statement.executeQuery();

             // If the resultSet has any rows, it means a recipient with the given name exists
             return resultSet.next();

         } catch (SQLException ex) {
             ex.printStackTrace();
             return false;  // Assume recipient doesn't exist in case of an exception
         }
     }

     private int getRecipientId(final String fullName) {
         try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
              PreparedStatement statement = connection.prepareStatement(
                      "SELECT recipient_id FROM Recipients WHERE full_name = ?")) {

             statement.setString(1, fullName);
             ResultSet resultSet = statement.executeQuery();

             if (resultSet.next()) {
                 return resultSet.getInt("recipient_id");
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
         return -1;
     }

     private void updateRecipient(int recipientId, String address, String contact, String emailAddress, int age, String gender, String bloodType) {
         try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
              PreparedStatement statement = connection.prepareStatement(
                      "UPDATE Recipients SET address = ?, contact = ?, email_address = ?, " +
                              "age = ?, gender = ?, blood_type = ? WHERE recipient_id = ?")) {

             statement.setString(1, address);
             statement.setString(2, contact);
             statement.setString(3, emailAddress);
             statement.setInt(4, age);
             statement.setString(5, gender);
             statement.setString(6, bloodType);
             statement.setInt(7, recipientId);

             statement.executeUpdate();

         } catch (SQLException e) {
             e.printStackTrace();
         }
     }



     private void deleteRecipient() {
         TextInputDialog fullNameDialog = new TextInputDialog();
         fullNameDialog.setTitle("Delete Recipient");
         fullNameDialog.setHeaderText("Enter Full Name of Recipient to Delete:");
         fullNameDialog.setContentText("Full Name:");

         Optional<String> fullNameResult = fullNameDialog.showAndWait();
         fullNameResult.ifPresent(fullName -> {
             if (isRecipientExists(fullName)) {
                 Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                 confirmationAlert.setTitle("Confirmation");
                 confirmationAlert.setHeaderText("Confirm Deletion");
                 confirmationAlert.setContentText("Are you sure you want to delete the recipient with the full name: " + fullName + "?");

                 ButtonType confirmButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
                 ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                 confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

                 Optional<ButtonType> result = confirmationAlert.showAndWait();
                 if (result.isPresent() && result.get() == confirmButton) {
                     // User confirmed the deletion
                     int selectedRecipientId = getRecipientId(fullName);

                     try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                          PreparedStatement statement = connection.prepareStatement(
                                  "DELETE FROM Recipients WHERE recipient_id = ?")) {

                         statement.setInt(1, selectedRecipientId);

                         int rowsAffected = statement.executeUpdate();

                         if (rowsAffected > 0) {
                             showAlert("Recipient deleted successfully!");
                         } else {
                             showAlert("Failed to delete recipient. Recipient not found.");
                         }

                     } catch (SQLException ex) {
                         ex.printStackTrace();
                     }
                 }
             } else {
                 showAlert("Recipient not found. Please enter a valid full name.");
             }
         });
     }
     private void searchRecipient() {
         TextInputDialog dialog = new TextInputDialog();
         dialog.setTitle("Search/Filter Recipient");
         dialog.setHeaderText("Enter Search Criteria");
         dialog.setContentText("Full Name:");

         Optional<String> result = dialog.showAndWait();
         result.ifPresent(fullName -> {
             try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                  PreparedStatement statement = connection.prepareStatement(
                          "SELECT * FROM Recipients WHERE full_name = ?")) {

                 statement.setString(1, fullName);
                 ResultSet resultSet = statement.executeQuery();

                 if (resultSet.next()) {
                     displaySearchResults(resultSet);
                 } else {
                     showAlert("No recipients found with the specified full name.");
                 }

             } catch (SQLException ex) {
                 ex.printStackTrace();
             }
         });
     }

     private void displaySearchResults(ResultSet resultSet) {
    	    Stage resultsStage = new Stage();
    	    resultsStage.setTitle("Search Results");

    	    GridPane gridPane = new GridPane();
    	    gridPane.setPadding(new Insets(10));
    	    gridPane.setHgap(20);
    	    gridPane.setVgap(10);
    	    gridPane.setAlignment(Pos.CENTER);

    	    // Add title label
    	    Label titleLabel = new Label("SEARCH RESULTS");
    	    titleLabel.setFont(Font.font(18));
    	    GridPane.setColumnSpan(titleLabel, 3);
    	    gridPane.add(titleLabel, 0, 0);

    	    // Add details fields
    	    int row = 1;
    	    try {
    	        do {
    	            addResultRow(gridPane, "ID:", resultSet.getInt("recipient_id"), row++);
    	            addResultRow(gridPane, "Full Name:", resultSet.getString("full_name"), row++);
    	            addResultRow(gridPane, "Address:", resultSet.getString("address"), row++);
    	            addResultRow(gridPane, "Contact:", resultSet.getString("contact"), row++);
    	            addResultRow(gridPane, "Email Address:", resultSet.getString("email_address"), row++);
    	            addResultRow(gridPane, "Age:", resultSet.getInt("age"), row++);
    	            addResultRow(gridPane, "Gender:", resultSet.getString("gender"), row++);
    	            addResultRow(gridPane, "Blood Type:", resultSet.getString("blood_type"), row++);
    	            addSeparator(gridPane, row++);
    	        } while (resultSet.next());
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }

    	    // Create scene and set stage
    	    Scene resultsScene = new Scene(gridPane, 600, 400);
    	    resultsStage.setScene(resultsScene);
    	    resultsStage.show();
    	}

    	private void addResultRow(GridPane gridPane, String label, Object value, int row) {
    	    Label resultLabel = new Label(label);
    	    resultLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
    	    gridPane.add(resultLabel, 0, row);

    	    Label resultValue = new Label(String.valueOf(value));
    	    resultValue.setStyle("-fx-font-size: 14;");
    	    gridPane.add(resultValue, 1, row);

    	    // Add some spacing between label and value
    	    GridPane.setMargin(resultValue, new Insets(0, 0, 0, 10));
    	}

    	private void addSeparator(GridPane gridPane, int row) {
    	    Separator separator = new Separator();
    	    gridPane.add(separator, 0, row, 2, 1);

    	    // Add some spacing after the separator
    	    GridPane.setMargin(separator, new Insets(10, 0, 10, 0));
    	}


    public class Recipient {
        private int id;
        private String fullName;

        public Recipient(int id, String fullName, String address, String contact, String email, int age, String gender, String bloodType) {
            this.id = id;
            this.fullName = fullName;
        }

        public int getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }
    }
}
