package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import application.recact.Recipient;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class bloodunit extends Application {
	  private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	    private static final String DB_USERNAME = "system";
	    private static final String DB_PASSWORD = "Oracle_1";

	    public static void main(String[] args) {
	        launch(args);
	    }
   
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Disease Management");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #FFDBB5;");

        Label titleLabel = new Label("MANAGE DISEASE INFO");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKRED);
        grid.add(titleLabel, 0, 0);
       ;
        Button viewButton = createStyledButton("View Records");
        Button editButton = createStyledButton("Edit Records");
        Button deleteButton = createStyledButton("Delete Records");
        Button searchButton = createStyledButton("Search Records");

        grid.add(viewButton, 1, 1);
        grid.add(editButton, 1, 2);
        grid.add(deleteButton, 1, 3);
        grid.add(searchButton, 1, 4);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();

        // Set button actions with a simple fade-in animation
        setButtonActionWithAnimation(viewButton);
        setButtonActionWithAnimation(editButton);
        setButtonActionWithAnimation(deleteButton);
        setButtonActionWithAnimation(searchButton);
        viewButton.setOnAction(e -> viewRecords());
        editButton.setOnAction(e -> editRecord());
        deleteButton.setOnAction(e -> deleteRecord());
        searchButton.setOnAction(e -> searchRecords());
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

    private void viewRecords() {
        Stage viewStage = new Stage();
        viewStage.setTitle("View Records");

        GridPane viewGrid = new GridPane();
        viewGrid.setPadding(new Insets(20));
        viewGrid.setHgap(20);
        viewGrid.setVgap(20);
        viewGrid.setAlignment(Pos.CENTER);
        viewGrid.setStyle("-fx-background-color: #f4f4f4;");

        // Add title label
        Label titleLabel = new Label("VIEW RECORDS");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
        GridPane.setColumnSpan(titleLabel, 3);
        viewGrid.add(titleLabel, 0, 0);

        // Initialize TableView
        TableView<Disease> viewTableView = new TableView<>();
        viewTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        viewTableView.setStyle("-fx-background-color: #ffffff;");
        TableColumn<Disease, String> donornameColumn = new TableColumn<>("Donor Name");
        donornameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Disease, Integer> idColumn = new TableColumn<>("Disease ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("diseaseId"));

        TableColumn<Disease, String> nameColumn = new TableColumn<>("Disease Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("diseaseName"));

        TableColumn<Disease, String> typeColumn = new TableColumn<>("Disease Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("diseaseType"));

        viewTableView.getColumns().addAll(donornameColumn ,idColumn, nameColumn, typeColumn);
        viewTableView.setStyle("-fx-border-color: #dddddd; -fx-border-width: 1px;");
        viewGrid.add(viewTableView, 0, 1, 2, 1);

        ObservableList<Disease> viewDiseaseList = FXCollections.observableArrayList();
        viewDiseaseList.clear();
        viewDiseaseList.addAll(fetchDiseasesFromDatabase());
        viewTableView.setItems(viewDiseaseList);

        Scene viewScene = new Scene(viewGrid, 800, 600);
        viewStage.setScene(viewScene);
        viewStage.show();
    }

    private ObservableList<Disease> fetchDiseasesFromDatabase() {
        ObservableList<Disease> diseases = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
		     Statement statement = connection.createStatement();
		     ResultSet resultSet = statement.executeQuery("SELECT Donors.donor_id, Donors.full_name, " +
		             "Disease.DiseaseID, Disease.DiseaseName, Disease.Type " +
		             "FROM Donors " +
		             "JOIN Disease ON Donors.DiseaseID = Disease.DiseaseID")) {

		    while (resultSet.next()) {
		    	 int donorId = resultSet.getInt("donor_id");
		         String fullName = resultSet.getString("full_name");
		         int diseaseId = resultSet.getInt("DiseaseID");
		         String diseaseName = resultSet.getString("DiseaseName");
		         String diseaseType = resultSet.getString("Type");

		         Disease disease = new Disease(donorId, fullName, diseaseId, diseaseName, diseaseType);
		         diseases.add(disease);
		    }

		} catch (SQLException e) {
		    e.printStackTrace();
		}

        return diseases;
    }

    public class Disease {
        private final IntegerProperty donorId;
        private final StringProperty fullName;
        private final IntegerProperty diseaseId;
        private final StringProperty diseaseName;
        private final StringProperty diseaseType;

        public Disease(int donorId, String fullName, int diseaseId, String diseaseName, String diseaseType) {
            this.donorId = new SimpleIntegerProperty(donorId);
            this.fullName = new SimpleStringProperty(fullName);
            this.diseaseId = new SimpleIntegerProperty(diseaseId);
            this.diseaseName = new SimpleStringProperty(diseaseName);
            this.diseaseType = new SimpleStringProperty(diseaseType);
        }

        public int getDonorId() {
            return donorId.get();
        }

        public IntegerProperty donorIdProperty() {
            return donorId;
        }

        public String getFullName() {
            return fullName.get();
        }

        public StringProperty fullNameProperty() {
            return fullName;
        }

        public int getDiseaseId() {
            return diseaseId.get();
        }

        public IntegerProperty diseaseIdProperty() {
            return diseaseId;
        }

        public String getDiseaseName() {
            return diseaseName.get();
        }

        public StringProperty diseaseNameProperty() {
            return diseaseName;
        }

        public String getDiseaseType() {
            return diseaseType.get();
        }

        public StringProperty diseaseTypeProperty() {
            return diseaseType;
        }
    }
 // ...

    private void addRecord() {
        Stage addStage = new Stage();
        addStage.setTitle("Add Record");

        GridPane addGrid = new GridPane();
        addGrid.setPadding(new Insets(20));
        addGrid.setHgap(20);
        addGrid.setVgap(20);
        addGrid.setAlignment(Pos.CENTER);
        addGrid.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("ADD RECORD");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
        GridPane.setColumnSpan(titleLabel, 3);
        addGrid.add(titleLabel, 0, 0);

        TextField donorNameField = new TextField();
        donorNameField.setPromptText("Donor Name");
        TextField diseaseNameField = new TextField();
        diseaseNameField.setPromptText("Disease Name");
        TextField diseaseTypeField = new TextField();
        diseaseTypeField.setPromptText("Disease Type");

        Button addButton = createStyledButton("Add Record");

        addGrid.add(new Label("Donor Name:"), 0, 1);
        addGrid.add(donorNameField, 1, 1);
        addGrid.add(new Label("Disease Name:"), 0, 2);
        addGrid.add(diseaseNameField, 1, 2);
        addGrid.add(new Label("Disease Type:"), 0, 3);
        addGrid.add(diseaseTypeField, 1, 3);
        addGrid.add(addButton, 1, 4);

        addButton.setOnAction(e -> {
            String donorName = donorNameField.getText();
            String diseaseName = diseaseNameField.getText();
            String diseaseType = diseaseTypeField.getText();

            if (!donorName.isEmpty() && !diseaseName.isEmpty() && !diseaseType.isEmpty()) {
                addNewRecord(donorName, diseaseName, diseaseType);
                addStage.close();
            } else {
            	showAlert("Please fill in all fields");
            }
        });

        Scene addScene = new Scene(addGrid, 400, 300);
        addStage.setScene(addScene);
        addStage.show();
    }

    private void showAlert(String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Warning");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

	private void addNewRecord(String donorName, String diseaseName, String diseaseType) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // Load the Oracle JDBC driver
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD)) {
                String insertQuery = "INSERT INTO Donors (full_name) VALUES (?)";
                try (PreparedStatement donorStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                    donorStatement.setString(1, donorName);
                    donorStatement.executeUpdate();

                    ResultSet donorResultSet = donorStatement.getGeneratedKeys();
                    int donorId = -1;
                    if (donorResultSet.next()) {
                        donorId = donorResultSet.getInt(1);
                    }

                    String insertDiseaseQuery = "INSERT INTO Disease (DiseaseName, Type, DonorID) VALUES (?, ?, ?)";
                    try (PreparedStatement diseaseStatement = connection.prepareStatement(insertDiseaseQuery)) {
                        diseaseStatement.setString(1, diseaseName);
                        diseaseStatement.setString(2, diseaseType);
                        diseaseStatement.setInt(3, donorId);
                        diseaseStatement.executeUpdate();
                        connection.commit();

                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle the exception, show an alert or provide feedback to the user
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception, show an alert or provide feedback to the user
        }
    }
	private void editRecord() {
	    Stage editStage = new Stage();
	    editStage.setTitle("Edit Record");

	    GridPane editGrid = new GridPane();
	    editGrid.setPadding(new Insets(20));
	    editGrid.setHgap(20);
	    editGrid.setVgap(20);
	    editGrid.setAlignment(Pos.CENTER);
	    editGrid.setStyle("-fx-background-color: #f4f4f4;");

	    Label titleLabel = new Label("EDIT RECORD");
	    titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
	    GridPane.setColumnSpan(titleLabel, 3);
	    editGrid.add(titleLabel, 0, 0);

	    TextField fullNameField = new TextField();
	    fullNameField.setPromptText("Enter Full Name");

	    Button editButton = createStyledButton("Edit Record");

	    editGrid.add(new Label("Full Name:"), 0, 1);
	    editGrid.add(fullNameField, 1, 1);
	    editGrid.add(editButton, 1, 2);

	    editButton.setOnAction(e -> {
	        String fullName = fullNameField.getText();
	        if (!fullName.isEmpty()) {
	            editExistingRecord(fullName);
	            editStage.close();
	        } else {
	            showAlert("Please enter the full name");
	        }
	    });

	    Scene editScene = new Scene(editGrid, 400, 200);
	    editStage.setScene(editScene);
	    editStage.show();
	}

	private void editExistingRecord(String fullName) {
	    // Fetch existing details based on the entered full name
	    Disease existingDisease = fetchDiseaseByFullName(fullName);

	    if (existingDisease != null) {
	        openEditDetailsWindow(existingDisease);
	    } else {
	        showAlert("No records found for the entered full name.");
	    }
	}
	
	private Disease fetchDiseaseByFullName(String fullName) {
	    Disease existingDisease = null;

	    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD)) {
	        String selectQuery = "SELECT Donors.donor_id, Donors.full_name, Disease.DiseaseID, Disease.DiseaseName, Disease.Type " +
	                             "FROM Donors " +
	                             "JOIN Disease ON Donors.DiseaseID = Disease.DiseaseID " +
	                             "WHERE Donors.full_name = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
	            preparedStatement.setString(1, fullName);
	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    // Record found, create a Disease object
	                    int donorId = resultSet.getInt("donor_id");
	                    int diseaseId = resultSet.getInt("DiseaseID");
	                    String diseaseName = resultSet.getString("DiseaseName");
	                    String diseaseType = resultSet.getString("Type");

	                    existingDisease = new Disease(donorId, fullName, diseaseId, diseaseName, diseaseType);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return existingDisease;
	}

		private void openEditDetailsWindow(Disease existingDisease) {
		    Stage editDetailsStage = new Stage();
		    editDetailsStage.setTitle("Edit Details");

		    GridPane editDetailsGrid = new GridPane();
		    editDetailsGrid.setPadding(new Insets(20));
		    editDetailsGrid.setHgap(20);
		    editDetailsGrid.setVgap(20);
		    editDetailsGrid.setAlignment(Pos.CENTER);
		    editDetailsGrid.setStyle("-fx-background-color: #f4f4f4;");

		    Label titleLabel = new Label("EDIT DETAILS");
		    titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
		    GridPane.setColumnSpan(titleLabel, 3);
		    editDetailsGrid.add(titleLabel, 0, 0);

		    TextField fullNameField = new TextField(existingDisease.getFullName());
		    fullNameField.setEditable(false); // Full name is not editable

		    TextField diseaseNameField = new TextField(existingDisease.getDiseaseName());
		    TextField diseaseTypeField = new TextField(existingDisease.getDiseaseType());

		    Button saveButton = createStyledButton("Save Changes");

		    editDetailsGrid.add(new Label("Full Name:"), 0, 1);
		    editDetailsGrid.add(fullNameField, 1, 1);
		    editDetailsGrid.add(new Label("Disease Name:"), 0, 2);
		    editDetailsGrid.add(diseaseNameField, 1, 2);
		    editDetailsGrid.add(new Label("Disease Type:"), 0, 3);
		    editDetailsGrid.add(diseaseTypeField, 1, 3);
		    editDetailsGrid.add(saveButton, 1, 4);

		    saveButton.setOnAction(e -> {
		        String editedDiseaseName = diseaseNameField.getText();
		        String editedDiseaseType = diseaseTypeField.getText();

		        if (!editedDiseaseName.isEmpty() && !editedDiseaseType.isEmpty()) {
		            updateDiseaseDetails(existingDisease, editedDiseaseName, editedDiseaseType);
		            editDetailsStage.close();
		        } else {
		            showAlert("Please fill in all fields");
		        }
		    });


		    Scene editDetailsScene = new Scene(editDetailsGrid, 400, 300);
		    editDetailsStage.setScene(editDetailsScene);
		    editDetailsStage.show();
		}

		 private void updateDiseaseDetails(Disease existingDisease, String editedDiseaseName, String editedDiseaseType) {
		        try {
		            Class.forName("oracle.jdbc.driver.OracleDriver");
		            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD)) {
		                String storedProcedureCall = "{call UPDATE_DISEASE_DETAILS(?, ?, ?)}";
		                try (CallableStatement callableStatement = connection.prepareCall(storedProcedureCall)) {
		                    callableStatement.setInt(1, existingDisease.getDiseaseId());
		                    callableStatement.setString(2, editedDiseaseName);
		                    callableStatement.setString(3, editedDiseaseType);

		                    callableStatement.executeUpdate();
		                }
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                showAlert("Error updating Disease details.");
		            }
		        } catch (ClassNotFoundException e) {
		            e.printStackTrace();
		            showAlert("Error connecting to the database.");
		        }
		    }
		 private void searchRecords() {
			    Stage searchStage = new Stage();
			    searchStage.setTitle("Search Records");

			    GridPane searchGrid = new GridPane();
			    searchGrid.setPadding(new Insets(20));
			    searchGrid.setHgap(20);
			    searchGrid.setVgap(20);
			    searchGrid.setAlignment(Pos.CENTER);
			    searchGrid.setStyle("-fx-background-color: #f4f4f4;");

			    Label titleLabel = new Label("SEARCH RECORDS");
			    titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
			    GridPane.setColumnSpan(titleLabel, 3);
			    searchGrid.add(titleLabel, 0, 0);

			    TextField searchField = new TextField();
			    searchField.setPromptText("Enter Full Name");

			    Button searchButton = createStyledButton("Search Records");

			    searchGrid.add(new Label("Full Name:"), 0, 1);
			    searchGrid.add(searchField, 1, 1);
			    searchGrid.add(searchButton, 1, 2);

			    searchButton.setOnAction(e -> {
			        String fullName = searchField.getText();
			        if (!fullName.isEmpty()) {
			            searchDisease(fullName);
			            searchStage.close();
			        } else {
			            showAlert("Please enter the full name");
			        }
			    });

			    Scene searchScene = new Scene(searchGrid, 400, 200);
			    searchStage.setScene(searchScene);
			    searchStage.show();
			}

			private void searchDisease(String fullName) {
			    ObservableList<Disease> searchDiseaseList = FXCollections.observableArrayList();
			    searchDiseaseList.clear();
			    Disease searchedDisease = fetchDiseaseByFullName(fullName);

			    if (searchedDisease != null) {
			        searchDiseaseList.add(searchedDisease);

			        Stage searchResultStage = new Stage();
			        searchResultStage.setTitle("Search Results");

			        GridPane searchResultGrid = new GridPane();
			        searchResultGrid.setPadding(new Insets(20));
			        searchResultGrid.setHgap(20);
			        searchResultGrid.setVgap(20);
			        searchResultGrid.setAlignment(Pos.CENTER);
			        searchResultGrid.setStyle("-fx-background-color: #f4f4f4;");

			        Label resultTitleLabel = new Label("SEARCH RESULTS");
			        resultTitleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
			        GridPane.setColumnSpan(resultTitleLabel, 3);
			        searchResultGrid.add(resultTitleLabel, 0, 0);

			        TableView<Disease> searchResultTableView = new TableView<>();
			        searchResultTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			        searchResultTableView.setStyle("-fx-background-color: #ffffff;");

			        TableColumn<Disease, String> donornameColumn = new TableColumn<>("Donor Name");
			        donornameColumn.setCellValueFactory(new PropertyValueFactory<>("full_name"));

			        TableColumn<Disease, Integer> idColumn = new TableColumn<>("Disease ID");
			        idColumn.setCellValueFactory(new PropertyValueFactory<>("diseaseId"));

			        TableColumn<Disease, String> nameColumn = new TableColumn<>("Disease Name");
			        nameColumn.setCellValueFactory(new PropertyValueFactory<>("diseaseName"));

			        TableColumn<Disease, String> typeColumn = new TableColumn<>("Disease Type");
			        typeColumn.setCellValueFactory(new PropertyValueFactory<>("diseaseType"));

			        searchResultTableView.getColumns().addAll(donornameColumn, idColumn, nameColumn, typeColumn);
			        searchResultTableView.setStyle("-fx-border-color: #dddddd; -fx-border-width: 1px;");
			        searchResultGrid.add(searchResultTableView, 0, 1, 2, 1);

			        searchResultTableView.setItems(searchDiseaseList);

			        Scene searchResultScene = new Scene(searchResultGrid, 800, 600);
			        searchResultStage.setScene(searchResultScene);
			        searchResultStage.show();
			    } else {
			        showAlert("No records found for the entered full name.");
			    }
			}
			private void deleteRecord() {
			    Stage deleteStage = new Stage();
			    deleteStage.setTitle("Delete Record");

			    GridPane deleteGrid = new GridPane();
			    deleteGrid.setPadding(new Insets(20));
			    deleteGrid.setHgap(20);
			    deleteGrid.setVgap(20);
			    deleteGrid.setAlignment(Pos.CENTER);
			    deleteGrid.setStyle("-fx-background-color: #f4f4f4;");

			    Label titleLabel = new Label("DELETE RECORD");
			    titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");
			    GridPane.setColumnSpan(titleLabel, 3);
			    deleteGrid.add(titleLabel, 0, 0);

			    TextField fullNameField = new TextField();
			    fullNameField.setPromptText("Enter Full Name");

			    Button deleteButton = createStyledButton("Delete Record");

			    deleteGrid.add(new Label("Full Name:"), 0, 1);
			    deleteGrid.add(fullNameField, 1, 1);
			    deleteGrid.add(deleteButton, 1, 2);

			    deleteButton.setOnAction(e -> {
			        String fullName = fullNameField.getText();
			        if (!fullName.isEmpty()) {
			            deleteDiseaseRecord(fullName);
			            deleteStage.close();
			        } else {
			            showAlert("Please enter the full name");
			        }
			    });

			    Scene deleteScene = new Scene(deleteGrid, 400, 200);
			    deleteStage.setScene(deleteScene);
			    deleteStage.show();
			}

			private void deleteDiseaseRecord(String fullName) {
			    Disease existingDisease = fetchDiseaseByFullName(fullName);

			    if (existingDisease != null) {
			        // Ask for confirmation before deleting the record
			        boolean confirmDeletion = showConfirmationAlert("Are you sure you want to delete the record for " + fullName + "?");
			        if (confirmDeletion) {
			            // Execute the delete query
			        	executeDeleteStoredProcedure(existingDisease.getDiseaseId());
			            showAlert("Record deleted successfully");
			        } else {
			            showAlert("Deletion canceled");
			        }
			    } else {
			        showAlert("No records found for the entered full name.");
			    }
			}

			private void executeDeleteStoredProcedure(int diseaseId) {
			    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD)) {
			        String storedProcedureCall = "{call DELETE_DISEASE(?)}";
			        try (CallableStatement callableStatement = connection.prepareCall(storedProcedureCall)) {
			            callableStatement.setInt(1, diseaseId);
			            int affectedRows = callableStatement.executeUpdate();

			            if (affectedRows > 0) {
			                // If a disease record is deleted, also delete associated records in the Donors table
			                deleteAssociatedDonors(diseaseId);
			                
			                showAlert("Record deleted successfully");
			            } else {
			                showAlert("Record not found for deletion");
			            }
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			        showAlert("Error deleting record: " + e.getMessage());
			    }
			}

			
			
			
			//TRIGGER WORK
			
			private void deleteAssociatedDonors(int diseaseId) {
			    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD)) {
			        String query = "DELETE FROM Donors WHERE DiseaseID = ?";
			        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			            preparedStatement.setInt(1, diseaseId);
			            preparedStatement.executeUpdate();
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			        showAlert("Error deleting associated donors: " + e.getMessage());
			    }
			}

private boolean showConfirmationAlert(String message) {
			    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
			    confirmationAlert.setTitle("Confirmation");
			    confirmationAlert.setHeaderText(null);
			    confirmationAlert.setContentText(message);

			    Optional<ButtonType> result = confirmationAlert.showAndWait();
			    return result.isPresent() && result.get() == ButtonType.OK;
			}

    }