package project.controllers;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import project.models.*;
import project.utils.ColorManager;

public class NewExpensePage {

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager, String groupId) {

        // UI Elements
        Button backBtn = new Button("«");
        Button saveBtn = new Button("Save");

        // Title Text - Enhanced
        Text titleText = new Text("New Expense");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleText.setFill(ColorManager.TEXT_COLOR);
        
        // Enhanced input field
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setPrefHeight(35);
        titleField.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.15) + ";" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-text-fill:" + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" + 
            "-fx-border-width: 0 0 1 0;" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbaString(ColorManager.WHITE_OPAQUE, 0.7) + ";"
        );

        // Enhanced amount field
        TextField totalField = new TextField();
        totalField.setPromptText("Total");
        totalField.setMaxWidth(120);
        totalField.setPrefHeight(35);
        totalField.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.15) + ";" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-text-fill:" + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" + 
            "-fx-border-width: 0 0 1 0;" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbaString(ColorManager.WHITE_OPAQUE, 0.7) + ";"
        );

        // Enhanced label styling
        Label iqdLabel = new Label("IQD");
        iqdLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        HBox totalFieldWithLabel = new HBox(8, totalField, iqdLabel);
        totalFieldWithLabel.setAlignment(Pos.CENTER_LEFT);

        // Get Members from the selected group
        List<Member> groupMembers = FairFundManager.getGroup(groupId).getMembers();

        // Enhanced dropdown styling
        ComboBox<String> paidByDropdown = new ComboBox<>();
        paidByDropdown.setPrefHeight(35);
        paidByDropdown.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-background-radius: 5px;"
        );
        
        for (Member u : groupMembers) {
            paidByDropdown.getItems().add(u.getName());
        }
        if (!groupMembers.isEmpty()) {
            paidByDropdown.setValue(groupMembers.get(0).getName());
        }

        Label paidByLabel = new Label("paid by");
        paidByLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px;");

        HBox totalRow = new HBox(15, totalFieldWithLabel, paidByLabel, paidByDropdown);
        totalRow.setAlignment(Pos.CENTER_LEFT);

        // Enhanced section label
        Label paidForLabel = new Label("paid for");
        paidForLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Enhanced checkboxes
        VBox checkboxes = new VBox(8);
        Map<CheckBox, Member> checkboxMemberMap = new HashMap<>();
        for (Member u : groupMembers) {
            CheckBox cb = new CheckBox(u.getName());
            cb.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 14px;");
            checkboxMemberMap.put(cb, u);
            checkboxes.getChildren().add(cb);
        }

        CheckBox selectAllCheckbox = new CheckBox("Select All");
        selectAllCheckbox.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 14px; -fx-font-weight: bold;");
        selectAllCheckbox.setOnAction(e -> {
            boolean isSelected = selectAllCheckbox.isSelected();
            for (javafx.scene.Node node : checkboxes.getChildren()) {
                if (node instanceof CheckBox cb) {
                    cb.setSelected(isSelected);
                }
            }
        });

         // Enhanced checkbox container with subtle border
        VBox checkboxesContainer = new VBox(12, selectAllCheckbox, checkboxes);
        checkboxesContainer.setStyle(
            "-fx-border-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 8px;" +
            "-fx-padding: 10px;"
        );

        // Save Button Logic remains the same
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
            Member payer = findMemberByName(groupMembers, paidByDropdown.getValue());
            if (payer == null) {
                showAlert("Error", "Selected payer not found in group!");
                return;
            }

            // Collect selected participants
            List<Member> participants = new ArrayList<>();
            for (Map.Entry<CheckBox, Member> entry : checkboxMemberMap.entrySet()) {
                if (entry.getKey().isSelected()) {
                    participants.add(entry.getValue());
                }
            }

            if (participants.isEmpty()) {
                showAlert("Error", "At least one participant must be selected!");
                return;
            }

            // Add expense to group
            FairFundManager.addExpenseToGroup(groupId, title, totalAmount, payer, participants);

            // Close the popup window
            Stage popupStage = (Stage) saveBtn.getScene().getWindow();
            popupStage.close();

            // Refresh the ExpensesPage in the parent window
            ExpensesPage.refreshExpensesPage(primaryStage, FairFundManager, groupId);
        });
    }
}