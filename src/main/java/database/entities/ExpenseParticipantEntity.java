package project.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "expense_participants")
public class ExpenseParticipantEntity {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "expense_id")
    private ExpenseEntity expense;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "Member_id")
    private MemberEntity Member;

    public ExpenseParticipantEntity() {}

    public ExpenseParticipantEntity(ExpenseEntity expense, MemberEntity Member) {
        this.expense = expense;
        this.Member = Member;
    }

    public ExpenseEntity getExpense() {
        return expense;
    }

    public MemberEntity getMember() {
        return Member;
    }
}