package project.models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private String groupName;
    private List<User> users;
    private List<Expense> expenses;

    public Group(String groupId, String groupName, List<User> users) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.users = users;
        this.expenses = new ArrayList<>();
    }
    
    public void addExpense(String title, double totalAmount, User payer, List<User> participants) {
        Expense expense = new Expense(title, totalAmount, payer, participants);
        expenses.add(expense);
        recalculateBalances();
    }

    public void recalculateBalances() {
        // Reset all balances to 0 first
        for (User user : users) {
            user.setBalance(0.0);
        }
    
        // Recalculate all balances based on all expenses
        for (Expense expense : expenses) {
            expense.calculateBalances();
        }
    }

    public void updateExpense(Expense oldExpense, Expense newExpense) {
        int index = expenses.indexOf(oldExpense);
        if (index != -1) {
            
            
            // Apply new balances
            newExpense.calculateBalances();
        }
    }
    
    public User getUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }
    
    public String getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public List<User> getUsers() { return users; }
    public List<Expense> getExpenses() { return expenses; }
}
