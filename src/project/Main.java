package project;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FarFundManager farFundManager = new FarFundManager();

        MainPage mainPage = new MainPage(farFundManager);
        mainPage.start(primaryStage);
    }
}
