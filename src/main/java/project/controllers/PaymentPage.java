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
    }
}