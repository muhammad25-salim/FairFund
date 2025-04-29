package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.models.*;


public class CreateGroupage {

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager) {
    	
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        StackPane roundedPanel = new StackPane();
        
        roundedPanel.setStyle("-fx-background-color: #238BFA; -fx-background-radius: 20px; -fx-padding: 40px;");
        roundedPanel.setMaxWidth(350);
        roundedPanel.setMaxHeight(250);

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        Text title = new Text("Create a group");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setFill(Color.WHITE);

        TextField GidField = new TextField();
        GidField.setPromptText("Group id");
        GidField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        TextField GnameField = new TextField();
        GnameField.setPromptText("Group name");
        GnameField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        Button createGroupBtn = new Button("Create group");
        createGroupBtn.setStyle("-fx-background-color: white; -fx-text-fill: #238BFA;");
        createGroupBtn.setOnAction(e -> {
            String groupId = GidField.getText().trim();
            String groupName = GnameField.getText().trim();

            if (!groupId.isEmpty() && !groupName.isEmpty()) {
                // Go to AddMembersPage instead of creating group here
                primaryStage.setScene(AddMembersPage.getScene(primaryStage, FairFundManager, groupId, groupName));
            }
        });

        Text joinText = new Text("Join an existing group");
        joinText.setFill(Color.WHITE);
        joinText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        
        joinText.setOnMouseClicked(e -> primaryStage.setScene(JoinGroupPage.getScene(primaryStage, FairFundManager)));

        contentBox.getChildren().addAll(title, GnameField, GidField, createGroupBtn, joinText);
        roundedPanel.getChildren().add(contentBox);
        mainLayout.getChildren().add(roundedPanel);

        return new Scene(mainLayout, 600, 400);
    }
}
