package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

public class ExpensesPage {
    public static Scene getScene(Stage primaryStage, FarFundManager farFundManager, String groupId) {
        // Buttons
        Button overviewBtn = new Button("Overview");
        overviewBtn.setOnAction(e -> primaryStage.setScene(OverviewPage.getScene(primaryStage, farFundManager, groupId)));

        Button plusBtn = new Button("+");
        plusBtn.setOnAction(e -> primaryStage.setScene(NewExpensePage.getScene(primaryStage, farFundManager, groupId)));

        // Table setup
        TableView<Expense> table = new TableView<>();
        TableColumn<Expense, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty()); // Bind to titleProperty

        TableColumn<Expense, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asString()); // Bind to totalAmountProperty

        TableColumn<Expense, String> payerColumn = new TableColumn<>("Payer");
        payerColumn.setCellValueFactory(cellData -> {
            User payer = cellData.getValue().getPayer(); // Get the payer (User object)
            return new SimpleStringProperty(payer.getName()); // Bind to the payer's name
        });
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !table.getSelectionModel().isEmpty()) {
                Expense selectedExpense = table.getSelectionModel().getSelectedItem();
                primaryStage.setScene(EditExpensePage.getScene(primaryStage, farFundManager, groupId, selectedExpense));
            }
        });
        

        // Add a delete button column
        TableColumn<Expense, Void> deleteColumn = new TableColumn<>("Action");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    farFundManager.removeExpenseFromGroup(groupId, expense); // Remove expense from backend
                    primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId)); // Refresh the page
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        table.getColumns().addAll(descriptionColumn, amountColumn, payerColumn, deleteColumn);

        // Load expenses for the group
        Group group = farFundManager.getGroup(groupId);
        ObservableList<Expense> expenses = FXCollections.observableArrayList(group.getExpenses());
        table.setItems(expenses);

        // Layout
        HBox buttonsBox = new HBox(10, overviewBtn, plusBtn);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(10, buttonsBox, table);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 600, 400);
    }
}




