package application;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class admindashboard extends Application {

    private String loggedInUsername;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        try {
            start1(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startDashboard(Stage primaryStage, String username) {
        this.loggedInUsername = username;
        primaryStage.setTitle("Blood Bank Management System");

        // Create buttons with 3D effects
        Button donorMan = createButton("DONOR MANAGEMENT");
        Button bloodUnit = createButton("DISEASE MANGEMENT");
        Button bloodType = createButton("BLOOD TYPE MANAGEMENT");
        Button recAct = createButton("RECIPIENT ACTIVITY");
        Button exitButton = createButton("EXIT");

        // Set button actions
        donorMan.setOnAction(e -> DonorManPage());
        bloodUnit.setOnAction(e -> bloodUnitPage());
        bloodType.setOnAction(e -> bloodTypePage());
        recAct.setOnAction(e -> RecAct());
        exitButton.setOnAction(e -> exitPage());

        // Create layout
        VBox buttonBox = new VBox(20); // Use VBox for vertical layout
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(donorMan, bloodUnit, bloodType, recAct, exitButton);

        // Set background image
        Image backgroundImage = new Image("https://wallpapercave.com/wp/wp7897969.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        buttonBox.setBackground(new Background(background));

       //  Create a label for the title
        Label titleLabel = new Label("ADMIN PAGE");
        titleLabel.setFont(Font.font("Dialog", FontWeight.BOLD, 26)); 
        titleLabel.setStyle("-fx-text-fill: white;");

        // Create a stack pane to hold the label and buttons
        StackPane root = new StackPane();
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(buttonBox, titleLabel);

        // Create scene
        Scene scene = new Scene(root, 600, 400);

        // Set up stage
        primaryStage.setScene(scene);
        primaryStage.show();

        // Apply button animations
        applyButtonAnimations(donorMan);
        applyButtonAnimations(bloodUnit);
        applyButtonAnimations(bloodType);
        applyButtonAnimations(recAct);
        applyButtonAnimations(exitButton);
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14; -fx-background-color: linear-gradient(to bottom,  #8B0000, #000000); -fx-text-fill: white;");
        button.setMinSize(200, 50);
        button.setEffect(new DropShadow(10, Color.GRAY));

        return button;
    }

    private void applyButtonAnimations(Button button) {
        FillTransition fillTransition = new FillTransition(Duration.millis(500), Color.rgb(128, 0, 0), Color.rgb(128, 0, 0, 0.5));
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);

        button.setOnMouseEntered(event -> {
            fillTransition.playFromStart();
        });
    }

    private void RecAct() {
        System.out.println("Recipient Activity clicked");
        Stage RecActStage = new Stage();
        recact Recact = new  recact();
       Recact.start(RecActStage);
    }

    private void bloodTypePage() {
        System.out.println("Blood Type Management  clicked");
        Stage bloodTypeStage = new Stage();
        bloodtype Bloodtype = new  bloodtype();
        Bloodtype.start(bloodTypeStage);
    }

    private void bloodUnitPage() {
        System.out.println("Blood transfusion page clicked");
            Stage bloodUnitStage = new Stage();
            bloodunit BloodUnit = new  bloodunit();
            BloodUnit.start(bloodUnitStage);
        }

    private void DonorManPage() {
        System.out.println("Donor Management clicked");
        Stage donorManStage = new Stage();
        DonorManagementApp ManRegistration = new DonorManagementApp();
        ManRegistration.start(donorManStage);
    }


    private void exitPage() {
    	Stage exitPage = new Stage();
        Exit exit = new Exit();
        Exit exit2 = new Exit();
		exit2.start(exitPage);
        System.out.println("EXIT clicked");
    }

    public void start1(Stage primaryStage) throws Exception {
        // Launching the main dashboard with a test username
        startDashboard(primaryStage, "TestUser");
    }
}
