package net.kk.orm.converts;


import net.kk.orm.enums.SQLiteType;

public abstract class CustomConvert<T> implements IConvert<String, T> {
    public final static SQLiteType TYPE = SQLiteType.TEXT;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }
}
