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

                        
                      // Add hover effect
                        setOnMouseEntered(e -> {
                            cellBox.setStyle("-fx-background-color: " + 
                                           ColorManager.toRgbString(ColorManager.HOVER_BLUE_START) + 
                                           "; -fx-background-radius: 5px; -fx-padding: 10px;");
                            setCursor(javafx.scene.Cursor.HAND);
                        });
                        
                        setOnMouseExited(e -> {
                            cellBox.setStyle("-fx-background-color: transparent; -fx-padding: 10px;");
                            setCursor(javafx.scene.Cursor.DEFAULT);
                        });
                    }
                    setPrefHeight(60);
                }
            });

            groupsListView.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) { // Double-click
                    String selectedGroupName = groupsListView.getSelectionModel().getSelectedItem();
                    if (selectedGroupName != null) {
                        // Find the corresponding groupId
                        String selectedGroupId = userGroupsMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().equals(selectedGroupName))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null);

                        if (selectedGroupId != null) {
                            if (fairFundManager.loadGroup(selectedGroupId)) {
                                primaryStage.setScene(OverviewPage.getScene(primaryStage, fairFundManager, selectedGroupId));
                            } else {
                                showAlert("Error", "Failed to load the selected group.");
                            }
                        } else {
                            showAlert("Error", "Group ID not found for the selected group.");
                        }
                    }
                }
            });

              // Add a hint text
            Label hintLabel = new Label("Double-click to open a group");
            hintLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + 
                             ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + "; -fx-font-style: italic;");
            
            userGroupsPanel.getChildren().addAll(userGroupsTitle, separator, groupsListView, hintLabel);
        }
        
        // Join Group Panel (Center)
        StackPane joinGroupPanel = new StackPane();
        joinGroupPanel.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" + 
            "-fx-background-radius: 30px;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 15, 0, 0, 5);"
        );
        joinGroupPanel.setPrefWidth(650);
        joinGroupPanel.setPrefHeight(550);

        VBox joinGroupContent = new VBox(40);
        joinGroupContent.setAlignment(Pos.CENTER);
        joinGroupContent.setPadding(new Insets(50));

        Text joinGroupTitle = new Text("Join a Group");
        joinGroupTitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        joinGroupTitle.setFill(ColorManager.TEXT_COLOR);

        // Description text
        Text descriptionText = new Text("Enter a group ID to join an existing group or create a new one");
        descriptionText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        descriptionText.setFill(ColorManager.TEXT_COLOR);
        descriptionText.setOpacity(0.8);

        VBox inputBox = new VBox(15);
        inputBox.setMaxWidth(450);
        inputBox.setAlignment(Pos.CENTER);

        TextField groupIdField = new TextField();
        groupIdField.setPromptText("Enter Group ID");
        groupIdField.setMinHeight(60);
        groupIdField.setPrefWidth(450);
        groupIdField.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 20px;" +
            "-fx-padding: 15 15 15 15;" +
            "-fx-background-radius: 10 10 0 0;"
        );
        
        Button joinGroupBtn = new Button("Join Group");
        joinGroupBtn.setPrefWidth(300);
        joinGroupBtn.setPrefHeight(60);
        joinGroupBtn.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 30;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 5, 0, 0, 1);"
        );

         // Add hover effects
        joinGroupBtn.setOnMouseEntered(e -> {
            joinGroupBtn.setStyle(
                "-fx-background-color: derive(" + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ", -10%);" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 30;" +
                "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 8, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });
        
        joinGroupBtn.setOnMouseExited(e -> {
            joinGroupBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 30;" +
                "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 5, 0, 0, 1);"
            );
        });

        joinGroupBtn.setOnAction(e -> {
            String groupId = groupIdField.getText().trim();

            if (groupId.isEmpty()) {
                showAlert("Error", "Please enter a valid Group ID.");
                return;
            }

            if (fairFundManager.loadGroup(groupId)) {
                primaryStage.setScene(OverviewPage.getScene(primaryStage, fairFundManager, groupId));
            } else {
                showAlert("Error", "Group not found or failed to load.");
            }
        });
        
        inputBox.getChildren().addAll(groupIdField, joinGroupBtn);


        // Or separator
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
        
        Button createGroupBtn = new Button("Create New Group");
        createGroupBtn.setPrefWidth(300);
        createGroupBtn.setPrefHeight(50);
        createGroupBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-border-width: 2px;" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 18px;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;"
        );

          
        // Add hover effects
        createGroupBtn.setOnMouseEntered(e -> {
            createGroupBtn.setStyle(
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
        
        createGroupBtn.setOnMouseExited(e -> {
            createGroupBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
                "-fx-border-width: 2px;" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;"
            );
        });
        
        createGroupBtn.setOnAction(e -> primaryStage.setScene(CreateGroupPage.getScene(primaryStage, fairFundManager)));

        joinGroupContent.getChildren().addAll(joinGroupTitle, descriptionText, inputBox, orSeparator, createGroupBtn);
        joinGroupPanel.getChildren().add(joinGroupContent);
        