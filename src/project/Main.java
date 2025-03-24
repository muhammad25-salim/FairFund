package project;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of FarFundManager (backend)
        FarFundManager farFundManager = new FarFundManager();

        // Pass the FarFundManager instance to MainPage
        MainPage mainPage = new MainPage(farFundManager);
        mainPage.start(primaryStage);
    }
}
