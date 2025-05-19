package project.controllers;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import project.models.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.CubicCurveTo;
import project.utils.ColorManager;


public class MainPage extends Application {
    private Stage primaryStage;
    private FairFundManager FairFundManager; 

    public MainPage(FairFundManager FairFundManager) {
        this.FairFundManager = FairFundManager;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: "  + ColorManager.toRgbString(ColorManager.BACKGROUND_COLOR) + ";");

        Text logoutText = new Text("Log out");
        logoutText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        logoutText.setFill(Color.BLACK);
        logoutText.setOnMouseClicked(e -> logout());
        logoutText.setOnMouseEntered(e -> logoutText.setUnderline(true));
        logoutText.setOnMouseExited(e -> logoutText.setUnderline(false));
        logoutText.setCursor(javafx.scene.Cursor.HAND);
        
        StackPane logoutPane = new StackPane(logoutText);
        logoutPane.setAlignment(Pos.TOP_LEFT);
        logoutPane.setPadding(new Insets(20, 0, 0, 20));
        root.setTop(logoutPane);

        Path customShape = new Path();
        customShape.getElements().addAll(
            new MoveTo(56.5, -55.9),
            new CubicCurveTo(72.3, -40.6, 83.7, -20.3, 84.5, 0.7),
            new CubicCurveTo(85.2, 21.8, 75.2, 43.5, 59.4, 59.6),
            new CubicCurveTo(43.5, 75.7, 21.8, 86.1, 0.6, 85.5),
            new CubicCurveTo(-20.6, 84.9, -41.2, 73.3, -55.5, 57.3),
            new CubicCurveTo(-69.9, 41.2, -78.1, 20.6, -79.2, -1.2),
            new CubicCurveTo(-80.4, -22.9, -74.6, -45.9, -60.2, -61.1),
            new CubicCurveTo(-45.9, -76.4, -22.9, -84, -1.3, -82.7),
            new CubicCurveTo(20.3, -81.4, 40.6, -71.1, 56.5, -55.9)
        );
        customShape.setFill(Color.web(ColorManager.toRgbString(ColorManager.getPrimaryColor())));
        customShape.setStroke(null); 

        
        customShape.setScaleX(7.0); 
        customShape.setScaleY(7.0);

        customShape.setTranslateX(-30);
        customShape.setTranslateY(-140); 


        StackPane bluePart = new StackPane();
        VBox leftSide = new VBox(20);
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setPadding(new Insets(50));

        ImageView imageView = null;
        try {
            Image image = new Image("file:src/main/resources/Image/Welcome_image.png");
            imageView = new ImageView(image);
            imageView.setFitWidth(430);
            imageView.setPreserveRatio(true);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Unable to load the image. Please check the file path.");
            imageView = new ImageView(); 
        }

        Text tagline = new Text("Split expenses effortlessly and keep\n track of shared costs with ease");
        tagline.setFill(Color.web(ColorManager.toRgbString(ColorManager.TEXT_COLOR)));
        tagline.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        tagline.setWrappingWidth(360);
        tagline.setTextAlignment(TextAlignment.CENTER); 

        leftSide.setPadding(new Insets(20, 20, 0, 60));
        leftSide.getChildren().addAll(imageView, tagline);
        bluePart.getChildren().addAll(customShape, leftSide);
        VBox.setMargin(imageView, new Insets(0, 0, 0, 60));

        VBox rightSide = new VBox(20);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPadding(new Insets(50));

        Text title = new Text("FAIR FUND");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        title.setFill(Color.web("#238BFA"));

       
        VBox.setMargin(title, new Insets(50, 0, 0, 110));

        Text subTitle = new Text("Fair, Fast, Transparent");
        subTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subTitle.setFill(Color.web(ColorManager.toRgbString(ColorManager.getPrimaryColor()))); 

       
        VBox.setMargin(subTitle, new Insets(-10, 0, 140, 250));

        


        Button createGroupBtn = new Button("Create group");
        createGroupBtn.setStyle(
                "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + "; -fx-font-size: 21px; -fx-padding: 10px 20px;");
        createGroupBtn.setPrefWidth(270); 
        createGroupBtn.setOnAction(e -> openCreateGroup()); 

        Button joinGroupBtn = new Button("Join an existing group");
        joinGroupBtn.setStyle(
                "-fx-background-color: transparent; -fx-border-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-text-fill: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + "; -fx-font-size: 19px; -fx-padding: 10px 20px;");
        joinGroupBtn.setPrefWidth(270); 
        joinGroupBtn.setOnAction(e -> openJoinGroup()); 


    
        VBox buttonBox = new VBox(10); 
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT); 
        buttonBox.setPadding(new Insets(160, 30, 40, 0)); 
    
        buttonBox.getChildren().addAll(createGroupBtn, joinGroupBtn);

     
        rightSide.getChildren().addAll(title, subTitle, buttonBox);

      
        root.setLeft(bluePart);
        root.setCenter(rightSide);

        
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("FairFund - Expense Sharing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openCreateGroup() {
        primaryStage.setScene(CreateGroupPage.getScene(primaryStage, FairFundManager));
    }

    private void openJoinGroup() {
        primaryStage.setScene(JoinGroupPage.getScene(primaryStage, FairFundManager));
    }

    private void logout() {
        // Implement the logout functionality here
        System.out.println("Logged out");
    }
}

