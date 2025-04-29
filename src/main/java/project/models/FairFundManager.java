package project.models;

import project.database.DatabaseHelper;
import project.database.entities.GroupEntity;
import project.database.entities.UserEntity;
import project.database.entities.ExpenseEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FairFundManager {
    private DatabaseHelper databaseHelper;
    private Map<String, Group> groups;

    public FairFundManager() {
        try {
            databaseHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.groups = new HashMap<>();
    }

    
    public void createGroup(String groupId, String groupName, List<User> users) {
        Group group = new Group(groupId, groupName, users);
        groups.put(groupId, group);
        // Save the group to the database
        GroupEntity groupEntity = new GroupEntity(groupId, groupName);
        try {
            databaseHelper.saveGroup(groupEntity);

            // Save users to the database
            for (User user : users) {
                UserEntity userEntity = new UserEntity(user.getName(), groupEntity);
                databaseHelper.saveUser(userEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void addExpenseToGroup(String groupId, String title, double totalAmount, User payer, List<User> participants) {
        Group group = groups.get(groupId);
        if (group != null) {
            // Create a new Expense object in memory (with a dummy ID, 0)
            Expense expense = new Expense(0, title, totalAmount, payer, participants);
    
            // Save the expense to the database
            ExpenseEntity expenseEntity = new ExpenseEntity(title, totalAmount, payer.getName(), groupId);
            try {
                // Save to DB and set the real ID from the database
                databaseHelper.saveExpense(expenseEntity);  // Save to database
    
                // After saving, set the real ID to in-memory expense
                expense.setId(expenseEntity.getId());  // Update the real ID after insertion
                group.getExpenses().add(expense);  // Add the expense with the correct ID to the group
    
                // Call recalculateBalances to update user balances
                group.recalculateBalances();  // Make sure balances are updated after adding an expense
    
                // Log the real ID after saving
                System.out.println("Expense added with real ID: " + expense.getId());  // Debugging line here
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Group not found!");
        }
    }

    public void removeExpenseFromGroup(String groupId, Expense expense) {
        Group group = groups.get(groupId);
        if (group != null) {
            // Remove expense from in-memory group
            group.getExpenses().remove(expense);
            group.recalculateBalances();
    
            // Ensure the expense has a real ID before deleting
            if (expense.getId() != 0) {
                // Delete expense from database
                try {
                    ExpenseEntity expenseEntity = new ExpenseEntity();
                    expenseEntity.setId(expense.getId());  // Use the real ID
    
                    // Log the ID being deleted for debugging
                    System.out.println("Deleting expense from database with ID: " + expense.getId());
    
                    // Delete the expense from the database
                    databaseHelper.deleteExpense(expenseEntity);  // Delete from DB
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Cannot delete: Expense ID is 0, it has not been saved to the database.");
            }
        } else {
            System.out.println("Group not found!");
        }
    }

    public void updateExpenseInGroup(String groupId, Expense oldExpense, Expense newExpense) {
        Group group = groups.get(groupId);
        if (group != null) {
            // Find the old expense and update it in the list
            int index = group.getExpenses().indexOf(oldExpense);
            if (index != -1) {
                // Replace the old expense with the new one in the list
                group.getExpenses().set(index, newExpense);
                group.recalculateBalances();  // Recalculate balances after the update
    
                // Step 2: Update the expense in the database
                try {
                    // Create ExpenseEntity with updated details
                    ExpenseEntity expenseEntity = new ExpenseEntity();
                    expenseEntity.setId(newExpense.getId());
                    expenseEntity.setTitle(newExpense.getTitle());
                    expenseEntity.setTotalAmount(newExpense.getTotalAmount());
                    expenseEntity.setPayer(newExpense.getPayer().getName());
                    expenseEntity.setGroupId(groupId);
    
                    // Save the updated expense to the database
                    databaseHelper.saveExpense(expenseEntity);  // This updates the expense in DB
    
                    // Log the update for debugging
                    System.out.println("Expense updated in database with ID: " + newExpense.getId());
                } catch (SQLException e) {
                    System.out.println("Error updating expense in database: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Old expense not found in the group.");
            }
        } else {
            System.out.println("Group not found!");
        }
    }

    public boolean loadGroup(String groupId) {
        try {
            // Load group entity
            GroupEntity groupEntity = databaseHelper.getGroupDao().queryForId(groupId);
            if (groupEntity == null) {
                System.out.println("Group not found in database: " + groupId);
                return false;
            }
    
            // Load users
            List<UserEntity> userEntities = databaseHelper.getUsersByGroup(groupEntity);
            List<User> users = new ArrayList<>();
            for (UserEntity ue : userEntities) {
                users.add(new User(ue.getName()));
            }
    
            // Create group
            Group group = new Group(groupId, groupEntity.getName(), users);
    
            // Load expenses
            List<ExpenseEntity> expenseEntities = databaseHelper.getExpensesByGroup(groupId);
            for (ExpenseEntity ee : expenseEntities) {
                User payer = group.getUserByName(ee.getPayer());
                if (payer == null) continue; // If payer not found, skip
    
                // Assume all users are participants (or you can customize this)
                List<User> participants = new ArrayList<>(group.getUsers());
                Expense expense = new Expense(ee.getId(), ee.getTitle(), ee.getTotalAmount(), payer, participants);  // Use the ID from the database
                group.getExpenses().add(expense);
            }
    
            // Recalculate balances for the loaded group
            group.recalculateBalances();  // Recalculate balances after loading the group
    
            // Put group into memory
            groups.put(groupId, group);
    
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void close() {
        databaseHelper.close();
    }
}


