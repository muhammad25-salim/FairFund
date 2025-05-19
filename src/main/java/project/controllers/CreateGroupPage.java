package project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.models.*;
import project.utils.ColorManager;

public class CreateGroupPage {

    public static Scene getScene(Stage primaryStage, FairFundManager FairFundManager) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_START) + ", " + 
                            ColorManager.toRgbString(ColorManager.LIGHT_BG_GRADIENT_END) + ");");

        // Back Button (styled )
        Button backButton = new Button("« Back");
        backButton.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 24 8 24;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 4, 0, 0, 1);"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.PRIMARY_HOVER_COLOR) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 24 8 24;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.3) + ", 6, 0, 0, 2);" +
            "-fx-cursor: hand;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
            "-fx-background-color: " + ColorManager.toRgbString(ColorManager.getPrimaryColor()) + ";" +
            "-fx-text-fill: " + ColorManager.toRgbString(ColorManager.TEXT_COLOR) + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 24 8 24;" +
            "-fx-effect: dropshadow(gaussian, " + ColorManager.toRgbaString(ColorManager.BLACK_SEMI_TRANSPARENT, 0.2) + ", 4, 0, 0, 1);"
        ));
        backButton.setOnAction(e -> {
            MainPage mainPage = new MainPage(FairFundManager);
            mainPage.start(primaryStage);
        });
    }
}