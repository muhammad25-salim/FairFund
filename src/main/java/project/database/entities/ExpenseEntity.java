package project.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "expenses")
public class ExpenseEntity {

    @DatabaseField(generatedId = true)  // Auto-generate unique ID
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField
    private double totalAmount;

    @DatabaseField
    private String payer;

    @DatabaseField
    private String groupId;

    @DatabaseField
    private String creator;  // Add creator field

    public ExpenseEntity() {
       
    }
    public ExpenseEntity(String title, double totalAmount, String payer, String groupId) {
        this.title = title;
        this.totalAmount = totalAmount;
        this.payer = payer;
        this.groupId = groupId;        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }

}