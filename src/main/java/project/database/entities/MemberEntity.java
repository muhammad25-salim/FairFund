package project.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Members")
public class MemberEntity {

    @DatabaseField(generatedId = true) // Auto-generate unique ID
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "group_id")
    private GroupEntity group;

    public MemberEntity() {
        // ORMLite requires a no-arg constructor
    }

    public MemberEntity(String name, GroupEntity group) {
        this.name = name;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }
}
