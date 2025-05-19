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
import project.models.FairFundManager;
import project.models.Payment;
import project.utils.ColorManager;

import java.util.List;

public class PaymentsPage {

    public static Scene getScene(Stage primaryStage, FairFundManager fairFundManager, String groupId) {
        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, " + 
                           ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                           ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ");");

        // Back Button
        Button backButton = new Button("« Back");
        backButton.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.PRIMARY_HOVER_COLOR) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;" +
                "-fx-cursor: hand;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        ));
        backButton.setOnAction(e -> primaryStage.setScene(OverviewPage.getScene(primaryStage, fairFundManager, groupId)));

         // Top Layout with Back Button
        HBox topLayout = new HBox(backButton);
        topLayout.setAlignment(Pos.TOP_LEFT);
        topLayout.setPadding(new Insets(10));

        // Rounded Panel
        VBox roundedPanelContent = new VBox(20); // Content inside the rounded panel
        roundedPanelContent.setAlignment(Pos.TOP_CENTER);
        roundedPanelContent.setPadding(new Insets(20));

        StackPane roundedPanel = new StackPane();
        roundedPanel.setStyle(
            "-fx-background-color:" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; " + 
            "-fx-background-radius: 30px; " +
            "-fx-padding: 50px;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 15, 0, 0, 5);"
        );
        roundedPanel.setMaxWidth(800);
        roundedPanel.setMaxHeight(600);

        // Title inside the rounded panel
        Text title = new Text("Payments");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(ColorManager.TEXT_COLOR);

        // Create payment list using VBox instead of ListView for more control
        VBox paymentsListContainer = new VBox(10);
        paymentsListContainer.setStyle("-fx-background-color: transparent;");
        
        ScrollPane scrollPane = new ScrollPane(paymentsListContainer);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: " + 
                ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";");
        scrollPane.setFitToWidth(true);

        // Populate with payments
        List<Payment> payments = fairFundManager.getGroup(groupId).getPayments();
        String currentUser = fairFundManager.getCurrentUser().getUsername();
        
        for (Payment payment : payments) {
            // Create a payment item panel
            VBox paymentItem = new VBox(5);
            paymentItem.setStyle(
                "-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_VERY_TRANSPARENT, 0.1) + "; " +
                "-fx-background-radius: 10px; " +
                "-fx-padding: 10px;"
            );  

            // Payment details
            Label amountLabel = new Label(String.format("Amount: $%.2f", payment.getAmount()));
            amountLabel.setStyle(
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + "; " +
                "-fx-font-weight: bold;"
            );
            
            Label fromToLabel = new Label(String.format("From: %s → To: %s", payment.getFrom(), payment.getTo()));
            fromToLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + ";");
            
            Label dateLabel = new Label("Date: " + payment.getDate().toString());
            dateLabel.setStyle("-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + ";");
            
            HBox bottomRow = new HBox();
            bottomRow.setAlignment(Pos.CENTER_RIGHT);
            
            // Only show delete button if current user is the creator
            if (payment.getCreator() != null && payment.getCreator().equals(currentUser)) {
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle(
                    "-fx-background-color: " + ColorManager.toRgbString(ColorManager.ERROR_COLOR) + ";" +
                    "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-padding: 3 10;"
                );
                
                // Add hover effect
                deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(
                    "-fx-background-color: derive(" + ColorManager.toRgbString(ColorManager.ERROR_COLOR) + ", -10%);" +
                    "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-padding: 3 10;" +
                    "-fx-cursor: hand;"
                ));
                
                deleteButton.setOnMouseExited(e -> deleteButton.setStyle(
                    "-fx-background-color: " + ColorManager.toRgbString(ColorManager.ERROR_COLOR) + ";" +
                    "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-padding: 3 10;"
                ));