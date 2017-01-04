package net.kk.orm.converts;

import net.kk.orm.api.Orm;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;

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

