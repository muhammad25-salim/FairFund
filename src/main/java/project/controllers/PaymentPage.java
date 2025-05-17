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
    }
}