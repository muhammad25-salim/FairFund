package project.models;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Member {
    private SimpleStringProperty name; // JavaFX property for name
    private SimpleDoubleProperty balance; // JavaFX property for balance

    public Member(String name) {
        this.name = new SimpleStringProperty(name);
        this.balance = new SimpleDoubleProperty(0.0); // Default balance is 0
    }

    // Getters for properties
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty balanceProperty() {
        return balance;
    }

    // Regular getters and setters
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getBalance() {
        return balance.get();
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member Member = (Member) obj;
        return name.equals(Member.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
