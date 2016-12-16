package net.kk.orm.converts;


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
    public abstract T toValue(String val);

    @Override
    public abstract String toDbValue(T value);

}
