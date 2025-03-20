package project;

import org.w3c.dom.Text;

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


         Text title = new Text("FAIR FUND");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(javafx.scene.paint.Color.BLACK);

        Text subTitle = new Text("Fair, Fast, Transparent");
        subTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Button createGroupBtn = new Button("Create group");
        createGroupBtn.setStyle("-fx-background-color: #238BFA; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        createGroupBtn.setOnAction(e -> openCreateGroup());

        Button joinGroupBtn = new Button("Join an existing group");
        joinGroupBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #238BFA; -fx-text-fill: #238BFA; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        joinGroupBtn.setOnAction(e -> openJoinGroup());

        rightSide.getChildren().addAll(title, subTitle, createGroupBtn, joinGroupBtn);

       
        root.setLeft(leftSide);
        root.setCenter(rightSide);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("FairFund - Expense Sharing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openCreateGroup() {
        primaryStage.setScene(CreateGroupage.getScene(primaryStage, farFundManager));
    }

    private void openJoinGroup() {
        primaryStage.setScene(JoinGroupPage.getScene(primaryStage, farFundManager));
    }

    public static void launchGUI(FarFundManager farFundManager) {
        MainPage mainPage = new MainPage(farFundManager);
        mainPage.start(new Stage());
    }}