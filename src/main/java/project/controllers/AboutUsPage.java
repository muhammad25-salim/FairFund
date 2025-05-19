package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import project.models.FairFundManager;
import project.utils.ColorManager;

public class AboutUsPage {

    public static Scene getScene(Stage primaryStage, FairFundManager fairFundManager, String groupId) {
        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(30));

        // Back Button
        Button backButton = new Button("« Back");
        backButton.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color:rgb(22, 92, 196);" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
                "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 20 6 20;"
        ));
        backButton.setOnAction(e -> primaryStage.setScene(OverviewPage.getScene(primaryStage, fairFundManager, groupId)));

         // Top Layout with Back Button
        HBox topLayout = new HBox(backButton);
        topLayout.setAlignment(Pos.TOP_LEFT);
        topLayout.setPadding(new Insets(10));
        
        // Rounded Panel
        VBox contentPanel = new VBox(20);
        contentPanel.setAlignment(Pos.TOP_CENTER);
        contentPanel.setPadding(new Insets(40));
        
        StackPane roundedPanel = new StackPane();
        roundedPanel.setStyle("-fx-background-color:" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + 
                "; -fx-background-radius: 30px; -fx-padding: 30px;");
        roundedPanel.setMaxWidth(900);
        roundedPanel.setMaxHeight(700);
        
        // App Logo/Title
        Text appTitle = new Text("Fair Fund");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        appTitle.setFill(ColorManager.TEXT_COLOR);
        
        // Try to load app icon/logo if available
        ImageView logoView = null;
        try {
            Image logo = new Image("file:src/main/resources/Image/Welcome_image.png");
            logoView = new ImageView(logo);
            logoView.setFitWidth(150);
            logoView.setFitHeight(150);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Logo image not found.");
        }
        
        // Tagline/Slogan
        Text tagline = new Text("Split expenses effortlessly and keep track of shared costs with ease");
        tagline.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        tagline.setFill(ColorManager.TEXT_COLOR);
        tagline.setTextAlignment(TextAlignment.CENTER);
        tagline.setWrappingWidth(600);
        
        // Separator
        Separator separator = new Separator();
        separator.setPrefWidth(600);
        separator.setStyle("-fx-background-color: " + ColorManager.toRgbaString(ColorManager.WHITE_SEMI_TRANSPARENT, 0.2) + ";");
        
        // App Description
        Text descriptionTitle = new Text("About");
        descriptionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        descriptionTitle.setFill(ColorManager.TEXT_COLOR);
        
        Text description = new Text(
            "Fair Fund is an intuitive expense sharing application designed to simplify group finances. " +
            "Whether you're planning a trip with friends, managing household expenses with roommates, " +
            "or organizing a group event, Fair Fund helps you track who paid what and who owes whom.\n\n" +
            
            "With Fair Fund, you can:\n" +
            "• Create and join expense groups\n" +
            "• Add expenses and split them equally or custom amounts\n" +
            "• Record payments between members\n" +
            "• View real-time balances\n" +
            "• Export expense reports\n\n" +
            
            "Fair Fund makes financial collaboration transparent and stress-free, ensuring everyone pays their fair share."
        );
        description.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        description.setFill(ColorManager.TEXT_COLOR);
        description.setTextAlignment(TextAlignment.LEFT);
        description.setWrappingWidth(700);