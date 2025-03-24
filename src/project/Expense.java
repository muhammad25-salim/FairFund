package project;

import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {
    private SimpleStringProperty title; 
    private SimpleDoubleProperty totalAmount; 
    private User payer; 
    private List<User> participants;

    public Expense(String title, double totalAmount, User payer, List<User> participants) {
        this.title = new SimpleStringProperty(title);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.payer = payer;
        this.participants = participants;
        calculateBalances();
    }

   
    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public User getPayer() {
        return payer;
    }

    public List<User> getParticipants() {
        return participants;
    }

   
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public void calculateBalances() {
        double splitAmount = totalAmount.get() / participants.size();
        for (User participant : participants) {
            if (participant.equals(payer)) {
                participant.setBalance(participant.getBalance() + (totalAmount.get() - splitAmount));
            } else {
                participant.setBalance(participant.getBalance() - splitAmount);
            }
        }
    }

    @Override
    public String toString() {
        return "Expense: " + title.get() + " (Total: " + totalAmount.get() + ", Payer: " + payer.getName() + ")";
    }
}
