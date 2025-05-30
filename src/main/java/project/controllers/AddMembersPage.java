package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import project.models.*;
import project.utils.ColorManager;

public class AddMembersPage {

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager, String groupId, String groupName) {
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        StackPane roundedPanel = new StackPane();
        roundedPanel.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-background-radius: 20px; -fx-padding: 40px;");
        roundedPanel.setMaxWidth(500);// Increased width
        roundedPanel.setMaxHeight(400); // Increased height

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);

        Text title = new Text("Add Members");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.web(ColorManager.toRgbString(ColorManager.TEXT_COLOR)));

        VBox inputList = new VBox(8);
        inputList.setAlignment(Pos.CENTER);

        List<TextField> memberInputs = new ArrayList<>();

        // Create the first input field
        addNewMemberInput(inputList, memberInputs);

        Text instructions = new Text("Press 'Enter' to add new members");
        instructions.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        instructions.setFill(Color.web(ColorManager.toRgbString(ColorManager.TEXT_COLOR)));

        Button saveButton = new Button("Save Group");
        saveButton.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) +"; -fx-text-fill:" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-font-weight: bold; -fx-font-size: 16px;");
        saveButton.setOnAction(e -> {
            List<Member> members = new ArrayList<>();
            for (TextField field : memberInputs) {
                String name = field.getText().trim();
                if (!name.isEmpty()) {
                    members.add(new Member(name));
                }
            }

            if (!members.isEmpty()) {
                FairFundManager.createGroup(groupId, groupName, members);
                primaryStage.setScene(OverviewPage.getScene(primaryStage, FairFundManager, groupId));
            }
        });

        contentBox.getChildren().addAll(title, inputList, instructions, saveButton);
        roundedPanel.getChildren().add(contentBox);
        mainLayout.getChildren().add(roundedPanel);

        return new Scene(mainLayout, 1200, 800);
    }

    private static void addNewMemberInput(VBox inputList, List<TextField> memberInputs) {
        TextField memberField = new TextField();
        memberField.setPromptText("Member name");
        memberField.setStyle("-fx-background-color: transparent; -fx-border-color:" + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) +"; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +"; -fx-border-width: 0 0 2 0; -fx-font-size: 16px;");
        memberField.setFont(Font.font(14));

        memberField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!memberField.getText().trim().isEmpty()) {
                    addNewMemberInput(inputList, memberInputs);
                }
            }
        });

        memberInputs.add(memberField);
        inputList.getChildren().add(memberField);
        memberField.requestFocus();
    }
}
