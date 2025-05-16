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
import javafx.stage.Modality;
import javafx.scene.image.ImageView;
import project.models.*;
import project.utils.ColorManager;

public class ExpensesPage {
    private static TableView<Expense> table;  // Declare a static reference to the TableView

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager, String groupId) {
        
        // Tab Buttons 
        Button overviewBtn = new Button("Overview");
        overviewBtn.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + "; -fx-border-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-border-radius: 10; -fx-font-weight: bold; -fx-font-size: 18px;");
        overviewBtn.setOnAction(e -> primaryStage.setScene(OverviewPage.getScene(primaryStage, FairFundManager, groupId)));

        Button expensesBtn = new Button("Expenses");
        expensesBtn.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-size: 18px;");

        // Button Bar
        HBox tabs = new HBox(20, overviewBtn, expensesBtn); // Increased spacing
        tabs.setAlignment(Pos.CENTER);

        // Plus Button
        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-font-size: 24px; -fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-background-radius: 100%; -fx-min-width: 40px; -fx-min-height: 40px;");
        plusBtn.setOnAction(e -> {
            Stage popup = new Stage();
            popup.setTitle("Add Expense");
            popup.setScene(NewExpensePage.getScene(popup, FairFundManager, groupId));
            popup.initOwner(primaryStage);
            popup.initModality(Modality.WINDOW_MODAL);
            popup.show();
        });

        // Add "Expense" label under the plus button
        Label expenseLabel = new Label("Expense");
        expenseLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-font-weight: bold;");

        // Create VBox to stack the button and label vertically
        VBox plusBtnWithLabel = new VBox(5, plusBtn, expenseLabel);
        plusBtnWithLabel.setAlignment(Pos.CENTER);

        final ImageView menuIcon;
        try {
            menuIcon = new ImageView(new javafx.scene.image.Image("file:src/main/resources/Image/Menu.png"));
            menuIcon.setFitWidth(35);
            menuIcon.setFitHeight(35);
            
            // Add hover effect to menu icon
            menuIcon.setStyle("-fx-cursor: hand;");
            menuIcon.setOpacity(0.8);
            menuIcon.setOnMouseEntered(e -> {
                menuIcon.setOpacity(1.0);
                menuIcon.setEffect(new javafx.scene.effect.DropShadow(10, ColorManager.BLACK_SEMI_TRANSPARENT));
            });
            menuIcon.setOnMouseExited(e -> {
                menuIcon.setOpacity(0.8);
                menuIcon.setEffect(null);
            });
        } catch (Exception ex) {
            System.out.println("Error loading menu icon: " + ex.getMessage());
            return new Scene(new VBox(new Label("Failed to load menu icon.")), 600, 400);
        }

        // Create a custom context menu with enhanced styling
        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + "; " +
                      "-fx-background-radius: 15px; " +
                      "-fx-border-radius: 15px; " + 
                      "-fx-border-color: " + ColorManager.toRgbString(ColorManager.BORDER_COLOR) + "; " +
                      "-fx-border-width: 1px; " +
                      "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 15, 0, 0, 5);");

        // Create menu items with enhanced styling and icons
        MenuItem settingsItem = createMenuItem("Settings", "file:src/main/resources/Image/settings.png");
        MenuItem aboutUsItem = createMenuItem("About Us", "file:src/main/resources/Image/info.png");
        MenuItem exportItem = createMenuItem("Export PDF", "file:src/main/resources/Image/pdf.png");
        MenuItem paymentsItem = createMenuItem("Payments", "file:src/main/resources/Image/payment.png");
        MenuItem exitGroupItem = createMenuItem("Exit Group", "file:src/main/resources/Image/exit.png");
        MenuItem logoutItem = createMenuItem("Log Out", "file:src/main/resources/Image/logout.png");

        // Add separator before logout items
        SeparatorMenuItem separator = new SeparatorMenuItem();
        separator.setStyle("-fx-padding: 5px 0;");

        menu.getItems().addAll(settingsItem, aboutUsItem, exportItem, paymentsItem, separator, exitGroupItem, logoutItem);

        menuIcon.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            // Position the menu below the icon with a small offset
            menu.show(menuIcon, event.getScreenX() - 10, event.getScreenY() + 10);
        });

        // Handle menu actions
        settingsItem.setOnAction(e -> primaryStage.setScene(SettingsPage.getScene(primaryStage, FairFundManager, groupId)));
        aboutUsItem.setOnAction(e -> primaryStage.setScene(AboutUsPage.getScene(primaryStage, FairFundManager, groupId)));
        exportItem.setOnAction(e -> {
            Group group = FairFundManager.getGroup(groupId);
            project.utils.PdfExporter.exportGroupReport(group, "group_report_" + groupId + ".pdf");
        });
        paymentsItem.setOnAction(e -> primaryStage.setScene(PaymentsPage.getScene(primaryStage, FairFundManager, groupId)));
        
        // Exit group action - return to main page
        exitGroupItem.setOnAction(e -> {
            MainPage mainPage = new MainPage(FairFundManager);
            mainPage.start(primaryStage);
        });

        // Log out action - return to login page
        logoutItem.setOnAction(e -> {
            primaryStage.setScene(LoginPage.getScene(primaryStage));
        });

        // Top Bar - matching OverviewPage layout
        HBox topBar = new HBox(400, menuIcon, tabs, plusBtnWithLabel); 
        topBar.setPadding(new Insets(30, 30, 20, 30));
        topBar.setAlignment(Pos.CENTER);

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
                primaryStage.setScene(EditExpensePage.getScene(primaryStage, FairFundManager, groupId, selectedExpense));
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
                    FairFundManager.removeExpenseFromGroup(groupId, expense); 
                    primaryStage.setScene(ExpensesPage.getScene(primaryStage, FairFundManager, groupId)); 
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        table.getColumns().addAll(descriptionColumn, amountColumn, payerColumn, deleteColumn);
        table.setItems(FXCollections.observableArrayList(FairFundManager.getGroup(groupId).getExpenses()));

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

        VBox layout = new VBox(10, topBar, table);
        layout.setPadding(new Insets(10));

        return new Scene(layout, 600, 500);
    }
}
