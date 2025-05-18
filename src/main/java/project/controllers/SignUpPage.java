package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import project.database.DatabaseHelper;
import project.database.entities.UserEntity;
import project.utils.ColorManager;

import java.sql.SQLException;

public class SignUpPage {

    public static Scene getScene(Stage primaryStage) {
        // Main layout container with two sides
        HBox mainContainer = new HBox();
        
        // Left side - Luxury image or gradient panel (40% of width)
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(480); // 40% of 1200px
        leftPanel.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + 
            ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", " + 
            "derive(" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", 30%));" +
            "-fx-effect: dropshadow(three-pass-box, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.4) + ", 15, 0, 0, 0);"
        );
        
        // Logo or app icon at the top
        ImageView logoView = new ImageView();
        try {
            Image logoImage = new Image("file:src/main/resources/Image/logo.png");
            logoView.setImage(logoImage);
            logoView.setFitHeight(100);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Logo not found, using text instead");
            Text logoText = new Text("FairFund");
            logoText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            logoText.setFill(ColorManager.TEXT_COLOR);
            leftPanel.getChildren().add(logoText);
        }

        // Welcome message
        VBox welcomeBox = new VBox(15);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(0, 30, 0, 30));
        
        Text welcomeTitle = new Text("Join");
        welcomeTitle.setFont(Font.font("Arial", FontWeight.LIGHT, 36));
        welcomeTitle.setFill(ColorManager.TEXT_COLOR);
        
        Text appTitle = new Text("FairFund");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        appTitle.setFill(ColorManager.TEXT_COLOR);
        
        Text tagline = new Text("Start Tracking Expenses Together");
        tagline.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        tagline.setFill(ColorManager.TEXT_COLOR);
        tagline.setOpacity(0.8);
        
        Line separator = new Line(0, 0, 100, 0);
        separator.setStroke(ColorManager.TEXT_COLOR);
        separator.setOpacity(0.5);
        separator.setStrokeWidth(2);
        separator.setStartX(0);
        separator.setEndX(200);
        
        Text description = new Text("Create an account today and start managing your shared expenses with ease.");
        description.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        description.setFill(ColorManager.TEXT_COLOR);
        description.setOpacity(0.8);
        description.setWrappingWidth(350);
        
        welcomeBox.getChildren().addAll(welcomeTitle, appTitle, tagline, separator, description);
        
        VBox logoContainer = new VBox(30);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.getChildren().addAll(logoView);
        
        // Stack the logo and welcome message with proper spacing
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setSpacing(50);
        leftPanel.getChildren().addAll(logoContainer, welcomeBox);
        
        // Right side - Signup form (60% of width)
        VBox rightPanel = new VBox();
        rightPanel.setPadding(new Insets(40));
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPrefWidth(720); // 60% of 1200px
        rightPanel.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + 
            ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ", " + 
            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ");"
        );
        
        // Signup form container
        VBox signupForm = new VBox(25);
        signupForm.setMaxWidth(450);
        signupForm.setAlignment(Pos.CENTER);
        signupForm.setPadding(new Insets(40));
        signupForm.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-background-radius: 15px;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.1) + ", 20, 0, 0, 0);"
        );
    }
}