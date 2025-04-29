package project.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import project.database.entities.GroupEntity;
import project.database.entities.UserEntity;
import project.database.entities.ExpenseEntity;
import java.sql.SQLException; 

public class DatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:fairfund.db";

    static {
        System.setProperty("com.j256.ormlite.logger.level", "DEBUG");
    }

    private ConnectionSource connectionSource;
    private Dao<GroupEntity, String> groupDao;
    private Dao<UserEntity, Integer> userDao;
    private Dao<ExpenseEntity, Integer> expenseDao;

    public DatabaseHelper() throws SQLException {
        connectionSource = new JdbcConnectionSource(DATABASE_URL);
        createTables();
        initializeDaos();
    }

    private void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, GroupEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, UserEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ExpenseEntity.class);
    }

    private void initializeDaos() throws SQLException {
        groupDao = DaoManager.createDao(connectionSource, GroupEntity.class); 
        userDao = DaoManager.createDao(connectionSource, UserEntity.class);
        expenseDao = DaoManager.createDao(connectionSource, ExpenseEntity.class);
    }
    

    // Save methods
    public void saveGroup(GroupEntity group) throws SQLException {
        groupDao.createOrUpdate(group); // This should insert or update the group in the database
    }

    public void saveUser(UserEntity user) throws SQLException {
        userDao.createOrUpdate(user);
    }
} 