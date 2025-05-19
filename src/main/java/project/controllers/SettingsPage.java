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
    
    private static void styleButton(Button button, Color color) {
        // Use the current color directly rather than relying on a possibly outdated reference
        String colorHex = String.format("#%02X%02X%02X", 
            (int)(color.getRed() * 255), 
            (int)(color.getGreen() * 255), 
            (int)(color.getBlue() * 255));
            
        // Enhanced button style
        button.setStyle(
            "-fx-background-color: " + colorHex + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 12px 24px;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
        
        // Enhanced hover effect
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> 
            button.setStyle(
                "-fx-background-color: derive(" + colorHex + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-padding: 12px 24px;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
                "-fx-cursor: hand;"
            )
        );
        
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> 
            button.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 12px 24px;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
            )
        );
    }

    private static void styleButton(ToggleButton toggleButton, Color color) {
        String colorHex = String.format("#%02X%02X%02X", 
            (int)(color.getRed() * 255), 
            (int)(color.getGreen() * 255), 
            (int)(color.getBlue() * 255));
            
        toggleButton.setStyle(
            "-fx-background-color: " + colorHex + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 10px 20px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5px;"
        );
        
        // Add hover effect
        toggleButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> 
            toggleButton.setStyle(
                "-fx-background-color: derive(" + colorHex + ", 30%);" +
                "-fx-text-fill: white;" +
                "-fx-padding: 10px 20px;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;"
            )
        );
        
        toggleButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> 
            toggleButton.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 10px 20px;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;"
            )
        );
    }
    
    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to create members table section
    private static Node createMembersTableSection(Group group, FairFundManager fairFundManager, String groupId) {
        Label memberListLabel = new Label("Group Members");
        memberListLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + 
                               ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
        
        // Create a TableView for members
        TableView<Member> membersTable = new TableView<>();
        membersTable.setEditable(true);
        membersTable.setMinHeight(300);
        membersTable.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + 
                             "; -fx-border-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + 
                             "; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        
        TableColumn<Member, String> nameColumn = new TableColumn<>("Member Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setMinWidth(400);
        nameColumn.setPrefWidth(400);
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-size: 14px; -fx-text-fill: " + 
                           ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
        
        // Make the name column editable
        nameColumn.setCellFactory(column -> {
            TableCell<Member, String> cell = new TableCell<Member, String>() {
                private TextField textField;
                
                @Override
                public void startEdit() {
                    super.startEdit();
                    
                    if (textField == null) {
                        createTextField();
                    }
                    
                    setText(null);
                    setGraphic(textField);
                    textField.selectAll();
                    textField.requestFocus();
                }
                
                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setText(getItem());
                    setGraphic(null);
                }
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (isEditing()) {
                            if (textField != null) {
                                textField.setText(item);
                            }
                            setText(null);
                            setGraphic(textField);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }
                }
                
                private void createTextField() {
                    textField = new TextField(getItem());
                    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                    textField.setOnKeyPressed(event -> {
                        if (event.getCode().getName().equals("Enter")) {
                            commitEdit(textField.getText());
                        } else if (event.getCode().getName().equals("Escape")) {
                            cancelEdit();
                        }
                    });
                }
            };
            
            return cell;
        });
        
        // Handle edits
        nameColumn.setOnEditCommit(event -> {
            String oldName = event.getRowValue().getName();
            String newName = event.getNewValue().trim();
            
            if (newName.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Member name cannot be empty");
                return;
            }
            
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                
                // Find the member entity
                List<MemberEntity> memberEntities = dbHelper.getMembersByGroup(
                    new GroupEntity(groupId, group.getGroupName(), fairFundManager.getCurrentUser().getUsername()));
                
                for (MemberEntity entity : memberEntities) {
                    if (entity.getName().equals(oldName)) {
                        // Update the name in database
                        entity.setName(newName);
                        dbHelper.saveMember(entity);
                        
                        // Update in-memory object
                        event.getRowValue().setName(newName);
                        
                        // Refresh table
                        membersTable.refresh();
                        
                        // Success notification
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Member name updated successfully");
                        break;
                    }
                }
                
                dbHelper.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update member name: " + ex.getMessage());
            }
        });
        
        membersTable.getColumns().add(nameColumn);
        
        // Populate the table with current members
        ObservableList<Member> memberData = FXCollections.observableArrayList(group.getMembers());
        membersTable.setItems(memberData);
        
        return new VBox(15, memberListLabel, membersTable);
    }

    // Helper method to create add member section
    private static Node createAddMemberSection(Group group, FairFundManager fairFundManager, String groupId) {
        Label addMemberLabel = new Label("Add New Member");
        addMemberLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + 
                              ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
        
        TextField newMemberField = new TextField();
        newMemberField.setPromptText("Enter new member name");
        newMemberField.setStyle("-fx-padding: 10px; -fx-font-size: 16px; -fx-background-radius: 5px; " + 
                              "-fx-border-radius: 5px; -fx-border-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + 
                              "; -fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + 
                              "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + ";");
        newMemberField.setMaxWidth(400);
        
        Button addMemberBtn = new Button("Add Member");
        styleButton(addMemberBtn, ColorManager.getPrimaryColor());
        
        addMemberBtn.setOnAction(e -> {
            String newMemberName = newMemberField.getText().trim();
            if (!newMemberName.isEmpty()) {
                // Check if member already exists
                boolean exists = false;
                for (Member m : group.getMembers()) {
                    if (m.getName().equals(newMemberName)) {
                        exists = true;
                        break;
                    }
                }
                
                if (exists) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Member with this name already exists");
                } else {
                    // Add to database
                    try {
                        DatabaseHelper dbHelper = new DatabaseHelper();
                        
                        // Create new member entity
                        GroupEntity groupEntity = new GroupEntity(groupId, group.getGroupName(), fairFundManager.getCurrentUser().getUsername());
                        MemberEntity newMember = new MemberEntity(newMemberName, groupEntity);
                        dbHelper.saveMember(newMember);
                        
                        // Add to in-memory model and refresh UI
                        Member member = new Member(newMemberName);
                        group.getMembers().add(member);
                        
                        // Clear the text field
                        newMemberField.clear();
                        
                        showAlert(Alert.AlertType.INFORMATION, "Success", "New member added successfully");
                        
                        dbHelper.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to add new member: " + ex.getMessage());
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Member name cannot be empty");
            }
        });
        
        HBox addMemberBox = new HBox(10, newMemberField, addMemberBtn);
        addMemberBox.setAlignment(Pos.CENTER_LEFT);
        
        return new VBox(15, addMemberLabel, addMemberBox);
    }

    // Helper method to create account info section
    private static Node createAccountInfoSection(FairFundManager fairFundManager) {
        Label accountInfoLabel = new Label("Account Information");
        accountInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + 
                            ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
    
        // Username display
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + 
                          ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
    
        Label usernameValue = new Label(fairFundManager.getCurrentUser().getUsername());
        usernameValue.setStyle("-fx-font-size: 16px; -fx-text-fill: " + 
                          ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";");
        
        HBox usernameBox = new HBox(10, usernameLabel, usernameValue);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        
        return new VBox(15, accountInfoLabel, usernameBox);
    }

    // Helper method to create password change section
    private static Node createPasswordChangeSection(FairFundManager fairFundManager) {
        Label passwordLabel = new Label("Change Password");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + 
                             ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
        
        // Current password field
        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Current Password");
        currentPasswordField.setStyle("-fx-padding: 10px; -fx-font-size: 16px; -fx-background-radius: 5px; " + 
                                   "-fx-border-radius: 5px; -fx-border-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + 
                                   "; -fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + 
                                   "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + ";");
        currentPasswordField.setMaxWidth(400);
        
        // New password field
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");
        newPasswordField.setStyle("-fx-padding: 10px; -fx-font-size: 16px; -fx-background-radius: 5px; " + 
                               "-fx-border-radius: 5px; -fx-border-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + 
                               "; -fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + 
                               "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + ";");
        newPasswordField.setMaxWidth(400);
        
        // Confirm new password field
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");
        confirmPasswordField.setStyle("-fx-padding: 10px; -fx-font-size: 16px; -fx-background-radius: 5px; " + 
                                  "-fx-border-radius: 5px; -fx-border-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + 
                                  "; -fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + 
                                  "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + ";");
        confirmPasswordField.setMaxWidth(400);
        
        // Save password button
        Button changePasswordBtn = new Button("Update Password");
        styleButton(changePasswordBtn, ColorManager.getPrimaryColor());
        
        changePasswordBtn.setOnAction(e -> {
            String currentPassword = currentPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            // Basic validation
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "All password fields are required");
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                showAlert(Alert.AlertType.WARNING, "Warning", "New passwords don't match");
                return;
            }
            
            if (newPassword.length() < 6) {
                showAlert(Alert.AlertType.WARNING, "Warning", "New password must be at least 6 characters");
                return;
            }
            
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                
                // Get the current user
                UserEntity currentUser = fairFundManager.getCurrentUser();
                
                // Verify current password
                if (!currentUser.getPassword().equals(currentPassword)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Current password is incorrect");
                    return;
                }
                
                // Update password
                UserEntity userToUpdate = dbHelper.getUserByUsername(currentUser.getUsername());
                if (userToUpdate != null) {
                    // Update the password
                    userToUpdate.setPassword(newPassword);
                    
                    // Update the user in the database
                    dbHelper.getUserDao().update(userToUpdate);
                    
                    // Update the current user in memory as well
                    currentUser.setPassword(newPassword);
                    
                    // Reset fields
                    currentPasswordField.clear();
                    newPasswordField.clear();
                    confirmPasswordField.clear();
                    
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "User not found in database");
                }
                
                dbHelper.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password: " + ex.getMessage());
            }
        });
        
        return new VBox(15, passwordLabel, currentPasswordField, newPasswordField, confirmPasswordField, changePasswordBtn);
    }

    // Helper method to create theme selection section
    private static Node createThemeSelectionSection() {
        Label themeLabel = new Label("Select Theme Color");
        themeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + 
                          ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";");
        
        // Create 5 color options - current blue, plus 4 additional colors
        ToggleGroup themeToggleGroup = new ToggleGroup();
        

          // Create the color options
        HBox colorOptions = new HBox(20);
        colorOptions.setAlignment(Pos.CENTER_LEFT);
        colorOptions.setPadding(new Insets(10, 0, 15, 0));
        
        // Blue (Default) - #238BFA
        ToggleButton blueTheme = createColorButton("#238BFA", themeToggleGroup);
        
        // Green - #2ecc71
        ToggleButton greenTheme = createColorButton("#2ecc71", themeToggleGroup);
        
        // Purple - #9b59b6
        ToggleButton purpleTheme = createColorButton("#9b59b6", themeToggleGroup);
        
        // Orange - #e67e22
        ToggleButton orangeTheme = createColorButton("#e67e22", themeToggleGroup);
        
        // Red - #e74c3c
        ToggleButton redTheme = createColorButton("#e74c3c", themeToggleGroup);
        
        colorOptions.getChildren().addAll(blueTheme, greenTheme, purpleTheme, orangeTheme, redTheme);
        
        // Set the currently active theme - use getPrimaryColor() for most up-to-date value
        Color currentPrimary = ColorManager.getPrimaryColor();
        String currentHex = String.format("#%02X%02X%02X", 
            (int)(currentPrimary.getRed() * 255), 
            (int)(currentPrimary.getGreen() * 255), 
            (int)(currentPrimary.getBlue() * 255)).toUpperCase();
        
        // Select the current color
        for (Node node : colorOptions.getChildren()) {
            if (node instanceof ToggleButton) {
                ToggleButton btn = (ToggleButton) node;
                if (btn.getUserData() != null && btn.getUserData().toString().equalsIgnoreCase(currentHex)) {
                    btn.setSelected(true);
                    break;
                }
            }
        }
        
        // If none match, default to blue
        if (themeToggleGroup.getSelectedToggle() == null) {
            blueTheme.setSelected(true);
        }
        
        // Create live preview label
        Label previewLabel = new Label("Preview");
        previewLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: " + 
                            ColorManager.toRgbString(ColorManager.MEDIUM_GRAY) + ";");
        
        // Create a small preview section that shows immediately how the new color looks
        Button previewButton = new Button("Preview Button");
        Rectangle previewAccent = new Rectangle(60, 4);
        previewAccent.setArcWidth(4);
        previewAccent.setArcHeight(4);
        previewAccent.setFill(ColorManager.getPrimaryColor());
        
        // Style the preview button with the current primary color
        styleButton(previewButton, ColorManager.getPrimaryColor());
        
        VBox previewBox = new VBox(10, previewLabel, previewButton, previewAccent);
        previewBox.setPadding(new Insets(10, 0, 10, 0));
        
        // Add live preview when selecting colors
        themeToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                String selectedColorHex = newToggle.getUserData().toString();
                Color selectedColor = Color.web(selectedColorHex);
                
                // Update preview elements
                previewAccent.setFill(selectedColor);
                styleButton(previewButton, selectedColor);
            }
        });
        
        // Apply button for the theme
        Button applyThemeBtn = new Button("Apply Theme");
        styleButton(applyThemeBtn, ColorManager.getPrimaryColor());
        
        applyThemeBtn.setOnAction(e -> {
            ToggleButton selectedBtn = (ToggleButton) themeToggleGroup.getSelectedToggle();
            if (selectedBtn != null && selectedBtn.getUserData() != null) {
                String colorHex = selectedBtn.getUserData().toString();
                Color newColor = Color.web(colorHex);
                
                try {
                    // Get reference to the current scene
                    Scene currentScene = applyThemeBtn.getScene();
                    
                    // Apply changes to the current scene
                    applyThemeChangesImmediately(currentScene, newColor);
                    
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                              "Theme updated successfully! The new theme will be applied to all pages.");
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update theme: " + ex.getMessage());
                }
            }
        });
        
        return new VBox(15, themeLabel, colorOptions, previewBox, applyThemeBtn);
    }


     
    /**
     * Apply theme changes immediately to all relevant UI elements in the current scene
     * and ensure the changes persist across page transitions
     */
    private static void applyThemeChangesImmediately(Scene scene, Color newColor) {
        if (scene == null) return;
        
        try {
            // 1. Update the ColorManager with the new theme
            ColorManager.updatePrimaryTheme(newColor);
            
            // 2. Start with the root node and traverse the scene graph
            updateNodeColors(scene.getRoot(), newColor);
            
            // 3. Store in preferences for persistence across app restarts
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(ColorManager.class);
            prefs.put("primaryThemeColor", ColorManager.toRgbString(newColor));
            prefs.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Some theme changes couldn't be applied: " + e.getMessage());
        }
    }
    
    /**
     * Recursively update all UI elements that use getPrimaryColor() in the scene graph
     */
    private static void updateNodeColors(Node node, Color newColor) {
        if (node == null) return;
        
        // Update buttons
        if (node instanceof Button) {
            styleButton((Button) node, newColor);
        }
        
        // Update toggle buttons - excluding the theme selection buttons which should keep their individual colors
        if (node instanceof ToggleButton && !(node.getProperties().containsKey("themeButton"))) {
            styleButton((ToggleButton) node, newColor);
        }
        
        // Update rectangles that use getPrimaryColor() (like our accent bars)
        if (node instanceof Rectangle) {
            Rectangle rect = (Rectangle) node;
            // Only update rectangles that were using the getPrimaryColor()
            if (rect.getFill() instanceof Color) {
                Color rectColor = (Color) rect.getFill();
                // If this rectangle was using a color similar to getPrimaryColor()
                if (isColorSimilarToThemeColor(rectColor)) {
                    rect.setFill(newColor);
                }
            }
        }
        
        // Update Separators (divider lines)
        if (node instanceof Separator) {
            String style = node.getStyle();
            if (style != null && style.contains("fx-background-color")) {
                // Replace color in style
                node.setStyle(style.replaceAll(
                    "-fx-background-color: rgb\\([0-9]+, [0-9]+, [0-9]+\\);",
                    "-fx-background-color: " + ColorManager.toRgbString(newColor) + ";"
                ));
            }
        }
        
        // Update tab selection styles
        // Handle Tab styling separately if needed
        if (node instanceof TabPane) {
            TabPane tabPane = (TabPane) node;
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                String tabStyle = "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + "; " +
                                  "-fx-padding: 10px 20px; " +
                                  "-fx-font-size: 15px; -fx-font-weight: bold; " +
                                  "-fx-background-radius: 10px 10px 0 0; " +
                                  "-fx-focus-color: transparent; -fx-faint-focus-color: transparent;";
                String selectedTabStyle = tabStyle + "-fx-border-width: 0 0 4 0; -fx-border-color: " + 
                                          ColorManager.toRgbString(newColor) + ";";
                for (Tab tab : tabPane.getTabs()) {
                    if (tab == selectedTab) {
                        tab.setStyle(selectedTabStyle);
                    } else {
                        tab.setStyle(tabStyle);
                    }
                }
            }
        }
        
        // Fix for TabPane - update tabs directly
        if (node instanceof TabPane) {
            TabPane tabPane = (TabPane) node;
            
            // Get the current styles
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                // Create updated styles
                String tabStyle = "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + "; " +
                                  "-fx-padding: 10px 20px; " +
                                  "-fx-font-size: 15px; -fx-font-weight: bold; " +
                                  "-fx-background-radius: 10px 10px 0 0; " +
                                  "-fx-focus-color: transparent; -fx-faint-focus-color: transparent;";
                
                String selectedTabStyle = tabStyle + "-fx-border-width: 0 0 4 0; -fx-border-color: " + 
                                          ColorManager.toRgbString(newColor) + ";";
                
                // Apply to all tabs
                for (Tab tab : tabPane.getTabs()) {
                    if (tab == selectedTab) {
                        tab.setStyle(selectedTabStyle);
                    } else {
                        tab.setStyle(tabStyle);
                    }
                }
            }
        }

        
          
        // Update labels with getPrimaryColor() text
        if (node instanceof Label) {
            Label label = (Label) node;
            String style = label.getStyle();
            if (style != null && style.contains("fx-text-fill") && 
                isStyleUsingPrimaryColor(style)) {
                label.setStyle(style.replaceAll(
                    "-fx-text-fill: rgb\\([0-9]+, [0-9]+, [0-9]+\\);",
                    "-fx-text-fill: " + ColorManager.toRgbString(newColor) + ";"
                ));
            }
        }
        
        // If this is a parent node, recurse through its children
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                updateNodeColors(child, newColor);
            }
        }
    }
    
    
     // Check if a color is similar to the theme color (getPrimaryColor())
    
    private static boolean isColorSimilarToThemeColor(Color color) {
        // This is a simple comparison - adjust as needed
        Color themeColor = ColorManager.getPrimaryColor(); // Use getter
        double threshold = 0.1; // Adjust sensitivity as needed
        
        return Math.abs(color.getRed() - themeColor.getRed()) < threshold &&
               Math.abs(color.getGreen() - themeColor.getGreen()) < threshold &&
               Math.abs(color.getBlue() - themeColor.getBlue()) < threshold;
    }
    
    /**
     * Check if a style string is using the getPrimaryColor()
     */
    private static boolean isStyleUsingPrimaryColor(String style) {
        // Get the RGB values of getPrimaryColor()
        int r = (int)(ColorManager.getPrimaryColor().getRed() * 255);
        int g = (int)(ColorManager.getPrimaryColor().getGreen() * 255);
        int b = (int)(ColorManager.getPrimaryColor().getBlue() * 255);
        
        // Check if style contains these RGB values
        String rgbPattern = String.format("rgb\\(%d, %d, %d\\)", r, g, b);
        return style.matches(".*" + rgbPattern + ".*");
    }
    
    // Helper method to create color selection buttons with a special property to mark them as theme buttons
    private static ToggleButton createColorButton(String colorHex, ToggleGroup group) {
        ToggleButton button = new ToggleButton("");
        button.setUserData(colorHex);
        button.setToggleGroup(group);
        
        // Mark this as a theme button to exclude it from automatic color updates
        button.getProperties().put("themeButton", true);
        
        // Create a circular color preview
        Rectangle colorPreview = new Rectangle(30, 30);
        colorPreview.setArcWidth(30);
        colorPreview.setArcHeight(30);
        colorPreview.setFill(Color.web(colorHex));
        
        // Add a border to highlight the selected state
        colorPreview.setStroke(Color.TRANSPARENT);
        colorPreview.setStrokeWidth(2);
        
        button.setGraphic(colorPreview);
        button.setPadding(new Insets(5));
        button.setStyle("-fx-background-color: white; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-border-color: #cccccc;");
        
        // Add hover effect
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> 
            button.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-border-color: #aaaaaa;")
        );
        
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> 
            button.setStyle("-fx-background-color: white; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-border-color: #cccccc;")
        );
        
        // Change the border color when selected
        button.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                colorPreview.setStroke(Color.BLACK);
                button.setStyle("-fx-background-color: #e6e6e6; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-border-color: #888888;");
            } else {
                colorPreview.setStroke(Color.TRANSPARENT);
                button.setStyle("-fx-background-color: white; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-border-color: #cccccc;");
            }
        });
        
        return button;
    }
}

