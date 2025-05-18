package project.controllers;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import project.database.DatabaseHelper;
import project.database.entities.UserEntity;
import project.models.FairFundManager;
import project.utils.ColorManager;

import java.sql.SQLException;

public class LoginPage extends Application {
    private FairFundManager fairFundManager;

    public LoginPage() {
        this.fairFundManager = new FairFundManager();
    }
    
    public LoginPage(FairFundManager fairFundManager) {
        this.fairFundManager = fairFundManager;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize database and tables
        try {
            System.setProperty("com.j256.ormlite.logger.level", "ERROR");
            DatabaseHelper dbHelper = new DatabaseHelper();
            dbHelper.recreateTables();
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        primaryStage.setScene(getLoginScene(primaryStage));
        primaryStage.setTitle("FairFund - Login");
        primaryStage.show();
    }

    public static Scene getScene(Stage primaryStage) {
        // For backward compatibility
        return getLoginScene(primaryStage);
    }

    public static Scene getLoginScene(Stage primaryStage) {
        // Main layout container with two sides
        HBox mainContainer = new HBox();
        
        // Left side - Luxury image or gradient panel (40% of width)
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(480); // 40% of 1200px
        leftPanel.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + 
            ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", " + 
            "derive(" + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ", 30%));" +
            "-fx-effect: dropshadow(three-pass-box, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.4) + ", 15, 0, 0, 0);"
        );
    }
}