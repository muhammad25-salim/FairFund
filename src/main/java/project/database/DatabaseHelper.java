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
import java.util.List;

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

    public void saveExpense(ExpenseEntity expense) throws SQLException {
        // This should either create or update the expense in the database
        Dao.CreateOrUpdateStatus status = expenseDao.createOrUpdate(expense);
    
        // Log the result of the createOrUpdate operation
        if (status.isCreated()) {
            System.out.println("Expense created in database with ID: " + expense.getId());
        } else if (status.isUpdated()) {
            System.out.println("Expense updated in database with ID: " + expense.getId());
        } else {
            System.out.println("No changes made to the expense with ID: " + expense.getId());
        }
    }
    
      
    public void deleteExpense(ExpenseEntity expense) throws SQLException {
        // Log for debugging to see which expense is being deleted
        System.out.println("Deleting expense from database with ID: " + expense.getId());
        expenseDao.delete(expense);  // Deletes the expense from the database
    }
    

    // Retrieve methods
    public List<GroupEntity> getAllGroups() throws SQLException {
        return groupDao.queryForAll();
    }

    public List<UserEntity> getUsersByGroup(GroupEntity group) throws SQLException {
        return userDao.queryForEq("group_id", group);
    }

    public List<ExpenseEntity> getExpensesByGroup(String groupId) throws SQLException {
        return expenseDao.queryForEq("groupId", groupId);
    }

    public Dao<GroupEntity, String> getGroupDao() {
        return groupDao;
    }

    public void close() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Example of recreating tables
    public void recreateTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, GroupEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, UserEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ExpenseEntity.class);
    }
}
