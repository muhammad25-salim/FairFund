package project.controllers;

import java.lang.classfile.Label;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.synth.Region;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.models.*;

public class NewExpensePage {

    public static Scene getScene(Stage primaryStage, FarFundManager farFundManager, String groupId) {
        // UI Elements
        Button backBtn = new Button("«");
        Button saveBtn = new Button("Save");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        TextField totalField = new TextField();
        totalField.setPromptText("Total");
        totalField.setMaxWidth(100);
        // Create a Label for "IQD" and align it to the right of the totalField
        Label iqdLabel = new Label("IQD");
        iqdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        // Use an HBox to position the totalField and iqdLabel side by side
        HBox totalFieldWithLabel = new HBox(5, totalField, iqdLabel);
        totalFieldWithLabel.setAlignment(Pos.CENTER_LEFT);
        // Get users from the selected group
        List<User> groupUsers = farFundManager.getGroup(groupId).getUsers();
        // Dropdown for Payer
        ComboBox<String> paidByDropdown = new ComboBox<>();
        for (User u : groupUsers) {
            paidByDropdown.getItems().add(u.getName());
        }
        if (!groupUsers.isEmpty()) {
            paidByDropdown.setValue(groupUsers.get(0).getName());  // default to first user
        }
        
        HBox totalRow = new HBox(10, totalFieldWithLabel, new Label("paid by"), paidByDropdown);
        totalRow.setAlignment(Pos.CENTER_LEFT);
        // Dynamically generate checkboxes for all users
        VBox checkboxes = new VBox(5);
        Map<CheckBox, User> checkboxUserMap = new HashMap<>();
        for (User u : groupUsers) {
            CheckBox cb = new CheckBox(u.getName());
            checkboxUserMap.put(cb, u);
            checkboxes.getChildren().add(cb);
        }

        CheckBox selectAllCheckbox = new CheckBox("Select All");
        selectAllCheckbox.setOnAction(e -> {
            boolean isSelected = selectAllCheckbox.isSelected();
            for (javafx.scene.Node node : checkboxes.getChildren()) {
                if (node instanceof CheckBox cb) {
                    cb.setSelected(isSelected);
                }
            }
        });

        VBox checkboxesContainer = new VBox(10, selectAllCheckbox, checkboxes);
        // Save Button Logic
        saveBtn.setOnAction(e -> {

            String title = titleField.getText();
            if (title.isEmpty()) {
                showAlert("Error", "You should name the Expense!");
                return;
            }
            double totalAmount;
            try {
                totalAmount = Double.parseDouble(totalField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "The total should be a number!");
                return;
            }
            // Find payer
            User payer = findUserByName(groupUsers, paidByDropdown.getValue());
            if (payer == null) {
                showAlert("Error", "Selected payer not found in group!");
                return;
            }
            // Collect selected participants
            List<User> participants = new ArrayList<>();
            for (Map.Entry<CheckBox, User> entry : checkboxUserMap.entrySet()) {
                if (entry.getKey().isSelected()) {
                    participants.add(entry.getValue());
                }
            }

            if (participants.isEmpty()) {
                showAlert("Error", "At least one participant must be selected!");
                return;
            }
            // Add expense to group
            farFundManager.addExpenseToGroup(groupId, title, totalAmount, payer, participants);
            // Navigate back to ExpensesPage
            primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId));
        });

        
        // BlueBox & Layout Styling
        VBox blueBox = new VBox(10, titleField, totalRow, new Label("paid for"), checkboxesContainer);
        blueBox.setPadding(new Insets(20));
        blueBox.setAlignment(Pos.TOP_LEFT);
        blueBox.setStyle("-fx-background-color: #3498db; -fx-background-radius: 20;");

        StackPane stackPane = new StackPane(blueBox);
        StackPane.setMargin(blueBox, new Insets(50, 50, 50, 50));
        // Button Styling
        styleButtons(backBtn, saveBtn);
        backBtn.setOnAction(e -> primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId)));
        // Button Box
        HBox buttonsBox = new HBox();
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        buttonsBox.getChildren().addAll(backBtn, spacer, saveBtn);
        // Root Layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(buttonsBox);
        root.setCenter(stackPane);

        return new Scene(root, 600, 400);
    }

    
    private static User findUserByName(List<User> users, String name) {
        for (User u : users) {
            if (u.getName().equals(name)) {
                return u;
            }
        }
        return null;
    }
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void styleButtons(Button backBtn, Button saveBtn) {
        backBtn.setStyle(
                "-fx-background-color: #6478E9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        );

        saveBtn.setStyle(
                "-fx-background-color: #00AEEF;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        );

        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
                "-fx-background-color: #c0392b;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        ));

        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle(
                "-fx-background-color:rgb(0, 80, 239);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        ));
    }
}

