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

        // Form title
        Text formTitle = new Text("Create Account");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        formTitle.setFill(ColorManager.DARK_GRAY);
        
        // Tab-like buttons for Login/Signup
        HBox tabButtons = new HBox();
        tabButtons.setAlignment(Pos.CENTER);
        tabButtons.setSpacing(0);
        tabButtons.setMaxWidth(360);
        tabButtons.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.LIGHT_GRAY_BG) + ";" + 
            "-fx-background-radius: 30px;" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BORDER_COLOR) + ";" +
            "-fx-border-radius: 30px;"
        );
        
        Button loginTab = new Button("Login");
        loginTab.setPrefWidth(180);
        loginTab.setPrefHeight(50);
        loginTab.setStyle(
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 30px 0px 0px 30px;" +
            "-fx-font-weight: bold;"
        );
        
        loginTab.setOnAction(e -> {
            primaryStage.setScene(LoginPage.getScene(primaryStage)); // Navigate to LoginPage
        });
        
        Button signupTab = new Button("Sign Up");
        signupTab.setPrefWidth(180);
        signupTab.setPrefHeight(50);
        signupTab.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 0px 30px 30px 0px;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 3, 0, 0, 1);"
        );
        
        tabButtons.getChildren().addAll(loginTab, signupTab);

        // Username field with icon
        HBox usernameBox = new HBox(10);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        usernameBox.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.LIGHT_GRAY_BG) + ";" +
            "-fx-background-radius: 30px;" +
            "-fx-padding: 5px 15px;"
        );
        
        ImageView userIcon = new ImageView();
        try {
            Image icon = new Image("file:src/main/resources/Image/user.png");
            userIcon.setImage(icon);
            userIcon.setFitHeight(20);
            userIcon.setFitWidth(20);
        } catch (Exception e) {
            System.out.println("User icon not found");
        }
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.setPrefHeight(40);
        usernameField.setPrefWidth(300);
        usernameField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";"
        );
        
        usernameBox.getChildren().addAll(userIcon, usernameField);

          // Password field with icon
        HBox passwordBox = new HBox(10);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.LIGHT_GRAY_BG) + ";" +
            "-fx-background-radius: 30px;" +
            "-fx-padding: 5px 15px;"
        );
        
        ImageView lockIcon = new ImageView();
        try {
            Image icon = new Image("file:src/main/resources/Image/lock.png");
            lockIcon.setImage(icon);
            lockIcon.setFitHeight(20);
            lockIcon.setFitWidth(20);
        } catch (Exception e) {
            System.out.println("Lock icon not found");
        }
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");
        passwordField.setPrefHeight(40);
        passwordField.setPrefWidth(300);
        passwordField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";"
        );
        
        passwordBox.getChildren().addAll(lockIcon, passwordField);
        
        // Confirm Password field with icon
        HBox confirmPasswordBox = new HBox(10);
        confirmPasswordBox.setAlignment(Pos.CENTER_LEFT);
        confirmPasswordBox.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.LIGHT_GRAY_BG) + ";" +
            "-fx-background-radius: 30px;" +
            "-fx-padding: 5px 15px;"
        );
        
        ImageView confirmLockIcon = new ImageView();
        try {
            Image icon = new Image("file:src/main/resources/Image/lock.png");
            confirmLockIcon.setImage(icon);
            confirmLockIcon.setFitHeight(20);
            confirmLockIcon.setFitWidth(20);
        } catch (Exception e) {
            System.out.println("Lock icon not found");
        }
        
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setPrefHeight(40);
        confirmPasswordField.setPrefWidth(300);
        confirmPasswordField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";"
        );
        
        confirmPasswordBox.getChildren().addAll(confirmLockIcon, confirmPasswordField);

        // Signup button with hover effect
        Button signUpButton = new Button("Create Account");
        signUpButton.setPrefHeight(50);
        signUpButton.setPrefWidth(360);
        signUpButton.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 3, 0, 0, 1);"
        );
        
        // Add hover effect
        signUpButton.setOnMouseEntered(e -> 
            signUpButton.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.PRIMARY_HOVER_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 16px;" +
                "-fx-background-radius: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 5, 0, 0, 1);" +
                "-fx-cursor: hand;"
            )
        );
        
        signUpButton.setOnMouseExited(e -> 
            signUpButton.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 16px;" +
                "-fx-background-radius: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 3, 0, 0, 1);"
            )
        );
        
        signUpButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (password.equals(confirmPassword)) {
                try {
                    // Initialize the database helper
                    DatabaseHelper dbHelper = new DatabaseHelper();

                    // Recreate tables to ensure database is initialized
                    dbHelper.recreateTables();  // Ensure table creation before saving the user

                    // Check if the user already exists
                    UserEntity existingUser = dbHelper.getUserByUsername(username);
                    if (existingUser != null) {
                        showAlert("Username already exists. Please choose a different username.");
                    } else {
                        // Create the new user and save it
                        UserEntity newUser = new UserEntity(username, password);
                        dbHelper.saveUser(newUser);
                        System.out.println("User created successfully!");
                        showSuccessAlert("Account created successfully!");

                        // Redirect to LoginPage after successful sign-up
                        primaryStage.setScene(LoginPage.getScene(primaryStage));
                    }
                    dbHelper.close();  // Close the database connection
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Error creating account: " + ex.getMessage());
                }
            } else {
                showAlert("Passwords do not match.");
            }
        });

         // Already have an account? Login link
        HBox loginLinkBox = new HBox();
        loginLinkBox.setAlignment(Pos.CENTER);
        Label alreadyMemberLabel = new Label("Already have an account? ");
        alreadyMemberLabel.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + ";"
        );
        
        Hyperlink loginLink = new Hyperlink("Login here");
        loginLink.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-border-color: transparent;" +
            "-fx-font-weight: bold;"
        );
        
        loginLink.setOnAction(e -> {
            primaryStage.setScene(LoginPage.getScene(primaryStage)); // Navigate to LoginPage
        });
        
        loginLinkBox.getChildren().addAll(alreadyMemberLabel, loginLink);

        // Add all elements to the signup form
        signupForm.getChildren().addAll(
            formTitle,
            tabButtons,
            new Region() {{ setPrefHeight(15); }},
            usernameBox,
            passwordBox,
            confirmPasswordBox,
            new Region() {{ setPrefHeight(10); }},
            signUpButton,
            loginLinkBox
        );
        
        // Add the form to the right panel
        rightPanel.getChildren().add(signupForm);
        
        // Add both panels to the main container
        mainContainer.getChildren().addAll(leftPanel, rightPanel);
        
        return new Scene(mainContainer, 1200, 800);
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private static void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
}