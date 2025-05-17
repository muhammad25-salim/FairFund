package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import project.models.*;
import project.utils.ColorManager;

public class OverviewPage {

    public static Scene getScene(Stage primaryStage, FairFundManager fairFundManager, String groupId) {

        // Tab Buttons
        Button overviewBtn = new Button("Overview");
        overviewBtn.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-weight: bold; -fx-font-size: 18px; -fx-background-radius: 10;");

        Button expensesBtn = new Button("Expenses");
        expensesBtn.setStyle("-fx-background-color: " + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + "; -fx-border-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-border-radius: 10; -fx-font-weight: bold; -fx-font-size: 18px;");
        expensesBtn.setOnAction(e -> primaryStage.setScene(ExpensesPage.getScene(primaryStage, fairFundManager, groupId)));

        // Button Bar
        HBox tabs = new HBox(20, overviewBtn, expensesBtn); // Increased spacing
        tabs.setAlignment(Pos.CENTER);

        // Plus Button
        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-font-size: 24px; -fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-background-radius: 100%; -fx-min-width: 40px; -fx-min-height: 40px;");
        plusBtn.setOnAction(e -> {
            Stage popup = new Stage();
            popup.setTitle("Add Payment");
            popup.setScene(PaymentPage.getScene(popup, fairFundManager, groupId));
            popup.initOwner(primaryStage);
            popup.initModality(Modality.WINDOW_MODAL);
            popup.show();
        });

        // Add "Payment" label under the plus button
        Label paymentLabel = new Label("Payment");
        paymentLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-font-weight: bold;");

        // Create VBox to stack the button and label vertically
        VBox plusBtnWithLabel = new VBox(5, plusBtn, paymentLabel);
        plusBtnWithLabel.setAlignment(Pos.CENTER);

        // Menu icon and options (with Export moved here)
        final ImageView menuIcon;
        try {
            menuIcon = new ImageView(new Image("file:src/main/resources/Image/Menu.png"));
            menuIcon.setFitWidth(35);
            menuIcon.setFitHeight(35);
            
            // Add hover effect to menu icon using ColorManager
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

        menuIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // Position the menu below the icon with a small offset
            menu.show(menuIcon, event.getScreenX() - 10, event.getScreenY() + 10);
        });

        // Handle menu actions
        settingsItem.setOnAction(e -> primaryStage.setScene(SettingsPage.getScene(primaryStage, fairFundManager, groupId)));
        aboutUsItem.setOnAction(e -> primaryStage.setScene(AboutUsPage.getScene(primaryStage, fairFundManager, groupId)));
        exportItem.setOnAction(e -> {
            Group group = fairFundManager.getGroup(groupId);
            project.utils.PdfExporter.exportGroupReport(group, "group_report_" + groupId + ".pdf");
        });
        paymentsItem.setOnAction(e -> primaryStage.setScene(PaymentsPage.getScene(primaryStage, fairFundManager, groupId)));
        
        // Exit group action - return to main page
        exitGroupItem.setOnAction(e -> {
            MainPage mainPage = new MainPage(fairFundManager);
            mainPage.start(primaryStage);
        });

        // Log out action - return to login page
        logoutItem.setOnAction(e -> {
            primaryStage.setScene(LoginPage.getScene(primaryStage));
        });

        // Top Bar - Replace plusBtn with plusBtnWithLabel
        HBox topBar = new HBox(400, menuIcon, tabs, plusBtnWithLabel); 
        topBar.setPadding(new Insets(30, 30, 20, 30));
        topBar.setAlignment(Pos.CENTER);

        // Table setup with ColorManager
        TableView<Member> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle("-fx-font-size: 18px; -fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_OPAQUE, 0.9) + 
                       "; -fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + 
                       ", 10, 0, 0, 2); -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-padding: 5px;");

        // Increase row height
        table.setFixedCellSize(50); 

        TableColumn<Member, String> nameColumn = new TableColumn<>("Member Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setMinWidth(300); // Increased width
        nameColumn.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT; -fx-font-weight: bold;");

<<<<<<< HEAD
        TableColumn<User, String> balanceColumn = new TableColumn<>("Balance (IQD)");
=======
        TableColumn<Member, String> balanceColumn = new TableColumn<>("Balance (IQD)");
>>>>>>> 1abfdcc2df29435cb06cc327f245fbdcaf0a1ae9
        balanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asString("%.2f"));
        balanceColumn.setMinWidth(200); // Increased width
        balanceColumn.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");


        
        // Add custom cell factory for balance column to style positive/negative values
        balanceColumn.setCellFactory(column -> {
            return new TableCell<Member, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("-fx-font-size: 18px;");
                    } else {
                        setText(item);
                        // Style based on positive/negative values using ColorManager
                        double value = Double.parseDouble(item);
                        if (value > 0) {
                            setStyle("-fx-font-size: 18px; -fx-text-fill: " + 
                                    ColorManager.toRgbString(ColorManager.SUCCESS_COLOR) + 
                                    "; -fx-font-weight: bold;");
                        } else if (value < 0) {
                            setStyle("-fx-font-size: 18px; -fx-text-fill: " + 
                                    ColorManager.toRgbString(ColorManager.ERROR_COLOR) + 
                                    "; -fx-font-weight: bold;");
                        } else {
                            setStyle("-fx-font-size: 18px; -fx-text-fill: " + 
                                    ColorManager.toRgbString(ColorManager.TEXT_COLOR2) + 
                                    "; -fx-font-weight: bold;");
                        }
                        setAlignment(Pos.CENTER_RIGHT);
                    }
                }
            };
        });

        // Enhanced zebra striping + hover effect with ColorManager
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Member item, boolean empty) {
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