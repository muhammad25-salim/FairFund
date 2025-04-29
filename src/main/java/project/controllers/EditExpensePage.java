package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.models.*;


public class EditExpensePage {

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager, String groupId, Expense expense) {
        Group group = FairFundManager.getGroup(groupId);
        
        // Back Button 
        Button backBtn = new Button("«");
        backBtn.setOnAction(e -> primaryStage.setScene(ExpensesPage.getScene(primaryStage, FairFundManager, groupId)));

        // Save Button 
        Button saveBtn = new Button("Save");
        

        // Title Field 
        TextField titleField = new TextField(expense.getTitle());
        titleField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        // Total Field 
        TextField totalField = new TextField(String.valueOf(expense.getTotalAmount()));
        totalField.setPromptText("Total");
        totalField.setMaxWidth(100);

        Label iqdLabel = new Label("IQD");
        iqdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        HBox totalFieldWithLabel = new HBox(5, totalField, iqdLabel);
        totalFieldWithLabel.setAlignment(Pos.CENTER_LEFT);

        // Paid By Dropdown 
        ComboBox<String> paidByDropdown = new ComboBox<>();
        for (User user : group.getUsers()) {
            paidByDropdown.getItems().add(user.getName());
        }
        if (expense.getPayer() != null) {
            paidByDropdown.setValue(expense.getPayer().getName());
        }
        // Horizontal Layout for Total and Paid By
        HBox totalRow = new HBox(10, totalFieldWithLabel, new Label("paid by"), paidByDropdown);
        totalRow.setAlignment(Pos.CENTER_LEFT);

        VBox checkboxes = new VBox(5);
        for (User user : group.getUsers()) {
            CheckBox checkBox = new CheckBox(user.getName());
            if (expense.getParticipants().contains(user)) {
                checkBox.setSelected(true);
            }
            checkboxes.getChildren().add(checkBox);
        }

        CheckBox selectAllCheckbox = new CheckBox("Select All");
        selectAllCheckbox.setOnAction(e -> {
            boolean isSelected = selectAllCheckbox.isSelected();
            for (javafx.scene.Node node : checkboxes.getChildren()) {
                if (node instanceof CheckBox cb) {
                    cb.setSelected(isSelected);
                }
            }
        });

        VBox checkboxesContainer = new VBox(10, selectAllCheckbox, checkboxes);

        saveBtn.setOnAction(e -> {
            try {
                if (titleField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("You should name the Expense!");
                    alert.showAndWait();
                    return;
                }

                double newAmount;
                try {
                    newAmount = Double.parseDouble(totalField.getText());
                    if (newAmount <= 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(primaryStage);
                        alert.setTitle("Invalid Amount");
                        alert.setHeaderText(null);
                        alert.setContentText("Amount must be positive");
                        alert.showAndWait();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("The total should be a number!");
                    alert.showAndWait();
                    return;
                }

                User newPayer = group.getUserByName(paidByDropdown.getValue());
                if (newPayer == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Selected payer not found in group");
                    alert.showAndWait();
                    return;
                }

                // Update the expense object with new values
                expense.setTitle(titleField.getText());
                expense.setTotalAmount(newAmount);
                expense.setPayer(newPayer);

                // Update participants list
                expense.getParticipants().clear();
                for (javafx.scene.Node node : checkboxes.getChildren()) {
                    if (node instanceof CheckBox cb && cb.isSelected()) {
                        User participant = group.getUserByName(cb.getText());
                        if (participant != null) {
                            expense.getParticipants().add(participant);
                        }
                    }
                }

                if (expense.getParticipants().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("At least one participant must be selected!");
                    alert.showAndWait();
                    return;
                }

                // call the method to update the expense in memory and database
                System.out.println("Calling updateExpenseInGroup...");
                FairFundManager.updateExpenseInGroup(groupId, expense, expense); // Update method call
        
                // Go back to the Expenses page
                primaryStage.setScene(ExpensesPage.getScene(primaryStage, FairFundManager, groupId));

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(primaryStage);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        // Blue Rounded Box
        VBox blueBox = new VBox(10, titleField, totalRow, new Label("paid for"), checkboxesContainer);
        blueBox.setPadding(new Insets(20));
        blueBox.setAlignment(Pos.TOP_LEFT);
        blueBox.setStyle("-fx-background-color: #3498db; -fx-background-radius: 20;");

        StackPane stackPane = new StackPane(blueBox);
        StackPane.setMargin(blueBox, new Insets(50));

        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 6 20 6 20;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 6 20 6 20;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 6 20 6 20;"));

        saveBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 6 20 6 20;");
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 6 20 6 20;"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 6 20 6 20;"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonsBox = new HBox(10, backBtn, spacer, saveBtn);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(buttonsBox);
        root.setCenter(stackPane);

        return new Scene(root, 600, 400);
    }
}
