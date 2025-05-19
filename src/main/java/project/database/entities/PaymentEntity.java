package project.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import project.database.persisters.LocalDatePersister;

import java.time.LocalDate;

@DatabaseTable(tableName = "payments")
public class PaymentEntity {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private double amount;

    @DatabaseField
    private String from;

    @DatabaseField
    private String to;

    @DatabaseField(persisterClass = LocalDatePersister.class) // Use the custom persister
    private LocalDate date;

    @DatabaseField
    private String groupId;
    
    @DatabaseField
    private String creator;

    public PaymentEntity() {}

    public PaymentEntity(double amount, String from, String to, LocalDate date, String groupId, String creator) {
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.date = date;
        this.groupId = groupId;
        this.creator = creator;
    }

    // Add creator getter and setter
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
