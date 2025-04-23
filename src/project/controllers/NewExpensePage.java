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
import project.models.*;

public class NewExpensePage {

    public static Scene getScene(Stage primaryStage, FarFundManager farFundManager, String groupId) {
        // UI Elements
        Button backBtn = new Button("«");
        Button saveBtn = new Button("Save");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        TextField totalField = new TextField();
        totalField.setPromptText("Total");
        totalField.setMaxWidth(100);
        // Create a Label for "IQD" and align it to the right of the totalField
        Label iqdLabel = new Label("IQD");
        iqdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        // Use an HBox to position the totalField and iqdLabel side by side
        HBox totalFieldWithLabel = new HBox(5, totalField, iqdLabel);
        totalFieldWithLabel.setAlignment(Pos.CENTER_LEFT);
        // Get users from the selected group
        List<User> groupUsers = farFundManager.getGroup(groupId).getUsers();
        // Dropdown for Payer
        ComboBox<String> paidByDropdown = new ComboBox<>();
        for (User u : groupUsers) {
            paidByDropdown.getItems().add(u.getName());
        }
        if (!groupUsers.isEmpty()) {
            paidByDropdown.setValue(groupUsers.get(0).getName());  // default to first user
        }
        
        HBox totalRow = new HBox(10, totalField, new Label("paid by"), paidByDropdown);
        totalRow.setAlignment(Pos.CENTER_LEFT);

       
        CheckBox user1 = new CheckBox("Mohammad Qadir");
        CheckBox user2 = new CheckBox("Ahmad Hamad");
        CheckBox user3 = new CheckBox("Abdull Aziz Shwan");
        CheckBox user4 = new CheckBox("Mohammad Salim");

        saveBtn.setOnAction(e -> {
            
            String title = titleField.getText();
            double totalAmount = Double.parseDouble(totalField.getText());
            User payer = new User(paidByDropdown.getValue()); 
            List<User> participants = new ArrayList<>();
            if (user1.isSelected()) participants.add(new User(user1.getText()));
            if (user2.isSelected()) participants.add(new User(user2.getText()));
            if (user3.isSelected()) participants.add(new User(user3.getText()));
            if (user4.isSelected()) participants.add(new User(user4.getText()));

            farFundManager.addExpenseToGroup(groupId, title, totalAmount, payer, participants);

            primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId));
        });

        VBox checkboxes = new VBox(5, user1, user2, user3, user4);

        VBox blueBox = new VBox(10, titleField, totalRow, new Label("paid for"), checkboxes);
        blueBox.setPadding(new Insets(20));
        blueBox.setAlignment(Pos.TOP_LEFT);
        blueBox.setStyle("-fx-background-color: #3498db; -fx-background-radius: 20;");

        StackPane stackPane = new StackPane(blueBox);
        StackPane.setMargin(blueBox, new Insets(50, 50, 50, 50));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(new HBox(backBtn, new Region(), saveBtn));
        root.setCenter(stackPane);

        return new Scene(root, 600, 400);
    }
}

