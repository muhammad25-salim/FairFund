package project.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "groups")
public class GroupEntity {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String name;

    public GroupEntity() {
        // ORMLite requires a no-arg constructor
    }

    public GroupEntity(String id, String name) {
        this.id = id;
        this.name = name;
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
}
