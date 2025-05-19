package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import project.database.DatabaseHelper;
import project.database.entities.GroupEntity;
import project.database.entities.MemberEntity;
import project.database.entities.UserEntity;
import project.models.FairFundManager;
import project.models.Group;
import project.models.Member;
import project.utils.ColorManager;

import java.sql.SQLException;
import java.util.List;

public class SettingsPage {

    public static Scene getScene(Stage primaryStage, FairFundManager fairFundManager, String groupId) {
        // Force reload of saved theme to ensure we're using the latest color
        ColorManager.loadSavedTheme();
        
        // Get current group
        Group group = fairFundManager.getGroup(groupId);
        if (group == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Group not found");
            return new Scene(new VBox(), 1200, 800);
        }
        
        // Main container with enhanced gradient background using ColorManager colors
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ");");
        mainLayout.setPadding(new Insets(30));
        
        // Header section with enhanced styling
        VBox header = createHeader("Settings", primaryStage, fairFundManager, groupId);
        mainLayout.setTop(header);
        
        // Create tabs for different settings with enhanced styling
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent; -fx-tab-min-height: 45px; -fx-tab-max-height: 45px;");
        
        // Create tabs with improved styling
        Tab generalTab = new Tab("General");
        Tab membersTab = new Tab("Members");
        Tab accountTab = new Tab("Account");
        
        // Enhanced tab style with ColorManager colors
        String tabStyle = "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + "; " +
                          "-fx-padding: 10px 20px; " +
                          "-fx-font-size: 15px; -fx-font-weight: bold; " +
                          "-fx-background-radius: 10px 10px 0 0; " +
                          "-fx-focus-color: transparent; -fx-faint-focus-color: transparent;";
        
        // Bolder accent color for selected tab using getPrimaryColor()
        String selectedTabStyle = tabStyle + "-fx-border-width: 0 0 4 0; -fx-border-color: " + 
                                  ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";";
        
        // Set initial styles
        generalTab.setStyle(selectedTabStyle);
        membersTab.setStyle(tabStyle);
        accountTab.setStyle(tabStyle);
        
        // Create the content for each tab
        ScrollPane generalScrollPane = new ScrollPane();
        generalScrollPane.setFitToWidth(true);
        generalScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        ScrollPane membersScrollPane = new ScrollPane();
        membersScrollPane.setFitToWidth(true);
        membersScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        ScrollPane accountScrollPane = new ScrollPane();
        accountScrollPane.setFitToWidth(true);
        accountScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        // Set content wrappers first
        generalTab.setContent(generalScrollPane);
        membersTab.setContent(membersScrollPane);
        accountTab.setContent(accountScrollPane);
        
        // Add tabs to the tab pane
        tabPane.getTabs().addAll(generalTab, membersTab, accountTab);
        
