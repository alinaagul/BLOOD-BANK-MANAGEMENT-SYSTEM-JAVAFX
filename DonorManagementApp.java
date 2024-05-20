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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DonorManagementApp extends Application {

    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USERNAME = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Donor Management");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color:#FFDBB5;");

        Label titleLabel = new Label("DONOR MANAGEMENT");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKRED);
        grid.add(titleLabel, 0, 0);

        Button viewButton = createStyledButton("View Donor");
        Button addButton = createStyledButton("Add Donor");
        Button deleteButton = createStyledButton("Delete Donor");
        Button searchButton = createStyledButton("Search Donor");

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

        viewButton.setOnAction(e -> viewDonors());
        addButton.setOnAction(e -> addDonor());
        deleteButton.setOnAction(e -> deleteDonors());
        searchButton.setOnAction(e -> searchDonors());
    }
   
    private void addDonor() {
    	Stage donorRegistrationStage = new Stage();
        registerdonor donorRegistration = new registerdonor();
        donorRegistration.start(donorRegistrationStage);
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

    private void viewDonors() {
        Stage viewStage = new Stage();
        viewStage.setTitle("View Donors");

        GridPane viewGrid = new GridPane();
        viewGrid.setPadding(new Insets(20));
        viewGrid.setHgap(20);
        viewGrid.setVgap(20);
        viewGrid.setAlignment(Pos.CENTER);
        viewGrid.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("VIEW DONORS");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
        GridPane.setColumnSpan(titleLabel, 3);
        viewGrid.add(titleLabel, 0, 0);

        TableView<Donor> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Donor, String> nameColumn = new TableColumn<>("Full Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName")); 

        TableColumn<Donor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("donorId"));

        TableColumn<Donor, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Donor, Integer> diseaseidColumn = new TableColumn<>("Disease ID");
        diseaseidColumn.setCellValueFactory(new PropertyValueFactory<>("diseaseId"));

        boolean addAll = tableView.getColumns().addAll(idColumn, nameColumn, emailColumn, diseaseidColumn);
        tableView.setStyle("-fx-border-color: #dddddd; -fx-border-width: 1px;");
        viewGrid.add(tableView, 0, 1, 2, 1);


        ObservableList<Donor> donorList = FXCollections.observableArrayList();
        donorList.addAll(fetchDonorsFromDatabase());
        tableView.setItems(donorList);

        Scene viewScene = new Scene(viewGrid, 800, 600);
        viewStage.setScene(viewScene);
        viewStage.show();
    }

    private ObservableList<Donor> fetchDonorsFromDatabase() {
        ObservableList<Donor> donors = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Donors")) {

            while (resultSet.next()) {
                int donorId = resultSet.getInt("donor_id");
                String email = resultSet.getString("email");
                String fullName = resultSet.getString("full_name");
                int diseaseId = resultSet.getInt("DiseaseID");
                Timestamp registrationDate = resultSet.getTimestamp("registration_date");
                Donor donor = new Donor(donorId, email, fullName, diseaseId, registrationDate.toLocalDateTime());
                donors.add(donor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return donors;
    }

   
	
public void clearDonorFields(TextField emailTextField, TextField fullNameTextField, TextField fatherNameTextField,
			TextField bloodGroupTextField, TextField motherNameTextField, TextField cityTextField,
			TextField addressTextField, TextField mobileTextField, TextField genderTextField) {
    	 emailTextField.clear();
         fullNameTextField.clear();
         bloodGroupTextField.clear();
         fatherNameTextField.clear();
         cityTextField.clear();
        motherNameTextField.clear();
        addressTextField.clear();
        mobileTextField.clear();
        genderTextField.clear();
       		
	}
	private void showAlert(String message) {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.setTitle("Information");
     alert.setHeaderText(null);
     alert.setContentText(message);
     alert.showAndWait();
 }

 private boolean isDonorExists(String full_name) {
     try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
          PreparedStatement statement = connection.prepareStatement(
                  "SELECT * FROM Donors WHERE full_name = ?")) {

         statement.setString(1, full_name);
         ResultSet resultSet = statement.executeQuery();

         // If the resultSet has any rows, it means a recipient with the given name exists
         return resultSet.next();

     } catch (SQLException ex) {
         ex.printStackTrace();
         return false;  // Assume recipient doesn't exist in case of an exception
     }
 }

 private int getDonorId(final String fullName) {
     try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
          PreparedStatement statement = connection.prepareStatement(
                  "SELECT donor_id FROM Donors WHERE full_name = ?")) {

         statement.setString(1, fullName);
         ResultSet resultSet = statement.executeQuery();

         if (resultSet.next()) {
             return resultSet.getInt("donor_id");
         }

     } catch (SQLException e) {
         e.printStackTrace();
     }
     return -1;
 }


 


 private void deleteDonors() {
     TextInputDialog fullNameDialog = new TextInputDialog();
     fullNameDialog.setTitle("Delete Donor");
     fullNameDialog.setHeaderText("Enter Full Name of Donor to Delete:");
     fullNameDialog.setContentText("Full Name:");

     Optional<String> fullNameResult = fullNameDialog.showAndWait();
     fullNameResult.ifPresent(fullName -> {
         if (isDonorExists(fullName)) {
             Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
             confirmationAlert.setTitle("Confirmation");
             confirmationAlert.setHeaderText("Confirm Deletion");
             confirmationAlert.setContentText("Are you sure you want to delete the donor: " + fullName + "?");

             ButtonType confirmButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
             ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

             confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

             Optional<ButtonType> result = confirmationAlert.showAndWait();
             if (result.isPresent() && result.get() == confirmButton) {
                 // User confirmed the deletion
                 int selecteddonor_id = getDonorId(fullName);

                 try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                      PreparedStatement statement = connection.prepareStatement(
                              "DELETE FROM Donors WHERE donor_id = ?")) {

                     statement.setInt(1, selecteddonor_id);

                     int rowsAffected = statement.executeUpdate();

                     if (rowsAffected > 0) {
                         showAlert("Donor deleted successfully!");
                     } else {
                         showAlert("Failed to delete donor! Donor not found.");
                     }

                 } catch (SQLException ex) {
                     ex.printStackTrace();
                 }
             }
         } else {
             showAlert("Donor not found. Please enter a valid full name.");
         }
     });
 }
 private void searchDonors() {
     TextInputDialog dialog = new TextInputDialog();
     dialog.setTitle("Search Donor");
     dialog.setHeaderText("Enter Search Criteria");
     dialog.setContentText("Full Name:");

     Optional<String> result = dialog.showAndWait();
     result.ifPresent( full_name -> {
         try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
              PreparedStatement statement = connection.prepareStatement(
                      "SELECT * FROM Donors WHERE  full_name = ?")) {

             statement.setString(1, full_name);
             ResultSet resultSet = statement.executeQuery();

             if (resultSet.next()) {
                 displaySearchResults(resultSet);
             } else {
                 showAlert("No donor found with the specified full name.");
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
	            addResultRow(gridPane, "ID:", resultSet.getInt("donor_id"), row++);
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


	public class Donor {
	    private int donorId;
	    private String email;
	    private String fullName;
	    private int diseaseId;
	    private LocalDateTime registrationDate;

	    public Donor(int donorId, String email, String fullName, int diseaseId, LocalDateTime registrationDate) {
	        this.donorId = donorId;
	        this.email = email;
	        this.fullName = fullName;
	        this.diseaseId = diseaseId;
	        this.registrationDate = registrationDate;
	    }

	    public int getDonorId() {
	        return donorId;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public String getFullName() {
	        return fullName;
	    }

	    public int getDiseaseId() {
	        return diseaseId;
	    }

	    public LocalDateTime getRegistrationDate() {
	        return registrationDate;
	    }
	}}