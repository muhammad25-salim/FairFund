package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.models.*;
import project.utils.ColorManager;

public class CreateGroupPage {

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ");");

        // Back Button (styled )
        Button backButton = new Button("« Back");
        backButton.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 24 8 24;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 4, 0, 0, 1);"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.PRIMARY_HOVER_COLOR) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 24 8 24;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 6, 0, 0, 2);" +
            "-fx-cursor: hand;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 24 8 24;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 4, 0, 0, 1);"
        ));
        backButton.setOnAction(e -> {
            MainPage mainPage = new MainPage(FairFundManager);
            mainPage.start(primaryStage);
        });

         // Top Layout with Back Button
        HBox topLayout = new HBox(backButton);
        topLayout.setAlignment(Pos.TOP_LEFT);
        topLayout.setPadding(new Insets(10, 10, 20, 10));
        
        // Create Group Panel (Center) - Styled panel
        StackPane createGroupPanel = new StackPane();
        createGroupPanel.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" + 
            "-fx-background-radius: 30px;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 15, 0, 0, 5);"
        );
        createGroupPanel.setPrefWidth(650);
        createGroupPanel.setPrefHeight(550);
        
        VBox createGroupContent = new VBox(40);
        createGroupContent.setAlignment(Pos.CENTER);
        createGroupContent.setPadding(new Insets(50));
        
        // Title styled like JoinGroupPage
        Text createGroupTitle = new Text("Create a Group");
        createGroupTitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        createGroupTitle.setFill(ColorManager.TEXT_COLOR);
        
        // Description text
        Text descriptionText = new Text("Enter group details to create a new group");
        descriptionText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        descriptionText.setFill(ColorManager.TEXT_COLOR);
        descriptionText.setOpacity(0.8);
        
        VBox inputBox = new VBox(20);
        inputBox.setMaxWidth(450);
        inputBox.setAlignment(Pos.CENTER);
        
        // Group name field styled input
        TextField GnameField = new TextField();
        GnameField.setPromptText("Group name");
        GnameField.setMinHeight(60);
        GnameField.setPrefWidth(450);
        GnameField.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 20px;" +
            "-fx-padding: 15 15 15 15;" +
            "-fx-background-radius: 10 10 0 0;"
        );
        
        // Group ID field styled input
        TextField GidField = new TextField();
        GidField.setPromptText("Group ID");
        GidField.setMinHeight(60);
        GidField.setPrefWidth(450);
        GidField.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 20px;" +
            "-fx-padding: 15 15 15 15;" +
            "-fx-background-radius: 10 10 0 0;"
        );
        
        // Create group button styled button
        Button createGroupBtn = new Button("Create Group");
        createGroupBtn.setPrefWidth(300);
        createGroupBtn.setPrefHeight(60);
        createGroupBtn.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 30;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 5, 0, 0, 1);"
        );
        
        // Add hover effects like JoinGroupPage
        createGroupBtn.setOnMouseEntered(e -> {
            createGroupBtn.setStyle(
                "-fx-background-color: derive(" + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ", -10%);" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 30;" +
                "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 8, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });
        
        createGroupBtn.setOnMouseExited(e -> {
            createGroupBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 30;" +
                "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 5, 0, 0, 1);"
            );
        });
        
        createGroupBtn.setOnAction(e -> {
            String groupId = GidField.getText().trim();
            String groupName = GnameField.getText().trim();

            if (!groupId.isEmpty() && !groupName.isEmpty()) {
                // Go to AddMembersPage instead of creating group here
                primaryStage.setScene(AddMembersPage.getScene(primaryStage, FairFundManager, groupId, groupName));
            }
        });

        inputBox.getChildren().addAll(GnameField, GidField, createGroupBtn);
        
         // Or separator like JoinGroupPage
        HBox orSeparator = new HBox();
        orSeparator.setAlignment(Pos.CENTER);
        
        Line leftLine = new Line(0, 0, 100, 0);
        leftLine.setStroke(ColorManager.TEXT_COLOR);
        leftLine.setOpacity(0.5);
        
        Text orText = new Text(" OR ");
        orText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        orText.setFill(ColorManager.TEXT_COLOR);
        orText.setOpacity(0.7);
        
        Line rightLine = new Line(0, 0, 100, 0);
        rightLine.setStroke(ColorManager.TEXT_COLOR);
        rightLine.setOpacity(0.5);
        
        orSeparator.getChildren().addAll(leftLine, orText, rightLine);
        
        // Join group button styled like the alternate button in JoinGroupPage
        Button joinGroupBtn = new Button("Join Existing Group");
        joinGroupBtn.setPrefWidth(300);
        joinGroupBtn.setPrefHeight(50);
        joinGroupBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-border-width: 2px;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 18px;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;"
        );
        
        // Add hover effects
        joinGroupBtn.setOnMouseEntered(e -> {
            joinGroupBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_VERY_TRANSPARENT, 0.1) + ";" +
                "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
                "-fx-border-width: 2px;" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-cursor: hand;"
            );
        });
        
        joinGroupBtn.setOnMouseExited(e -> {
            joinGroupBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
                "-fx-border-width: 2px;" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;"
            );
        });
        
        joinGroupBtn.setOnAction(e -> primaryStage.setScene(JoinGroupPage.getScene(primaryStage, FairFundManager)));
        
    }
}