package project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class EditExpensePage {
    public static Scene getScene(Stage primaryStage, FarFundManager farFundManager, String groupId, Expense expense) {
        // Back Button 
        Button backBtn = new Button("«");
        backBtn.setOnAction(e -> primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId)));

        // Save Button 
        Button saveBtn = new Button("Save");
        

        // Title Field 
        TextField titleField = new TextField(expense.getTitle());
        titleField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        // Total Field 
        TextField totalField = new TextField(String.valueOf(expense.getTotalAmount()));
        totalField.setMaxWidth(100);
        totalField.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 0 0 1 0;");

        // Paid By Dropdown 
        ComboBox<String> paidByDropdown = new ComboBox<>();
        paidByDropdown.getItems().addAll("Mohammad Salim", "Ahmad Hamad", "Abdull Aziz Shwan", "Mohammad Qadir");
        paidByDropdown.setValue(expense.getPayer().getName());

        // Horizontal Layout for Total and Paid By
        HBox totalRow = new HBox(10, totalField, new Label("paid by"), paidByDropdown);
        totalRow.setAlignment(Pos.CENTER_LEFT);

        // Checkboxes (Preselected)
        CheckBox user1 = new CheckBox("Mohammad Qadir");
        user1.setSelected(expense.getParticipants().contains(new User("Mohammad Qadir")));
        CheckBox user2 = new CheckBox("Ahmad Hamad");
        user2.setSelected(expense.getParticipants().contains(new User("Ahmad Hamad")));
        CheckBox user3 = new CheckBox("Abdull Aziz Shwan");
        user3.setSelected(expense.getParticipants().contains(new User("Abdull Aziz Shwan")));
        CheckBox user4 = new CheckBox("Mohammad Salim");
        user4.setSelected(expense.getParticipants().contains(new User("Mohammad Salim")));

        saveBtn.setOnAction(e -> {
            // Update the expense with new values
            expense.setTitle(titleField.getText());
            expense.setTotalAmount(Double.parseDouble(totalField.getText()));
            expense.getPayer().setName(paidByDropdown.getValue());

            
            if (user1.isSelected()) expense.getParticipants().add(new User(user1.getText()));
            if (user2.isSelected()) expense.getParticipants().add(new User(user2.getText()));
            if (user3.isSelected()) expense.getParticipants().add(new User(user3.getText()));
            if (user4.isSelected()) expense.getParticipants().add(new User(user4.getText()));

            expense.calculateBalances();

            primaryStage.setScene(ExpensesPage.getScene(primaryStage, farFundManager, groupId));
        });

        VBox checkboxes = new VBox(5, user1, user2, user3, user4);

        // Blue Rounded Box
        VBox blueBox = new VBox(10, titleField, totalRow, new Label("paid for"), checkboxes);
        blueBox.setPadding(new Insets(20));
        blueBox.setAlignment(Pos.TOP_LEFT);
        blueBox.setStyle("-fx-background-color: #3498db; -fx-background-radius: 20;");

        StackPane stackPane = new StackPane(blueBox);
        StackPane.setMargin(blueBox, new Insets(50, 50, 50, 50));

        HBox buttonsBox = new HBox(10, backBtn, saveBtn);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(buttonsBox);
        root.setCenter(stackPane);

        return new Scene(root, 600, 400);
    }
}
