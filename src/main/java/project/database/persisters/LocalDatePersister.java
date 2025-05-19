package project.database.persisters;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDatePersister extends BaseDataType {

    private static final LocalDatePersister INSTANCE = new LocalDatePersister();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private LocalDatePersister() {
        super(SqlType.STRING, new Class<?>[]{LocalDate.class});
    }

    public static LocalDatePersister getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        return LocalDate.parse(defaultStr, FORMATTER);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return javaObject == null ? null : ((LocalDate) javaObject).format(FORMATTER);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return sqlArg == null ? null : LocalDate.parse((String) sqlArg, FORMATTER);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        String value = results.getString(columnPos);
        return value == null ? null : value;
    }
}

