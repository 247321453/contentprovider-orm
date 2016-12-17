package net.kk.orm.converts;


import net.kk.orm.api.SQLiteOpera;
import net.kk.orm.linq.Orm;
import net.kk.orm.api.SQLiteType;

public abstract class CustomConvert<T> implements IConvert<String, T> {
    public final static SQLiteType TYPE = SQLiteType.TEXT;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }
}
