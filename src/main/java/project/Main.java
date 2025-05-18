package project;

import javafx.application.Application;
import javafx.stage.Stage;
import project.controllers.LoginPage;
import project.database.DatabaseHelper;
import project.database.entities.GroupEntity;
import project.utils.ColorManager;

import java.sql.SQLException;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
        @Override
    public void start(Stage primaryStage) {
        try {
            // Load saved theme
            ColorManager.loadSavedTheme();

            // Start the LoginPage directly
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage);

            // Database setup
            System.setProperty("com.j256.ormlite.logger.level", "ERROR");
            DatabaseHelper dbHelper = new DatabaseHelper();
            dbHelper.recreateTables();

            // Sample data (if needed)
            GroupEntity group = new GroupEntity("nm123", "nmuna", "nmuna");
            dbHelper.saveGroup(group);

            List<GroupEntity> groups = dbHelper.getAllGroups();
            System.out.println("Groups in database: " + groups.size());

            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
