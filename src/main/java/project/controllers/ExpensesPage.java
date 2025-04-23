package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import project.models.*;

public class ExpensesPage {
    public static Scene getScene(Stage primaryStage, FarFundManager farFundManager, String groupId) {
        
        Button overviewBtn = new Button("Overview");
        overviewBtn.setStyle("-fx-background-color: white; -fx-border-color: #00AEEF; -fx-border-radius: 10; -fx-font-weight: bold;");
        overviewBtn.setOnAction(e -> primaryStage.setScene(OverviewPage.getScene(primaryStage, farFundManager, groupId)));
        
        Button expensesBtn = new Button("Expenses");
        expensesBtn.setStyle("-fx-background-color: #00AEEF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #00AEEF; -fx-text-fill: white; -fx-background-radius: 100%; -fx-min-width: 30px; -fx-min-height: 30px;");
        plusBtn.setOnAction(e -> primaryStage.setScene(NewExpensePage.getScene(primaryStage, farFundManager, groupId)));
        
        HBox tabs = new HBox(10, overviewBtn, expensesBtn);
        tabs.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, tabs, spacer, plusBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 20, 10, 20));

        TableView<Expense> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Expense, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty()); 

        TableColumn<Expense, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().totalAmountProperty().get() + " IQD")
        );

        TableColumn<Expense, String> payerColumn = new TableColumn<>("Payer");
        payerColumn.setCellValueFactory(cellData -> {
            User payer = cellData.getValue().getPayer(); 
            return new SimpleStringProperty(payer.getName()); 
        });

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !table.getSelectionModel().isEmpty()) {
                Expense selectedExpense = table.getSelectionModel().getSelectedItem();
                primaryStage.setScene(EditExpensePage.getScene(primaryStage, farFundManager, groupId, selectedExpense));
            }
        });
        

        TableColumn<Expense, Void> deleteColumn = new TableColumn<>("Action");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                // Style the delete button
                deleteButton.setStyle(
                    "-fx-background-color: #e74c3c;" +    // Red background
                    "-fx-text-fill: white;" +               // White text color
                    "-fx-font-size: 12px;" +                // Smaller font size
                    "-fx-font-weight: bold;" +              // Bold font weight
                    "-fx-padding: 3px 10px;" +              // Smaller padding around text
                    "-fx-background-radius: 20px;" +        // Slightly rounded corners
                    "-fx-border-radius: 20px;" +            // Rounded border
                    "-fx-transition: background-color 0.3s ease-in-out;" // Smooth transition
                );

                // Button hover effect with the smaller button size
                deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(
                    "-fx-background-color: #c0392b;" +  // Darker red for hover
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 3px 10px;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-border-radius: 20px;"
                ));

                deleteButton.setOnAction(event -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    farFundManager.removeExpenseFromGroup(groupId, expense); 
                    primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId)); 
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        table.getColumns().addAll(descriptionColumn, amountColumn, payerColumn, deleteColumn);
        table.setItems(FXCollections.observableArrayList(farFundManager.getGroup(groupId).getExpenses()));

        // Apply striped row styling
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Expense item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    int index = getIndex();
                    setStyle(index % 2 == 0 
                        ? "-fx-background-color: white;" 
                        : "-fx-background-color: #E0F7FA;");  // Light blue
                } else {
                    setStyle("");
                }
        
                setOnMouseEntered(e -> setStyle("-fx-background-color: #B2EBF2;"));  // Hover highlight
                setOnMouseExited(e -> {
                    if (!empty) {
                        int index = getIndex();
                        setStyle(index % 2 == 0 
                            ? "-fx-background-color: white;" 
                            : "-fx-background-color: #E0F7FA;");
                    } else {
                        setStyle("");
                    }
                });
            }
        });

        Group group = farFundManager.getGroup(groupId);
        ObservableList<Expense> expenses = FXCollections.observableArrayList(group.getExpenses());
        table.setItems(expenses);

        VBox layout = new VBox(10, topBar, table);
        layout.setPadding(new Insets(10));

        return new Scene(layout, 600, 500);
    }
}




