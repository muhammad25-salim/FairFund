package project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FarFundManager {
    private Map<String, Group> groups;

    public FarFundManager() {
        this.groups = new HashMap<>();
    }

    
    public void createGroup(String groupId, String groupName, List<User> users) {
        Group group = new Group(groupId, groupName, users);
        groups.put(groupId, group);
    }

    
    public void addExpenseToGroup(String groupId, String title, double totalAmount, User payer, List<User> participants) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.addExpense(title, totalAmount, payer, participants);
        } else {
            System.out.println("Group not found!");
        }
    }

    public void removeExpenseFromGroup(String groupId, Expense expense) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.getExpenses().remove(expense);

            for (User user : group.getUsers()) {
                user.setBalance(0.0);
            }

            for (Expense e : group.getExpenses()) {
                e.calculateBalances();
            }
        } else {
            System.out.println("Group not found!");
        }
    }

    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    public Map<String, Group> getGroups() {
        return groups;
    }
}


