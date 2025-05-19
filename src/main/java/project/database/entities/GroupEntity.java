package project.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "groups")
public class GroupEntity {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String creator;  // New field to store the creator's username

    // Default constructor for ORMLite
    public GroupEntity() {}

    // Constructor for creating a new group
    public GroupEntity(String id, String name, String creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
