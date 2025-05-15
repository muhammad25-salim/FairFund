package project.controllers;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.models.FairFundManager;
import project.utils.ColorManager;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class JoinGroupPage {

    public static Scene getScene(Stage primaryStage, FairFundManager fairFundManager) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ");");

        // Back Button with increased size
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
            MainPage mainPage = new MainPage(fairFundManager);
            mainPage.start(primaryStage);
        });

        // Top Layout with Back Button
        HBox topLayout = new HBox(backButton);
        topLayout.setAlignment(Pos.TOP_LEFT);
        topLayout.setPadding(new Insets(10, 10, 20, 10));
        
        // User Groups Panel (Left Side)
        VBox userGroupsPanel = new VBox(20);
        userGroupsPanel.setPadding(new Insets(25));
        userGroupsPanel.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_OPAQUE, 0.9) + ";" + 
            "-fx-background-radius: 20px;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 8, 0, 0, 2);"
        );
        userGroupsPanel.setPrefWidth(300);
        userGroupsPanel.setMaxHeight(600);
        
        Text userGroupsTitle = new Text("Your Groups");
        userGroupsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        userGroupsTitle.setFill(ColorManager.getPrimaryColor());
        
        // Styled separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";");
        separator.setOpacity(0.6);
        
        // Fetch groups created by the current user
        Map<String, String> userGroupsMap = fairFundManager.getGroupsCreatedByCurrentUser();
        List<String> userGroupNames = new ArrayList<>(userGroupsMap.values()); // Extract group names
        
        if (userGroupNames.isEmpty()) {
            Label noGroupsLabel = new Label("You haven't created any groups yet");
            noGroupsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + 
                                  ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + "; -fx-font-style: italic;");
            noGroupsLabel.setWrapText(true);
            userGroupsPanel.getChildren().addAll(userGroupsTitle, separator, noGroupsLabel);
        } else {
            ListView<String> groupsListView = new ListView<>(FXCollections.observableArrayList(userGroupNames));
            groupsListView.setPrefHeight(500);
            groupsListView.setStyle(
                "-fx-background-color: transparent;" + 
                "-fx-border-color: transparent;" +
                "-fx-font-size: 16px;" +
                "-fx-border-radius: 8px;"
            );
            
            // Customize list cells
            groupsListView.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox cellBox = new HBox(10);
                        cellBox.setAlignment(Pos.CENTER_LEFT);
                        
                        Label groupName = new Label(item);
                        groupName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + 
                                         ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
                        
                        cellBox.getChildren().add(groupName);
                        cellBox.setStyle("-fx-padding: 10px;");
                        
                        setText(null);
                        setGraphic(cellBox);