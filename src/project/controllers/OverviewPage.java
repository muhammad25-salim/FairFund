package project;

import javax.swing.GroupLayout.Group;
import javax.swing.plaf.synth.Region;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

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
       
        
        // Tab Buttons
        Button overviewBtn = new Button("Overview");
        overviewBtn.setStyle("-fx-background-color: #00AEEF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        Button expensesBtn = new Button("Expenses");
        expensesBtn.setStyle("-fx-background-color: white; -fx-border-color: #00AEEF; -fx-border-radius: 10; -fx-font-weight: bold;");
        expensesBtn.setOnAction(e -> primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId)));

        // Plus Button
        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #00AEEF; -fx-text-fill: white; -fx-background-radius: 100%; -fx-min-width: 30px; -fx-min-height: 30px;");
        plusBtn.setOnAction(e -> primaryStage.setScene(NewExpensePage.getScene(primaryStage, farFundManager, groupId)));

        // Button Bar
        HBox tabs = new HBox(10, overviewBtn, expensesBtn);
        tabs.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, tabs, spacer, plusBtn);
        topBar.setPadding(new Insets(20, 20, 10, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);

    
       
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


