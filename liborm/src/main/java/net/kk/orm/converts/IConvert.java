package net.kk.orm.converts;

import net.kk.orm.Orm;
import net.kk.orm.SQLiteType;

/**
 * int[] List/Integer
 * long[] List/Long
 * String[] List/String
 */
public interface IConvert<D, V> {
    SQLiteType getSQLiteType();

    V toValue(Orm orm, D d);

    D toDbValue(Orm orm,V value);
}

