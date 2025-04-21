package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class OverviewPage {
    public static Scene getScene(Stage primaryStage, FarFundManager farFundManager, String groupId) {
       
        Button expensesBtn = new Button("Expenses");
        expensesBtn.setOnAction(e -> primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId)));

        Button plusBtn = new Button("+");
        plusBtn.setOnAction(e -> primaryStage.setScene(NewExpensePage.getScene(primaryStage, farFundManager, groupId)));

       
        TableView<User> table = new TableView<>();
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<User, String> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asString());

        table.getColumns().addAll(nameColumn, balanceColumn);

       
        Group group = farFundManager.getGroup(groupId);
        ObservableList<User> users = FXCollections.observableArrayList(group.getUsers());
        table.setItems(users);

        
        HBox buttonsBox = new HBox(10, expensesBtn, plusBtn);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox layout = new VBox(10, buttonsBox, table);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 600, 400);
    }
}


