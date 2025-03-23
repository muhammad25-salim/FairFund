package project;

import java.util.ArrayList;
import java.util.List;

class Group {
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
    }

    public String getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public List<User> getUsers() { return users; }
    public List<Expense> getExpenses() { return expenses; }
    
    @Override
    public String toString() {
        return "Group: " + groupName + " (ID: " + groupId + ")";
    }
}
