package net.kk.orm.converts;

import net.kk.orm.api.Orm;
import net.kk.orm.enums.SQLiteType;

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
    public T toDbValue(Orm orm, T value, int opera) {
        return value;
    }
}