package project.models;

import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {

    private SimpleStringProperty title; 
    private SimpleDoubleProperty totalAmount; 
    private Member payer; 
    private List<Member> participants; 
    private int id;  // Add this ID field for in-memory reference
    private String creator;


    public Expense(int id, String title, double totalAmount, Member payer, List<Member> participants) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.payer = payer;
        this.participants = participants;
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

    public void setPayer(Member payer) {
        this.payer = payer;
    }    

    public Member getPayer() {
        return payer;
    }

    public List<Member> getParticipants() {
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
        for (Member participant : participants) {
            participant.setBalance(participant.getBalance() - splitAmount);
        }
    
        // 2. Payer gets credited the full amount
        payer.setBalance(payer.getBalance() + totalAmount.get());
    }
}

