package project.controllers;

import java.lang.reflect.Member;
import java.util.HashMap;
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



public class EditExpensePage {

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager, String groupId, Expense expense) {
        Group group = FairFundManager.getGroup(groupId);
        
        // UI Elements with matching style
        Button backBtn = new Button("«");
        Button saveBtn = new Button("Save");
        
        // Title Text - Enhanced to match NewExpensePage
        Text titleText = new Text("Edit Expense");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleText.setFill(ColorManager.TEXT_COLOR);

        // Enhanced input field to match NewExpensePage
        TextField titleField = new TextField(expense.getTitle());
        titleField.setPrefHeight(35);
        titleField.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.15) + ";" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-text-fill:" + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" + 
            "-fx-border-width: 0 0 1 0;" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbaString(ColorManager.WHITE_OPAQUE, 0.7) + ";"
        );

        // Enhanced amount field to match NewExpensePage
        TextField totalField = new TextField(String.valueOf(expense.getTotalAmount()));
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

        // Enhanced label styling to match NewExpensePage
        Label iqdLabel = new Label("IQD");
        iqdLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Use an HBox to position the totalField and iqdLabel side by side
        HBox totalFieldWithLabel = new HBox(5, totalField, iqdLabel);
        totalFieldWithLabel.setAlignment(Pos.CENTER_LEFT);

        // Enhanced dropdown styling to match NewExpensePage
        ComboBox<String> paidByDropdown = new ComboBox<>();
        paidByDropdown.setPrefHeight(35);
        paidByDropdown.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-background-radius: 5px;"
        );

        for (Member member : group.getMembers()) {
            paidByDropdown.getItems().add(member.getName());
        }
        if (expense.getPayer() != null) {
            paidByDropdown.setValue(expense.getPayer().getName());
        }

        // Enhanced label
        Label paidByLabel = new Label("paid by");
        paidByLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px;");

        // Replace totalField with totalFieldWithLabel in the totalRow
        HBox totalRow = new HBox(15, totalFieldWithLabel, paidByLabel, paidByDropdown);
        totalRow.setAlignment(Pos.CENTER_LEFT);

        // Enhanced section label to match NewExpensePage
        Label paidForLabel = new Label("paid for");
        paidForLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Enhanced checkboxes to match NewExpensePage
        VBox checkboxes = new VBox(8);
        Map<CheckBox, Member> checkboxMemberMap = new HashMap<>();
        for (Member member : group.getMembers()) {
            CheckBox cb = new CheckBox(member.getName());
            cb.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 14px;");
            if (expense.getParticipants().contains(member)) {
                cb.setSelected(true);
            }
            checkboxMemberMap.put(cb, member);
            checkboxes.getChildren().add(cb);
        }

        // Enhanced "Select All" checkbox to match NewExpensePage
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

        // Enhanced checkbox container with subtle border to match NewExpensePage
        VBox checkboxesContainer = new VBox(12, selectAllCheckbox, checkboxes);
        checkboxesContainer.setStyle(
            "-fx-border-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 8px;" +
            "-fx-padding: 10px;"
        );

        saveBtn.setOnAction(e -> {
            try {
                // Validate the inputs first
                if (titleField.getText().isEmpty()) {
                    showAlert("Error", "You should name the Expense!");
                    return;
                }
        
                double newAmount;
                try {
                    newAmount = Double.parseDouble(totalField.getText());
                    if (newAmount <= 0) {
                        showAlert("Invalid Amount", "Amount must be positive");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "The total should be a number!");
                    return;
                }
        
                Member newPayer = group.getMemberByName(paidByDropdown.getValue());
                if (newPayer == null) {
                    showAlert("Error", "Selected payer not found in group");
                    return;
                }
        
                // Update the expense object with new values
                expense.setTitle(titleField.getText());
                expense.setTotalAmount(newAmount);
                expense.setPayer(newPayer);
        
                // Update participants list
                expense.getParticipants().clear();
                for (Map.Entry<CheckBox, Member> entry : checkboxMemberMap.entrySet()) {
                    if (entry.getKey().isSelected()) {
                        expense.getParticipants().add(entry.getValue());
                    }
                }
        
                if (expense.getParticipants().isEmpty()) {
                    showAlert("Error", "At least one participant must be selected!");
                    return;
                }
        
                // call the method to update the expense in memory and database
                FairFundManager.updateExpenseInGroup(groupId, expense, expense);
        
                // Close the popup window
                Stage popupStage = (Stage) saveBtn.getScene().getWindow();
                popupStage.close();

                // Refresh the ExpensesPage in the parent window
                ExpensesPage.refreshExpensesPage(primaryStage, FairFundManager, groupId);
        
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

         // Enhanced content box with spacing 
        VBox blueBox = new VBox(20);
        blueBox.getChildren().addAll(
            titleText, 
            titleField, 
            new Separator(), // visual separator  
            totalRow, 
            new Separator(), // visual separator 
            paidForLabel, 
            checkboxesContainer
        );
        blueBox.setPadding(new Insets(30));
        blueBox.setAlignment(Pos.TOP_LEFT);
        
        // Enhanced background with gradient 
        blueBox.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + 
            ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", " + 
            "derive(" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", -15%));" + 
            "-fx-background-radius: 20px;"
        );

        StackPane stackPane = new StackPane(blueBox);
        StackPane.setMargin(blueBox, new Insets(20));

        // Button styling to match NewExpensePage
        styleButtons(backBtn, saveBtn);

        backBtn.setOnAction(e -> {
            // Close the modal
            Stage popupStage = (Stage) backBtn.getScene().getWindow();
            popupStage.close();
            
            // After going back, refresh the ExpensesPage in the main window
            ExpensesPage.refreshExpensesPage(primaryStage, FairFundManager, groupId);
        });

        // Button box with spacer to match NewExpensePage
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonsBox = new HBox(10, backBtn, spacer, saveBtn);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER);

        // Root layout with subtle background to match NewExpensePage
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ";");
        root.setPadding(new Insets(20));
        root.setTop(buttonsBox);
        root.setCenter(stackPane);

        return new Scene(root, 700, 600);
    }

    
}
