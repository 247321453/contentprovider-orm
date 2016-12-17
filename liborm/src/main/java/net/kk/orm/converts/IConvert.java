package net.kk.orm.converts;

import net.kk.orm.api.SQLiteOpera;
import net.kk.orm.linq.Orm;
import net.kk.orm.api.SQLiteType;

/**
 * int[] List/Integer
 * long[] List/Long
 * String[] List/String
 */
public interface IConvert<D, V> {
    SQLiteType getSQLiteType();

    V toValue(Orm orm, D d);

    D toDbValue(Orm orm,V value,SQLiteOpera opera);
}

