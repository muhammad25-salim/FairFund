package project.controllers;

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

        // Use the existing button styling
        styleButtons(backBtn, saveBtn);

        backBtn.setOnAction(e -> {
            Stage popupStage = (Stage) backBtn.getScene().getWindow();
            popupStage.close();
            ExpensesPage.refreshExpensesPage(primaryStage, FairFundManager, groupId);
        });

        // Button box with spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonsBox = new HBox(10, backBtn, spacer, saveBtn);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER);

        // Root layout with subtle background
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ";");
        root.setPadding(new Insets(20));
        root.setTop(buttonsBox);
        root.setCenter(stackPane);

        return new Scene(root, 700, 600);
    }

    private static Member findMemberByName(List<Member> Members, String name) {
        for (Member u : Members) {
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

        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + 
            ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", " + 
            "derive(" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", -20%));" +
            "-fx-border-radius: 5px;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 10, 0, 0, 3);"
        );
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 14px;");
        
        alert.showAndWait();
    }

    private static void styleButtons(Button backBtn, Button saveBtn) {
        backBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) +";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        );

        saveBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BUTTON_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        );

        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.PRIMARY_HOVER_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;" +
                "-fx-cursor: hand;"
        ));

        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle(
                "-fx-background-color:" + ColorManager.toRgbString(ColorManager.BUTTON_HOVER_COLOR) +";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;" +
                "-fx-cursor: hand;"
        ));

        backBtn.setOnMouseExited(e -> backBtn.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) +";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 6 20 6 20;"
        ));

        saveBtn.setOnMouseExited(e -> saveBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BUTTON_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        ));
    }
}