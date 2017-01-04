package net.kk.orm.converts;


import net.kk.orm.api.Orm;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;

import java.util.Date;

/**
 * ‘YYYY-MM-DD
 */
class DateConvert implements IConvert<Long, Date> {
    public final static SQLiteType TYPE = SQLiteType.LONG;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public DateConvert() {
    }

    @Override
    public Date toValue(Orm orm, Long val) {
        return new Date(val);
    }

    @Override
    public Long toDbValue(Orm orm,Date value,SQLiteOpera opera) {
        return value.getTime();
    }
}
