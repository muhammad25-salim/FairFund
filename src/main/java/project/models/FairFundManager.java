package project.models;

import project.database.DatabaseHelper;
import project.database.entities.GroupEntity;
import project.database.entities.UserEntity;
import project.database.entities.ExpenseEntity;
import project.database.entities.MemberEntity;
import project.database.entities.PaymentEntity;

import java.lang.reflect.Member;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FairFundManager {
<<<<<<< HEAD
=======

>>>>>>> muhammadSalim
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
            GroupEntity groupEntity = databaseHelper.getGroupDao().queryForId(groupId);
            if (groupEntity == null) {
                System.out.println("Group not found in database: " + groupId);
                return false;
            }
    
            List<MemberEntity> memberEntities = databaseHelper.getMembersByGroup(groupEntity);
            List<Member> members = new ArrayList<>();
            for (MemberEntity ue : memberEntities) {
                members.add(new Member(ue.getName()));
            }
    
            Group group = new Group(groupId, groupEntity.getName(), members);
    
            // Load expenses
            List<ExpenseEntity> expenseEntities = databaseHelper.getExpensesByGroup(groupId);
            for (ExpenseEntity ee : expenseEntities) {
                Member payer = group.getMemberByName(ee.getPayer());
                if (payer == null) continue;
    
                List<MemberEntity> participantEntities = databaseHelper.getParticipantsByExpense(ee);
                List<Member> participants = new ArrayList<>();
                for (MemberEntity ue : participantEntities) {
                    Member u = group.getMemberByName(ue.getName());
                    if (u != null) participants.add(u);
                }
    
                Expense expense = new Expense(ee.getId(), ee.getTitle(), ee.getTotalAmount(), payer, participants);
                expense.setCreator(ee.getCreator());  // Set the creator
                group.getExpenses().add(expense);
            }
            
            // Load payments
            List<PaymentEntity> paymentEntities = databaseHelper.getPaymentsByGroup(groupId);
            for (PaymentEntity pe : paymentEntities) {
                Payment payment = new Payment(pe.getId(), pe.getAmount(), pe.getFrom(), pe.getTo(), pe.getDate(), pe.getCreator());
                group.getPayments().add(payment);
            }
    
            // Calculate balances considering both expenses and payments
            group.recalculateBalances();
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

    public Map<String, String> getGroupsCreatedByCurrentUser() {
        Map<String, String> groupsMap = new HashMap<>();
        try {
            List<GroupEntity> groups = databaseHelper.getGroupsByCreator(currentUser.getUsername());
            for (GroupEntity group : groups) {
                groupsMap.put(group.getId(), group.getName()); // Map groupId to group name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupsMap;
    }

    public void addPaymentToGroup(String groupId, double amount, String from, String to, LocalDate date) {
        Group group = groups.get(groupId);
        if (group != null) {
            // Create payment with creator field set
            Payment payment = new Payment(0, amount, from, to, date, currentUser.getUsername());
            group.getPayments().add(payment);

            // Adjust balances
            Member payer = findMemberByName(group.getMembers(), from);
            Member payee = findMemberByName(group.getMembers(), to);

            if (payer != null && payee != null) {
                payer.setBalance(payer.getBalance() + amount); // Increase payer's balance
                payee.setBalance(payee.getBalance() - amount); // Decrease payee's balance
            }

            // Save payment to database
            PaymentEntity paymentEntity = new PaymentEntity(amount, from, to, date, groupId, currentUser.getUsername());
            try {
                databaseHelper.savePayment(paymentEntity);
                payment.setId(paymentEntity.getId()); // Set the ID from the database
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Add a method to remove a payment
    public void removePaymentFromGroup(String groupId, Payment payment) {
        Group group = groups.get(groupId);
        if (group != null) {
            // Remove payment from in-memory group
            group.getPayments().remove(payment);
            
            // Reverse the balance changes
            Member payer = findMemberByName(group.getMembers(), payment.getFrom());
            Member payee = findMemberByName(group.getMembers(), payment.getTo());
            
            if (payer != null && payee != null) {
                payer.setBalance(payer.getBalance() - payment.getAmount()); // Decrease payer's balance
                payee.setBalance(payee.getBalance() + payment.getAmount()); // Increase payee's balance
            }
            
            // Recalculate all balances to be sure everything is correct
            group.recalculateBalances();
            
            // Delete from database
            try {
                if (payment.getId() != 0) {
                    PaymentEntity paymentEntity = new PaymentEntity();
                    paymentEntity.setId(payment.getId());
                    databaseHelper.deletePayment(paymentEntity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Member findMemberByName(List<Member> members, String name) {
        for (Member member : members) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    public void close() {
        databaseHelper.close();
    }
}


