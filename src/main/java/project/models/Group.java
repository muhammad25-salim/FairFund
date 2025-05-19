package project.models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private String groupName;
    private List<Member> Members;
    private List<Expense> expenses;
    private List<Payment> payments; // List of payments

    public Group(String groupId, String groupName, List<Member> Members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.Members = Members;
        this.expenses = new ArrayList<>();
        this.payments = new ArrayList<>(); // Initialize payments list
    }

    
    public void addExpense(String title, double totalAmount, Member payer, List<Member> participants) {
        Expense expense = new Expense(0, title, totalAmount, payer, participants);  // Pass 0 for id initially
        expenses.add(expense);
        recalculateBalances();
    }

    public void recalculateBalances() {
        // Reset all balances to 0 first
        for (Member member : Members) {
            member.setBalance(0.0);
        }
    
        // Apply all expenses again
        for (Expense expense : expenses) {
            expense.calculateBalances();
        }
        
        // Apply all payments
        for (Payment payment : payments) {
            Member payer = getMemberByName(payment.getFrom());
            Member payee = getMemberByName(payment.getTo());
            
            if (payer != null && payee != null) {
                payer.setBalance(payer.getBalance() + payment.getAmount());
                payee.setBalance(payee.getBalance() - payment.getAmount());
            }
        }
    }


    public void updateExpense(Expense oldExpense, Expense newExpense) {
        int index = expenses.indexOf(oldExpense);
        if (index != -1) {
            // First, remove the old expense impact
            expenses.remove(oldExpense);
            recalculateBalances();  // Recalculate balances before adding the new expense
    
            // Add the new expense
            expenses.add(newExpense);
            recalculateBalances();  // Recalculate again with the new expense
        }
    }
    
    

    public Member getMemberByName(String name) {
        for (Member Member : Members) {
            if (Member.getName().equals(name)) {
                return Member;
            }
        }
        return null;
    }
    

    public String getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public List<Member> getMembers() { return Members; }
    public List<Expense> getExpenses() { return expenses; }
    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
}