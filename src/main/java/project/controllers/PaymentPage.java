package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.models.*;
import project.utils.ColorManager;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.List;

public class PaymentPage {

    public static Scene getScene(Stage primaryStage, FairFundManager fairFundManager, String groupId) {
        // UI Elements
        Button backBtn = new Button("«");
        Button payBtn = new Button("Pay");
        
        // Title Text - Enhanced to match NewExpensePage
        Text titleText = new Text("New Payment");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleText.setFill(ColorManager.TEXT_COLOR);

        // Enhanced amount field styling to match NewExpensePage
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setPrefHeight(35);
        amountField.setMaxWidth(120);
        amountField.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.15) + ";" +
            "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";" +
            "-fx-text-fill:" + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" + 
            "-fx-border-width: 0 0 1 0;" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbaString(ColorManager.WHITE_OPAQUE, 0.7) + ";"
        );

         // Enhanced label styling to match NewExpensePage
        Label iqdLabel = new Label("IQD");
        iqdLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        HBox amountFieldWithLabel = new HBox(8, amountField, iqdLabel);
        amountFieldWithLabel.setAlignment(Pos.CENTER_LEFT);

        // Enhanced labels for dropdowns to match NewExpensePage
        Label fromLabel = new Label("From");
        fromLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label toLabel = new Label("To");
        toLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Enhanced dropdown styling to match NewExpensePage
        ComboBox<String> fromDropdown = new ComboBox<>();
        fromDropdown.setPrefHeight(35);
        fromDropdown.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-background-radius: 5px;"
        );

        ComboBox<String> toDropdown = new ComboBox<>();
        toDropdown.setPrefHeight(35);
        toDropdown.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-background-radius: 5px;"
        );

        List<Member> groupMembers = fairFundManager.getGroup(groupId).getMembers();
        for (Member member : groupMembers) {
            fromDropdown.getItems().add(member.getName());
            toDropdown.getItems().add(member.getName());
        }
        if (!groupMembers.isEmpty()) {
            fromDropdown.setValue(groupMembers.get(0).getName());
            if (groupMembers.size() > 1) {
                toDropdown.setValue(groupMembers.get(1).getName());
            }
        }

         // Enhanced date label to match NewExpensePage
        Label dateLabel = new Label("Date");
        dateLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Enhanced date picker styling
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefHeight(35);
        datePicker.setStyle(
            "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-prompt-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-background-radius: 5px;"
        );

        // Create organized layout with spacing to match NewExpensePage
        VBox amountBox = new VBox(8, amountFieldWithLabel);
        
        VBox fromBox = new VBox(8, fromLabel, fromDropdown);
        
        VBox toBox = new VBox(8, toLabel, toDropdown);
        
        VBox dateBox = new VBox(8, dateLabel, datePicker);

        // Enhanced layout organization
        VBox fieldsLayout = new VBox(20);
        fieldsLayout.getChildren().addAll(
            titleText,
            amountBox,
            new Separator(), // visual separator to match NewExpensePage
            fromBox,
            toBox,
            new Separator(), // visual separator to match NewExpensePage
            dateBox
        );
        
        fieldsLayout.setPadding(new Insets(30));
        fieldsLayout.setAlignment(Pos.TOP_LEFT);
        
        // Enhanced background with gradient to match NewExpensePage
        fieldsLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + 
            ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", " + 
            "derive(" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", -15%));" + 
            "-fx-background-radius: 20px;"
        );

         // Save Button Logic remains the same but with enhanced alert to match NewExpensePage
        payBtn.setOnAction(e -> {
            String from = fromDropdown.getValue();
            String to = toDropdown.getValue();
            String amountText = amountField.getText();
            LocalDate date = datePicker.getValue();

            if (from == null || to == null || from.equals(to)) {
                showAlert("Error", "Please select valid 'From' and 'To' members.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    showAlert("Invalid Amount", "The payment amount must be greater than zero.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid number for the payment amount.");
                return;
            }

            // Perform calculations and save payment
            fairFundManager.addPaymentToGroup(groupId, amount, from, to, date);

            // Close the popup
            Stage popupStage = (Stage) payBtn.getScene().getWindow();
            popupStage.close();
        });

        // Back Button Logic
        backBtn.setOnAction(e -> {
            Stage popupStage = (Stage) backBtn.getScene().getWindow();
            popupStage.close();
        });

        // Style Buttons to match NewExpensePage
        styleButtons(backBtn, payBtn);

        // Button box with spacer to match NewExpensePage
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonsBox = new HBox(10, backBtn, spacer, payBtn);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER);

        // Use stack pane for consistent layout with NewExpensePage
        StackPane stackPane = new StackPane(fieldsLayout);
        StackPane.setMargin(fieldsLayout, new Insets(20));

        // Root layout with subtle background to match NewExpensePage
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ";");
        root.setPadding(new Insets(20));
        root.setTop(buttonsBox);
        root.setCenter(stackPane);

        return new Scene(root, 700, 600);
    }

    // Updated showAlert method to use ColorManager
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style the alert dialog to match NewExpensePage
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

     // Updated styleButtons method to use ColorManager
    private static void styleButtons(Button backBtn, Button payBtn) {
        backBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) +";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        );

        payBtn.setStyle(
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

        payBtn.setOnMouseEntered(e -> payBtn.setStyle(
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

        payBtn.setOnMouseExited(e -> payBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.BUTTON_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        ));
    }
}