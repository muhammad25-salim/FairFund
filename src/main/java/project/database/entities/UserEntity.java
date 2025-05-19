package project.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class UserEntity {

    @DatabaseField(generatedId = true) // Automatically generates the ID field
    private int id;

    @DatabaseField(canBeNull = false, unique = true) // Username should be unique and not null
    private String username;

    @DatabaseField(canBeNull = false)
    private String password;

    // Default constructor for ORMLite
    public UserEntity() {}

    // Constructor for creating a new user
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
