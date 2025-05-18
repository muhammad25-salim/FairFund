package project.models;

import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {

    private SimpleStringProperty title; 
    private SimpleDoubleProperty totalAmount; 
    private User payer; 
    private List<User> participants; 
    private int id;
    private String creator;

    public Expense(int id ,String title, double totalAmount, User payer, List<User> participants) {
        this.title = new SimpleStringProperty(title);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.payer = payer;
        this.participants = participants;
        this.id=id;
    }

   
    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setPayer(User payer) {
        this.payer = payer;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void calculateBalances() {
        if (participants == null || participants.isEmpty()) {
            return;
        }
    
        double splitAmount = totalAmount.get() / participants.size();
    
        // 1. Participants owe their share
        for (User participant : participants) {
            participant.setBalance(participant.getBalance() - splitAmount);
        }
    
        // 2. Payer gets credited the full amount
        payer.setBalance(payer.getBalance() + totalAmount.get());
    }
}

