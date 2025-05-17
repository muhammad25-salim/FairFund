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

        // Table setup
        table = new TableView<>();  // Initialize the TableView here
        table.setId("expenseTable"); // Set the ID for the TableView
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle("-fx-font-size: 18px; -fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_OPAQUE, 0.9) + 
                       "; -fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + 
                       ", 10, 0, 0, 2); -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-padding: 5px;");

        // Increase row height
        table.setFixedCellSize(50);

        TableColumn<Expense, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setMinWidth(300); // Increased width
        descriptionColumn.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT; -fx-font-weight: bold;");

        TableColumn<Expense, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.format("%.2f IQD", cellData.getValue().totalAmountProperty().get()))
        );
        amountColumn.setMinWidth(200); // Increased width
        amountColumn.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT; -fx-font-weight: bold;");
        
        // Right-align the amount values
        amountColumn.setCellFactory(column -> {
            return new TableCell<Expense, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("-fx-font-size: 18px;");
                    } else {
                        setText(item);
                        setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                        setAlignment(Pos.CENTER_RIGHT);
                    }
                }
            };
        });

        TableColumn<Expense, String> payerColumn = new TableColumn<>("Payer");
        payerColumn.setCellValueFactory(cellData -> {
            Member payer = cellData.getValue().getPayer();
            return new SimpleStringProperty(payer.getName());
        });
        payerColumn.setMinWidth(250); // Increased width
        payerColumn.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT; -fx-font-weight: bold;");

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !table.getSelectionModel().isEmpty()) {
                Expense selectedExpense = table.getSelectionModel().getSelectedItem();

                // Check if the current user is the creator of the expense
                if (!selectedExpense.getCreator().equals(FairFundManager.getCurrentUser().getUsername())) {
                    showAlert("Permission Denied", "Only the creator of the expense can edit it.");
                    return;
                }

                // Create a new Stage for the popup
                Stage popup = new Stage();
                popup.setTitle("Edit Expense");

                // Set the scene of the popup
                popup.setScene(EditExpensePage.getScene(popup, FairFundManager, groupId, selectedExpense));

                // Set the modal behavior
                popup.initOwner(primaryStage); // makes the popup dependent on the main stage
                popup.initModality(Modality.WINDOW_MODAL); // makes it modal
                popup.show();  // Show the popup window
            }
        });

        TableColumn<Expense, Void> deleteColumn = new TableColumn<>("Action");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                // Style the delete button with more luxurious look - matching other buttons
                deleteButton.setStyle(
                    "-fx-background-color: " + ColorManager.toRgbString(ColorManager.ERROR_COLOR) +";" +
                    "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 6px 14px;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-border-radius: 20px;" +
                    "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 4, 0, 0, 1);"
                );
                
                // Hover effects
                deleteButton.setOnMouseEntered(e -> {
                    deleteButton.setStyle(
                        "-fx-background-color: derive(" + ColorManager.toRgbString(ColorManager.ERROR_COLOR) + ", -10%);" +
                        "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 6px 14px;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-border-radius: 20px;" +
                        "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 6, 0, 0, 2);" +
                        "-fx-cursor: hand;"
                    );
                });
                
                deleteButton.setOnMouseExited(e -> {
                    deleteButton.setStyle(
                        "-fx-background-color: " + ColorManager.toRgbString(ColorManager.ERROR_COLOR) +";" +
                        "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) +";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 6px 14px;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-border-radius: 20px;" +
                        "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 4, 0, 0, 1);"
                    );
                });
                
                deleteButton.setOnAction(event -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    if (!expense.getCreator().equals(FairFundManager.getCurrentUser().getUsername())) {
                        showAlert("Permission Denied", "Only the creator of the expense can delete it.");
                        return;
                    }
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

        // Apply luxury striped row styling with gradients - matching OverviewPage
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Expense item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    int index = getIndex();
                    // Luxury alternating colors
                    setStyle(index % 2 == 0 
                        ? "-fx-background-color: linear-gradient(to right, " + 
                          ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                          ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + 
                          "); -fx-border-width: 0 0 1 0; -fx-border-color: " + 
                          ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + ";"
                        : "-fx-background-color: linear-gradient(to right, " + 
                          ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ", " + 
                          ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + 
                          "); -fx-border-width: 0 0 1 0; -fx-border-color: " + 
                          ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + ";");
                } else {
                    setStyle("");
                }

                // More elegant hover effect
                setOnMouseEntered(e -> setStyle("-fx-background-color: linear-gradient(to right, " + 
                                              ColorManager.toRgbString(ColorManager.HOVER_BLUE_START) + ", " + 
                                              ColorManager.toRgbString(ColorManager.HOVER_BLUE_END) + "); -fx-cursor: hand;"));
                setOnMouseExited(e -> {
                    if (!empty) {
                        int index = getIndex();
                        setStyle(index % 2 == 0 
                            ? "-fx-background-color: linear-gradient(to right, " + 
                              ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                              ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + 
                              "); -fx-border-width: 0 0 1 0; -fx-border-color: " + 
                              ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + ";"
                            : "-fx-background-color: linear-gradient(to right, " + 
                              ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ", " + 
                              ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + 
                              "); -fx-border-width: 0 0 1 0; -fx-border-color: " + 
                              ColorManager.toRgbString(ColorManager.LIGHT_BORDER) + ";");
                    } else {
                        setStyle("");
                    }
                });
            }
        });

        // Add a header styling - matching OverviewPage
        table.getStyleClass().add("table-view");

        // Add a title above the table
        Label tableTitle = new Label("Expense Records");
        tableTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";");
        tableTitle.setPadding(new Insets(20, 0, 10, 10));

        // Layout with title - matching OverviewPage
        VBox tableContainer = new VBox(10);
        tableContainer.getChildren().addAll(tableTitle, table);
        tableContainer.setPadding(new Insets(10, 20, 20, 20));
        tableContainer.setStyle("-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_MEDIUM, 0.5) + ";");
        VBox.setVgrow(table, Priority.ALWAYS);

        // Layout - matching OverviewPage
        VBox layout = new VBox(10, topBar, tableContainer);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, " + 
                        ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                        ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ");");

        // Return the scene with the layout
        return new Scene(layout, 1200, 800); // Window size remains the same
    }

    public static void refreshExpensesPage(Stage primaryStage, FairFundManager fairFundManager, String groupId) {
        System.out.println("Refreshing ExpensesPage...");
        
        // Get the updated list of expenses
        ObservableList<Expense> updatedExpenses = FXCollections.observableArrayList(fairFundManager.getGroup(groupId).getExpenses());

        // If table is already initialized, update it directly
        if (table != null) {
            table.setItems(updatedExpenses);  // Update the TableView with the latest data
            table.refresh();  // Force a refresh of the TableView
        } else {
            System.out.println("Error: TableView not initialized.");
        }
    } 
    
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private static MenuItem createMenuItem(String text, String iconPath) {
        MenuItem item = new MenuItem(text);
        
        try {
            // Load icon
            ImageView icon = new ImageView(new javafx.scene.image.Image(iconPath));
            icon.setFitWidth(16);
            icon.setFitHeight(16);
            item.setGraphic(icon);
        } catch (Exception ex) {
            System.out.println("Could not load icon for " + text + ": " + ex.getMessage());
        }
        
        // Style menu item
        item.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 16px; " +
            "-fx-font-weight: normal; " +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.DARK_GRAY) + ";"
        );
        
        return item;
    }
}
