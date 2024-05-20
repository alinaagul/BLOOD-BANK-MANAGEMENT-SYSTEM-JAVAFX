package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Login extends Application {
    private Stage primaryStage;
    private Map<String, String> userAccounts = new HashMap<>();
    private TextField loginIdField;
    private PasswordField loginPassField;
    private TextField signupIdField;
    private PasswordField signupPassField;

    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USERNAME = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Blood Bank Management System");

        // Create a BorderPane to hold the left and right panes
        BorderPane borderPane = new BorderPane();

        VBox leftPane = createLeftPane();
        VBox rightPane = createRightPane();

        // Set the left pane to the left side and the right pane to the center
        borderPane.setLeft(leftPane);
        borderPane.setCenter(rightPane);

        Scene scene = new Scene(borderPane, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftPane() {
        VBox leftPane = new VBox(20);
        leftPane.setPrefWidth(400); // Adjust the preferred width as needed
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #2C3E50;"); // Dark Grey Background

        // Load an image for the left pane
        Image leftImage = new Image("https://img.freepik.com/free-vector/blood-donor-composition-with-hand-blood-droplet-flat_1284-61734.jpg?w=740&t=st=1701409066~exp=1701409666~hmac=daf1a53bab7d2ac5c581f6ece33e65953e61aab59c5b6247d045d01133cc657f"); // Replace with the actual path to your image
        ImageView leftImageView = new ImageView(leftImage);
        leftImageView.setFitWidth(350); // Set the width as needed
        leftImageView.setFitHeight(220); // Set the height as needed

        Label welcomeLabel = new Label("WELCOME TO BBMS");
        welcomeLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: white;");
        Button signupButton = new Button("SIGN UP");
        signupButton.setStyle("-fx-font-size: 16; -fx-text-fill: black;"); // Black Text
        signupButton.setOnAction(e -> showSecondStage("Signup"));

        leftPane.getChildren().addAll(leftImageView, welcomeLabel, signupButton);
        leftPane.setAlignment(Pos.CENTER);

        return leftPane;
    }

    private VBox createRightPane() {
        VBox rightPane = new VBox(10);
        rightPane.setPrefWidth(200); // Adjust the preferred width as needed
        rightPane.setPadding(new Insets(10, 20, 10, 20));
        rightPane.setStyle("-fx-background-color: #C1B4AE;"); // Set the background color

        Label headerLabel = new Label("Give the Gift of Life - DONATE BLOOD");
        headerLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: red;");

        Label optionLabel = new Label("Option: Login");

        GridPane loginGridPane = createLoginGridPane();

        rightPane.getChildren().addAll(headerLabel, optionLabel, loginGridPane);
        rightPane.setAlignment(Pos.CENTER);

        return rightPane;
    }

    private GridPane createLoginGridPane() {
        GridPane loginGridPane = new GridPane();
        loginGridPane.setHgap(10);
        loginGridPane.setVgap(10);

        Label loginIdLabel = new Label("Username/Email");
        Label loginPassLabel = new Label("Password");

        loginIdField = new TextField();
        loginPassField = new PasswordField();

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setOnAction(e -> showForgotPasswordAlert());

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginActionPerformed());

        loginGridPane.add(loginIdLabel, 0, 0);
        loginGridPane.add(loginIdField, 1, 0);
        loginGridPane.add(loginPassLabel, 0, 1);
        loginGridPane.add(loginPassField, 1, 1);
        loginGridPane.add(forgotPasswordLink, 1, 2);
        loginGridPane.add(loginButton, 1, 3);

        return loginGridPane;
    }

    private void showSecondStage(String option) {
        Stage secondStage = new Stage();
        secondStage.setTitle(option);

        VBox secondStageContent = createSecondStageContent(option);
        Scene secondStageScene = new Scene(secondStageContent, 400, 300);

        secondStage.setScene(secondStageScene);
        secondStage.initOwner(primaryStage);
        secondStage.initModality(Modality.WINDOW_MODAL);
        secondStage.show();
    }

    private VBox createSecondStageContent(String option) {
        VBox secondStageContent = new VBox(10);
        secondStageContent.setPadding(new Insets(10, 20, 10, 20));
        GridPane gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: #FFDBB5;");

        Label headerLabel = new Label("Give the Gift of Life - DONATE BLOOD");
        headerLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: red;");

        ImageView heartbeatIcon = new ImageView(new Image("https://www.google.com/imgres?imgurl=https%3A%2F%2Fimages.assetsdelivery.com%2Fcompings_v2%2Fsmile3377%2Fsmile33771905%2Fsmile3377190500032.jpg&tbnid=GkApPh4Jn5mz7M&vet=12ahUKEwiJhfX3we6CAxWNU6QEHSv1D2cQMygzegUIARDzAQ..i&imgrefurl=https%3A%2F%2Fwww.tasmeemme.com%2Fen%2Fstore-items%2Fred-heartbeat-line-isolated-on-white-background-heartbeat-icon%2F%3Fitem%3D10122025982&docid=D_URit5_TQGMAM&w=450&h=375&q=heartbeat%20icon&hl=en-GB&ved=2ahUKEwiJhfX3we6CAxWNU6QEHSv1D2cQMygzegUIARDzAQ")); // Replace with the actual path to your icon
        heartbeatIcon.setFitWidth(200);
        heartbeatIcon.setFitHeight(50);

        Label optionLabel = new Label("Option: " + option);

        if (option.equals("Login")) {
            GridPane loginGridPane = createLoginGridPane();
            secondStageContent.getChildren().addAll(headerLabel, optionLabel, loginGridPane);
        } else if (option.equals("Signup")) {
            GridPane signupGridPane = createSignupGridPane();
            secondStageContent.getChildren().addAll(headerLabel, optionLabel, signupGridPane);
        }

        return secondStageContent;
    }

    private GridPane createSignupGridPane() {
        GridPane signupGridPane = new GridPane();
        signupGridPane.setHgap(10);
        signupGridPane.setVgap(10);

        Label signupIdLabel = new Label("New Username");
        Label signupPassLabel = new Label("New Password");

        signupIdField = new TextField();
        signupPassField = new PasswordField();

        Button signupButton = new Button("Create Account");
        signupButton.setOnAction(e -> signupActionPerformed());

        signupGridPane.add(signupIdLabel, 0, 0);
        signupGridPane.add(signupIdField, 1, 0);
        signupGridPane.add(signupPassLabel, 0, 1);
        signupGridPane.add(signupPassField, 1, 1);
        signupGridPane.add(signupButton, 1, 2);

        return signupGridPane;
    }

    private void loginActionPerformed() {
        String username = loginIdField.getText();
        String password = loginPassField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter both username and password");
            return;
        }

        if (DatabaseHandler.validateUser(username, password)) {
            showAlert("Login Successful", "Welcome, " + username + "!");
            showDashboardStage(username); // Open the dashboard stage
        } else {
            showAlert("Login Failed", "Invalid username or password");
        }
    }


    private void showDashboardStage(String username) {
    	primaryStage.close();

        // Create and show the dashboard
        Stage dashboardStage = new Stage();
        Dashboard dashboard = new Dashboard();
        dashboard.startDashboard(dashboardStage, username);
    }

    private void signupActionPerformed() {
        String username = signupIdField.getText();
        String password = signupPassField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Signup Failed", "Please enter both username and password");
            return;
        }

        if (!userAccounts.containsKey(username)) {
            userAccounts.put(username, password);
            showAlert("Signup Successful", "Account created for " + username);
            showDashboardStage(username); // Open the dashboard stage
        } else {
            showAlert("Signup Failed", "Username already exists. Choose a different username.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showForgotPasswordAlert() {
        Alert forgotPasswordAlert = new Alert(Alert.AlertType.INFORMATION);
        forgotPasswordAlert.setTitle("Forgot Password");
        forgotPasswordAlert.setHeaderText(null);
        forgotPasswordAlert.setContentText("Please contact support to reset your password.");
        forgotPasswordAlert.showAndWait();
    }
    private static class DatabaseHandler {
        static {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        static boolean validateUser(String username, String password) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        static boolean createUser(String username, String password) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}

