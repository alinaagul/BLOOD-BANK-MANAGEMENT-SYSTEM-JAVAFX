package application;

import application.bloodunit.Disease;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Dashboard extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private String loggedInUsername;

    public void startDashboard(Stage primaryStage, String username) {
        this.loggedInUsername = username;
        primaryStage.setTitle("Blood Bank Management System");

        // Create buttons with 3D effects
        Button DonorReg = createButton("Donor Registration");
        Button RecipientButton = createButton("Recipient Activity");
        Button bloodStockButton = createButton("Blood Stock");
        Button bloodSearchButton = createButton("Search Blood Donor");
        Button dieaseButton = createButton("Find Disease");
        Button contactUsButton = createButton("Contact Us");
        Button exitButton = createButton("Exit");

        // Set button actions
        DonorReg.setOnAction(e -> openDonorRegistrationPage());
        RecipientButton.setOnAction(e -> RecipientActivityPage());
        bloodStockButton.setOnAction(e -> bloodStockButtonPage());
        dieaseButton.setOnAction(e -> diseaseButtonPage());
        bloodSearchButton.setOnAction(e -> searchblooddonorPage());
        contactUsButton.setOnAction(e -> contactUsPage());
        exitButton.setOnAction(e -> exitPage());
        Label titleLabel = new Label("USER PAGE");
        titleLabel.setFont(Font.font("Dialog", FontWeight.BOLD, 26)); 
        titleLabel.setStyle("-fx-text-fill: white;");
        // Create layout
        VBox root = new VBox(20);  // Use VBox for vertical layout
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel,DonorReg, RecipientButton, bloodStockButton, bloodSearchButton, dieaseButton, contactUsButton, exitButton);

        // Set background image
        Image backgroundImage = new Image("https://wallpapercave.com/wp/wp7897969.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(background));
      
        // Create scene
        Scene scene = new Scene(root, 600, 400);

        // Set up stage
        primaryStage.setScene(scene);
        primaryStage.show();

        // Apply button animations
        applyButtonAnimations(DonorReg);
        applyButtonAnimations(RecipientButton);
        applyButtonAnimations(contactUsButton);
        applyButtonAnimations(bloodStockButton);
        applyButtonAnimations(bloodSearchButton);
        applyButtonAnimations(dieaseButton);
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
        FillTransition fillTransition = new FillTransition(Duration.millis(500), Color.DODGERBLUE, Color.LIGHTGRAY);
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);

        button.setOnMouseEntered(event -> {
            fillTransition.playFromStart();
        });
    }
    private void diseaseButtonPage() {
        System.out.println("Disease page clicked");
        
        // Create a new instance of the disease class
        disease diseaseInstance = new disease();
        
        // Call the start method on the disease instance to show the new stage
        diseaseInstance.start(new Stage());
    }

    private void exitPage() {
        Stage exitPage = new Stage();
        Exit exit = new Exit();
        Exit exit2 = new Exit();
		exit2.start(exitPage);
    }

    private void contactUsPage() {
        System.out.println("Contact Us clicked");
        Stage ContactUsStage = new Stage();
        ContactUs ContactUs = new ContactUs();
        ContactUs.start(ContactUsStage);
    }

    private void searchblooddonorPage() {
        System.out.println("Search Blood Donor clicked");
        Stage bloodStockStage = new Stage();
        BloodStock bloodStock = new BloodStock();
        bloodStock.start(bloodStockStage);
    }

    private void RecipientActivityPage() {
        System.out.println("Recipient Activity clicked");
        Stage Recipient = new Stage();
        Recipient recipient = new Recipient();
        recipient.start(Recipient);
    }

    private void bloodStockButtonPage() {
        System.out.println("Blood Stock clicked");
        Stage bloodStockdetStage = new Stage();
        BloodStockdet bloodStockdet = new BloodStockdet();
        bloodStockdet.start(bloodStockdetStage);
    }

    private void openDonorRegistrationPage() {
        System.out.println("Donor Registration clicked");

        Stage donorRegistrationStage = new Stage();
        registerdonor donorRegistration = new registerdonor();
        donorRegistration.start(donorRegistrationStage);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Launching the main dashboard with a test username
        startDashboard(primaryStage, "TestUser");
    }
}