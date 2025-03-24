package project;

import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {
<<<<<<< HEAD
    private SimpleStringProperty title; 
    private SimpleDoubleProperty totalAmount; 
    private User payer; 
    private List<User> participants;
=======
    private SimpleStringProperty title; // JavaFX property for title
    private SimpleDoubleProperty totalAmount; // JavaFX property for total amount
    private User payer; // Payer (no need for property here)
    private List<User> participants; // Participants (no need for property here)
>>>>>>> Muhamad_Qadr

    public Expense(String title, double totalAmount, User payer, List<User> participants) {
        this.title = new SimpleStringProperty(title);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.payer = payer;
        this.participants = participants;
        calculateBalances();
    }

<<<<<<< HEAD
   
=======
    // Property getters
>>>>>>> Muhamad_Qadr
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

<<<<<<< HEAD
   
=======
    // Regular getters and setters
>>>>>>> Muhamad_Qadr
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
<<<<<<< HEAD
}
=======
}
>>>>>>> Muhamad_Qadr
