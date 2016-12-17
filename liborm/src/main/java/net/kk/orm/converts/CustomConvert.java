package net.kk.orm.converts;


import net.kk.orm.Orm;
import net.kk.orm.SQLiteType;

public abstract class CustomConvert<T> implements IConvert<String, T> {
    public final static SQLiteType TYPE = SQLiteType.TEXT;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public CustomConvert() {
    }

    @Override
    public abstract T toValue(Orm orm, String val);

    @Override
    public abstract String toDbValue(Orm orm,T value);

}
