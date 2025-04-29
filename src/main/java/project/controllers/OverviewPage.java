package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.models.*;


public class OverviewPage {
    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager, String groupId) {

        // Tab Buttons
        Button overviewBtn = new Button("Overview");
        overviewBtn.setStyle("-fx-background-color: #00AEEF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        Button expensesBtn = new Button("Expenses");
        expensesBtn.setStyle("-fx-background-color: white; -fx-border-color: #00AEEF; -fx-border-radius: 10; -fx-font-weight: bold;");
        expensesBtn.setOnAction(e -> primaryStage.setScene(ExpensesPage.getScene(primaryStage, FairFundManager, groupId)));

        // Plus Button
        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #00AEEF; -fx-text-fill: white; -fx-background-radius: 100%; -fx-min-width: 30px; -fx-min-height: 30px;");
        plusBtn.setOnAction(e -> primaryStage.setScene(NewExpensePage.getScene(primaryStage, FairFundManager, groupId)));

        // Button Bar
        HBox tabs = new HBox(10, overviewBtn, expensesBtn);
        tabs.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, tabs, spacer, plusBtn);
        topBar.setPadding(new Insets(20, 20, 10, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Table
        TableView<User> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<User, String> nameColumn = new TableColumn<>("Member Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setMinWidth(200);

        TableColumn<User, String> balanceColumn = new TableColumn<>("Balance ($)");
        balanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asString("%.2f"));
        balanceColumn.setMinWidth(150);

        // Zebra striping + hover effect
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    int index = getIndex();
                    setStyle(index % 2 == 0 
                        ? "-fx-background-color: white;" 
                        : "-fx-background-color: #E0F7FA;");  // Light blue shade
                } else {
                    setStyle("");
                }
        
                setOnMouseEntered(e -> setStyle("-fx-background-color: #B2EBF2;"));  // Slightly stronger hover blue
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
        

        table.getColumns().addAll(nameColumn, balanceColumn);

        // Populate with group data
        Group group = FairFundManager.getGroup(groupId);
        ObservableList<User> users = FXCollections.observableArrayList(group.getUsers());
        table.setItems(users);

        table.refresh();       

        // Layout
        VBox layout = new VBox(10, topBar, table);
        layout.setPadding(new Insets(10));

        return new Scene(layout, 600, 500);
    }
}

