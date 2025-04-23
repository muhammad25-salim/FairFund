package project;

import java.nio.file.Path;

import org.w3c.dom.Text;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import project.models.FarFundManager;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainPage extends Application {
    private Stage primaryStage;
    private FarFundManager farFundManager; 

    public MainPage(FarFundManager farFundManager) {
        this.farFundManager = farFundManager;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

       
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

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
        customShape.setFill(Color.web("#238BFA")); 
        customShape.setStroke(null); 

        customShape.setScaleX(5.0); 
        customShape.setScaleY(5.0); 

      
        customShape.setTranslateX(-100);
        customShape.setTranslateY(-100); 

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
        createGroupBtn.setStyle(
                "-fx-background-color: #238BFA; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        createGroupBtn.setOnAction(e -> openCreateGroup());

        Button joinGroupBtn = new Button("Join an existing group");
        joinGroupBtn.setStyle(
                "-fx-background-color: transparent; -fx-border-color: #238BFA; -fx-text-fill: #238BFA; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        createGroupBtn.setStyle("-fx-background-color: #238BFA; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        createGroupBtn.setOnAction(e -> openCreateGroup());

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

    }
}
    
