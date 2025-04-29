package project;

import javafx.application.Application;
import javafx.stage.Stage;
import project.controllers.MainPage;
import project.models.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FairFundManager FairFundManager = new FairFundManager();

        MainPage mainPage = new MainPage(FairFundManager);
        mainPage.start(primaryStage);
    }
}
