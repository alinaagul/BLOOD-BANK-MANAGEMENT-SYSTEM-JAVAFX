package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Start extends Application {
	 private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	    private static final String DB_USERNAME = "system";
	    private static final String DB_PASSWORD = "Oracle_1";
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blood Bank Management System");

        // Call the displayStartPage method from the start method
        displayStartPage(primaryStage);
    }

    public static void displayStartPage(Stage primaryStage) {
        VBox startPane = createStartPane(primaryStage);
        Scene startScene = new Scene(startPane, 800, 400);

        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    private static VBox createStartPane(Stage primaryStage) {
        // Create a Pane to hold the background image
        Pane backgroundPane = new Pane();

        // Load the image for the start pane
        Image startImage = new Image("C:\\Users\\HP\\Downloads\\coronavirus-blood-samples-arrangement-lab.jpg");
        ImageView startImageView = new ImageView(startImage);
        startImageView.setFitWidth(800); // Set the width to the scene width
        startImageView.setFitHeight(400); // Set the height to the scene height

        // Set the background image
        backgroundPane.getChildren().add(startImageView);
        VBox startPane = new VBox(20);
        startPane.setPadding(new Insets(20));
        startPane.setAlignment(Pos.CENTER);
        startPane.setStyle("-fx-background-color: transparent;"); // Set transparent background

        Label startLabel = new Label("Click below to enter into BLOOD BANK MANAGEMENT SYSTEM");
        startLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill:WHITE;");

        Button userButton = createStyledButton("Login as USER");
        userButton.setOnAction(e -> showLoginPage(primaryStage, "USER"));

        Button adminButton = createStyledButton("Login as ADMIN");
        adminButton.setOnAction(e -> showLoginPage(primaryStage, "ADMIN"));

        startPane.getChildren().addAll(startLabel, userButton, adminButton);

        StackPane stackPane = new StackPane(backgroundPane, startPane);

        // Create a VBox to hold the StackPane
        VBox vbox = new VBox(stackPane);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    private static Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-font-size: 16; " +
                "-fx-background-color: linear-gradient(to bottom, #8B0000, #000000);" +
                "-fx-text-fill: white;"
        );
        button.setMinSize(200, 40);
        button.setEffect(new DropShadow(10, Color.GRAY));

        // Apply animations
        applyButtonAnimations(button);

        return button;
    }

    private static void applyButtonAnimations(Button button) {
        FillTransition fillTransition = new FillTransition(Duration.millis(500), Color.DODGERBLUE, Color.LIGHTGRAY);
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);

        button.setOnMouseEntered(event -> fillTransition.playFromStart());
    }

   //VIEW
    	private static void showLoginPage(Stage primaryStage, String userType) {
            if ("USER".equals(userType)) {
                String username = "user123"; // Get the username from the user input
                String password = "password123"; // Get the password from the user input
                Login login = new Login();
                login.start(primaryStage);
                if (authenticateUser(username, password, userType)) {
                    
                } else {
                    // Failed login, show an error message
                }
            } else if ("ADMIN".equals(userType)) {
            	 adminlogin adminlogin = new adminlogin();
                 adminlogin.start(primaryStage);
             }
        }
//VIEW
    	private static boolean authenticateUser(String username, String password, String role) {
    	    String query = "SELECT * FROM starttable WHERE username = ? AND password = ? AND role = ?";
    	    
    	    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
    	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

    	        preparedStatement.setString(1, username);
    	        preparedStatement.setString(2, password);
    	        preparedStatement.setString(3, role);

    	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
    	            return resultSet.next(); // Returns true if a matching user is found
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        return false;
    	    }
    	}
}