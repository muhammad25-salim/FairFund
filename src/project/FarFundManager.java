package project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FarFundManager {
    private Map<String, Group> groups;

    public FarFundManager() {
        this.groups = new HashMap<>();
    }

    // Create a new group
    public void createGroup(String groupId, String groupName, List<User> users) {
        Group group = new Group(groupId, groupName, users);
        groups.put(groupId, group);
    }

    // Add an expense to a group
    public void addExpenseToGroup(String groupId, String title, double totalAmount, User payer, List<User> participants) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.addExpense(title, totalAmount, payer, participants);
        } else {
            System.out.println("Group not found!");
        }
    }

    // Remove an expense from a group
    public void removeExpenseFromGroup(String groupId, Expense expense) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.getExpenses().remove(expense);

            // Reset all user balances to zero
            for (User user : group.getUsers()) {
                user.setBalance(0.0);
            }

            // Recalculate balances for all remaining expenses
            for (Expense e : group.getExpenses()) {
                e.calculateBalances();
            }
        } else {
            System.out.println("Group not found!");
        }
    }

    // Get a group by ID
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    // Get all groups
    public Map<String, Group> getGroups() {
        return groups;
    }
}


