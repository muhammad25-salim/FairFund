package project.models;

import java.time.LocalDate;

public class Payment {
    private double amount;
    private String from;
    private String to;
    private LocalDate date;
    private String creator; // Add creator field
    private int id;         // Add ID field for database reference

    public Payment(double amount, String from, String to, LocalDate date) {
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Payment(int id, double amount, String from, String to, LocalDate date, String creator) {
        this.id = id;
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.date = date;
        this.creator = creator;
    }

    public double getAmount() {
        return amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
