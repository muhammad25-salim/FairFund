package project.models;

import project.database.DatabaseHelper;
import project.database.entities.GroupEntity;
import project.database.entities.UserEntity;
import project.database.entities.ExpenseEntity;
import project.database.entities.MemberEntity;
import project.database.entities.PaymentEntity;

import java.lang.reflect.Member;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FairFundManager {

    private DatabaseHelper databaseHelper;
    private Map<String, Group> groups;
    private UserEntity currentUser;

    public FairFundManager() {
        try {
            databaseHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.groups = new HashMap<>();
    }

    // Set current user
    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void createGroup(String groupId, String groupName, List<Member> Members) {
        // Ensure the currentUser is set before using it
        if (currentUser == null) {
            throw new IllegalStateException("Current user is not set.");
        }
        // Create a new group
        Group group = new Group(groupId, groupName, Members);
        groups.put(groupId, group);

        // Save the group to the database
        GroupEntity groupEntity = new GroupEntity(groupId, groupName, currentUser.getUsername());
        try {
            databaseHelper.saveGroup(groupEntity);

            // Save Members to the database with the correct groupId
            for (Member member : Members) {
                MemberEntity memberEntity = new MemberEntity(member.getName(), groupEntity);
                databaseHelper.saveMember(memberEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addExpenseToGroup(String groupId, String title, double totalAmount, Member payer, List<Member> participants) {
        Group group = groups.get(groupId);
        if (group != null) {
            // Create expense with creator set
            Expense expense = new Expense(0, title, totalAmount, payer, participants);
            expense.setCreator(currentUser.getUsername());  // Set the creator here
            
            ExpenseEntity expenseEntity = new ExpenseEntity(title, totalAmount, payer.getName(), groupId);
            expenseEntity.setCreator(currentUser.getUsername());  // Set creator in entity
            
            try {
                databaseHelper.saveExpense(expenseEntity);
                expense.setId(expenseEntity.getId());
    
                List<MemberEntity> participantEntities = new ArrayList<>();
                for (Member u : participants) {
                    for (MemberEntity ue : databaseHelper.getMembersByGroup(new GroupEntity(groupId, group.getGroupName(), currentUser.getUsername()))) {
                        if (ue.getName().equals(u.getName())) {
                            participantEntities.add(ue);
                            break;
                        }
                    }
                }
    
                // Save participants to the database
                databaseHelper.saveExpenseParticipants(expenseEntity, participantEntities);
    
                group.getExpenses().add(expense);
                group.recalculateBalances();
                
                System.out.println("Expense added with real ID: " + expense.getId());
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

                    // Preserve the creator field
                    expenseEntity.setCreator(oldExpense.getCreator());  // Ensure the creator is not lost
    
                    // Save the updated expense to the database
                    databaseHelper.saveExpense(expenseEntity);  // This updates the expense in DB
    
                    // Delete old participants
                    databaseHelper.deleteParticipantsByExpense(expenseEntity);

                    // Add new participants
                    List<MemberEntity> participantEntities = new ArrayList<>();
                    for (Member u : newExpense.getParticipants()) {
                        for (MemberEntity ue : databaseHelper.getMembersByGroup(new GroupEntity(groupId, group.getGroupName(), currentUser.getUsername()))) {
                            if (ue.getName().equals(u.getName())) {
                                participantEntities.add(ue);
                                break;
                            }
                        }
                    }
                    databaseHelper.saveExpenseParticipants(expenseEntity, participantEntities);

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


