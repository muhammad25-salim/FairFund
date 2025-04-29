package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.models.FairFundManager;


public class JoinGroupPage {

    public static Scene getScene(Stage primaryStage, FairFundManager fairFundManager) {
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        StackPane roundedPanel = new StackPane();
        roundedPanel.setStyle("-fx-background-color: #238BFA; -fx-background-radius: 20px; -fx-padding: 40px;");
        roundedPanel.setMaxWidth(350);
        roundedPanel.setMaxHeight(200);

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        Text title = new Text("Join a group");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setFill(Color.WHITE);

        TextField GidField = new TextField();
        GidField.setPromptText("Group ID");
        GidField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        Button joinGroupBtn = new Button("Join Group");
        joinGroupBtn.setStyle("-fx-background-color: white; -fx-text-fill: #238BFA;");

        joinGroupBtn.setOnAction(e -> {
            String groupId = GidField.getText().trim();

            if (groupId.isEmpty()) {
                showAlert("Error", "Please enter a valid Group ID.");
                return;
            }

            if (fairFundManager.loadGroup(groupId)) {
                primaryStage.setScene(OverviewPage.getScene(primaryStage, fairFundManager, groupId));
            } else {
                showAlert("Error", "Group not found or failed to load.");
            }
        });

        Text createGroupText = new Text("Create Group");
        createGroupText.setFill(Color.WHITE);
        createGroupText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        createGroupText.setOnMouseClicked(e -> primaryStage.setScene(CreateGroupage.getScene(primaryStage, fairFundManager)));

        contentBox.getChildren().addAll(title, GidField, joinGroupBtn, createGroupText);
        roundedPanel.getChildren().add(contentBox);
        mainLayout.getChildren().add(roundedPanel);

        return new Scene(mainLayout, 600, 400);
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
