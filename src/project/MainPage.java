package project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainPage extends Application {
    private Stage primaryStage; 
    

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; 

        // Main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        
        VBox leftSide = new VBox();
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setStyle("-fx-background-color: #238BFA; -fx-padding: 50px;");

        Image image = new Image("file:assets/illustration.png"); 
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        Text tagline = new Text("Split expenses effortlessly and keep track of shared costs with ease");
        tagline.setFill(javafx.scene.paint.Color.WHITE);
        tagline.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        tagline.setWrappingWidth(280);

        leftSide.getChildren().addAll(imageView, tagline);

        
        VBox rightSide = new VBox(20);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPadding(new Insets(50));
    }}