        // Set up tab selection styling
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (oldTab != null) oldTab.setStyle(tabStyle);
            if (newTab != null) newTab.setStyle(selectedTabStyle);
        });
        
        // General Tab Content - Group Name settings
        VBox generalContent = new VBox(20);
        generalContent.setPadding(new Insets(30, 20, 20, 20));
        generalContent.setAlignment(Pos.TOP_LEFT);
        
        // Group name settings
        Label groupNameLabel = new Label("Group Name");
        groupNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TextField groupNameField = new TextField(group.getGroupName());
        groupNameField.setStyle("-fx-padding: 10px; -fx-font-size: 16px; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-border-color: #cccccc;");
        groupNameField.setMaxWidth(400);
        
        Button saveGroupNameBtn = new Button("Save Group Name");
        styleButton(saveGroupNameBtn, ColorManager.getPrimaryColor());
        
        saveGroupNameBtn.setOnAction(e -> {
            String newName = groupNameField.getText().trim();
            if (!newName.isEmpty()) {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper();
                    GroupEntity groupEntity = dbHelper.getGroupDao().queryForId(groupId);
                    if (groupEntity != null) {
                        groupEntity.setName(newName);
                        dbHelper.saveGroup(groupEntity);
                        group.setGroupName(newName);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Group name updated successfully");
                    }
                    dbHelper.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update group name: " + ex.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Group name cannot be empty");
            }
        });
        
        VBox groupNameSection = createSettingsSection("Group Name", "Update your group's display name", 
            new VBox(15, groupNameLabel, groupNameField, saveGroupNameBtn));
        
        // Add theme selection section
        VBox themeSection = createSettingsSection("Color Theme", "Change the application's primary color theme", 
            createThemeSelectionSection());
        
        generalContent.getChildren().addAll(groupNameSection, themeSection);
        generalScrollPane.setContent(generalContent);
        
        // Members Tab Content - Manage members
        VBox membersContent = new VBox(25);
        membersContent.setPadding(new Insets(30, 20, 20, 20));
        membersContent.setAlignment(Pos.TOP_LEFT);
        
        // Member list section
        VBox memberListSection = createSettingsSection("Group Members", "Manage and edit group members", 
            createMembersTableSection(group, fairFundManager, groupId));
        
        // Add new member section
        VBox addMemberSection = createSettingsSection("Add New Member", "Add a new member to the group", 
            createAddMemberSection(group, fairFundManager, groupId));
        
        membersContent.getChildren().addAll(memberListSection, addMemberSection);
        membersScrollPane.setContent(membersContent);
        
        // Account Tab Content
        VBox accountContent = new VBox(25);
        accountContent.setPadding(new Insets(30, 20, 20, 20));
        accountContent.setAlignment(Pos.TOP_LEFT);
        
        // Account info section
        VBox accountInfoSection = createSettingsSection("Account Information", "View your account details",
            createAccountInfoSection(fairFundManager));
        
        // Password change section
        VBox passwordSection = createSettingsSection("Change Password", "Update your account password", 
            createPasswordChangeSection(fairFundManager));
        
        accountContent.getChildren().addAll(accountInfoSection, passwordSection);
        accountScrollPane.setContent(accountContent);
        
        // Add spacing around the tab pane and add it to the main layout
        VBox tabPaneContainer = new VBox(tabPane);
        tabPaneContainer.setPadding(new Insets(20, 0, 0, 0));
        tabPaneContainer.setStyle("-fx-background-color: transparent;");
        mainLayout.setCenter(tabPaneContainer);
        
        // Create scene with enhanced styling
        Scene scene = new Scene(mainLayout, 1200, 800);
        return scene;
    }
    
    private static VBox createHeader(String title, Stage primaryStage, FairFundManager fairFundManager, String groupId) {
        // Enhance header with icon or decoration
        Label headerLabel = new Label(title);
        headerLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + 
                             ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
        
        // Create a more appealing back button
        Button backBtn = new Button("← Back to Overview");
        styleButton(backBtn, ColorManager.getPrimaryColor());
        backBtn.setOnAction(e -> primaryStage.setScene(OverviewPage.getScene(primaryStage, fairFundManager, groupId)));
        
        HBox buttonBar = new HBox(10, backBtn);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        
        // Add a subtle divider
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";");
        separator.setPrefHeight(1);
        
        VBox header = new VBox(15, headerLabel, buttonBar, separator);
        header.setPadding(new Insets(0, 0, 25, 0));
        
        return header;
    }
    
    private static VBox createSettingsSection(String title, String description, Node content) {
        Label sectionTitle = new Label(title);
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + 
                             ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
        
        Label sectionDesc = new Label(description);
        sectionDesc.setStyle("-fx-font-size: 15px; -fx-text-fill: " + 
                            ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + ";");
        
        // Create a colored accent bar with getPrimaryColor()
        Rectangle accent = new Rectangle(60, 4);
        accent.setFill(ColorManager.getPrimaryColor());
        accent.setArcWidth(4);
        accent.setArcHeight(4);
        
        VBox titleBox = new VBox(8, sectionTitle, accent, sectionDesc);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox section = new VBox(15, titleBox, content);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + 
                        "; -fx-background-radius: 15px;");
        
        // Add enhanced shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(ColorManager.BLACK_SEMI_TRANSPARENT);
        shadow.setOffsetY(3);
        shadow.setRadius(12);
        shadow.setSpread(0.05);
        section.setEffect(shadow);
        
        return section;
    }
}