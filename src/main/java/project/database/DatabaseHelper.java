package project.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import project.database.entities.GroupEntity;
import project.database.entities.MemberEntity;
import project.database.entities.UserEntity;
import project.database.entities.ExpenseEntity;
import project.database.entities.ExpenseParticipantEntity;
import project.database.entities.PaymentEntity;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

public class DatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:fairfund.db";

     private ConnectionSource connectionSource;
    private Dao<GroupEntity, String> groupDao;
    private Dao<MemberEntity, Integer> MemberDao;
    private Dao<ExpenseEntity, Integer> expenseDao;
    private Dao<ExpenseParticipantEntity, Integer> expenseParticipantDao;
    private Dao<UserEntity, Integer> userDao;
    private Dao<PaymentEntity, Integer> paymentDao

    public DatabaseHelper() throws SQLException {
        connectionSource = new JdbcConnectionSource(DATABASE_URL);
        createTables();
        initializeDaos();
    }

    private void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, GroupEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, UserEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ExpenseEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ExpenseParticipantEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, PaymentEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, MemberEntity.class);


    }

    private void initializeDaos() throws SQLException {
        groupDao = DaoManager.createDao(connectionSource, GroupEntity.class); 
        userDao = DaoManager.createDao(connectionSource, UserEntity.class);
        expenseDao = DaoManager.createDao(connectionSource, ExpenseEntity.class);
        expenseParticipantDao = DaoManager.createDao(connectionSource, ExpenseParticipantEntity.class);
        MemberDao = DaoManager.createDao(connectionSource, MemberEntity.class);
        paymentDao = DaoManager.createDao(connectionSource, PaymentEntity.class);

    }
    

    // Save methods

       public void saveUser(UserEntity user) throws SQLException {
        try {
            // Assuming you have a DAO for UserEntity
            userDao.create(user);
            System.out.println("User saved to database: " + user.getUsername());
        } catch (SQLException e) {
            System.err.println("Error saving user to database: " + e.getMessage());
            throw e;
        }
    }

    public void saveGroup(GroupEntity group) throws SQLException {
        groupDao.createOrUpdate(group); // This should insert or update the group in the database
    }

    public void saveMember(MemberEntity Member) throws SQLException {
        MemberDao.createOrUpdate(Member);
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

    public void saveExpenseParticipants(ExpenseEntity expenseEntity, List<MemberEntity> Members) throws SQLException {
        for (MemberEntity Member : Members) {
            expenseParticipantDao.create(new ExpenseParticipantEntity(expenseEntity, Member));
        }
    }

    public void deleteParticipantsByExpense(ExpenseEntity expense) throws SQLException {
        List<ExpenseParticipantEntity> links = expenseParticipantDao.queryForEq("expense_id", expense.getId());
        for (ExpenseParticipantEntity link : links) {
            expenseParticipantDao.delete(link);
        }
    } 

    // Retrieve methods
    public UserEntity getUserByUsername(String username) throws SQLException {
        return userDao.queryForEq("username", username).stream().findFirst().orElse(null);
    }

    public List<GroupEntity> getAllGroups() throws SQLException {
        return groupDao.queryForAll();
    }

     public List<MemberEntity> getMembersByGroup(GroupEntity group) throws SQLException {
        return MemberDao.queryForEq("group_id", group);
    }

    public List<ExpenseEntity> getExpensesByGroup(String groupId) throws SQLException {
        return expenseDao.queryForEq("groupId", groupId);
    }

       public List<MemberEntity> getParticipantsByExpense(ExpenseEntity expenseEntity) throws SQLException {
        List<ExpenseParticipantEntity> links = expenseParticipantDao.queryForEq("expense_id", expenseEntity.getId());
        List<MemberEntity> participants = new ArrayList<>();
        for (ExpenseParticipantEntity link : links) {
            participants.add(link.getMember());
        }
        return participants;
    }

    public List<GroupEntity> getGroupsByCreator(String creatorUsername) throws SQLException {
        return getGroupDao().queryBuilder().where().eq("creator", creatorUsername).query();
    }

    public List<PaymentEntity> getPaymentsByGroup(String groupId) throws SQLException {
        return paymentDao.queryBuilder().where().eq("groupId", groupId).query();
    }

    public Dao<GroupEntity, String> getGroupDao() {
        return groupDao;
    }

    public Dao<UserEntity, Integer> getUserDao() throws SQLException {
        return userDao;
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
        TableUtils.createTableIfNotExists(connectionSource, MemberEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ExpenseParticipantEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, PaymentEntity.class);

    }

        public void savePayment(PaymentEntity payment) throws SQLException {
        paymentDao.create(payment);
    }

    public void deletePayment(PaymentEntity payment) throws SQLException {
        paymentDao.delete(payment);
    }

}
