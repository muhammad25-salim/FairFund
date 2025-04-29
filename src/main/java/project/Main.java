package project;

import javafx.application.Application;
import javafx.stage.Stage;
import project.controllers.MainPage;
import project.models.*;
import project.database.DatabaseHelper;
import project.database.entities.GroupEntity;
import java.sql.SQLException;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
        try {
            System.setProperty("com.j256.ormlite.logger.level", "ERROR");
            DatabaseHelper dbHelper = new DatabaseHelper();

            dbHelper.recreateTables();

            // Save a group
            GroupEntity group = new GroupEntity("nm123","nmuna");
            dbHelper.saveGroup(group);

            
            List<GroupEntity> groups = dbHelper.getAllGroups();
            System.out.println("Groups in database: " + groups.size());

            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) {
        FairFundManager FairFundManager = new FairFundManager();

        MainPage mainPage = new MainPage(FairFundManager);
        mainPage.start(primaryStage);
    }
}
