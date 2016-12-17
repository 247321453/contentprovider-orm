package net.kk.orm.converts;

import net.kk.orm.api.SQLiteOpera;
import net.kk.orm.linq.Orm;
import net.kk.orm.api.SQLiteType;

class SameConvert<T> implements IConvert<T, T> {
    @Override
    public SQLiteType getSQLiteType() {
        return type;
    }

    private SQLiteType type;
    private Class<T> pClass;

    public SameConvert(Class<T> pClass, SQLiteType type) {
        this.type = type;
        this.pClass = pClass;
    }

    @Override
    public T toValue(Orm orm, T val) {
        return val;
    }

    @Override
    public T toDbValue(Orm orm, T value, SQLiteOpera opera) {
        return value;
    }
}