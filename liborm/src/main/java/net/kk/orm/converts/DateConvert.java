package net.kk.orm.converts;


import net.kk.orm.SQLiteType;

import java.util.Date;

/**
 * ‘YYYY-MM-DD
 */
public class DateConvert implements IConvert<Long, Date> {
    public final static SQLiteType TYPE = SQLiteType.LONG;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public DateConvert() {
    }

    @Override
    public Date toValue(Long val) {
        return new Date(val);
    }

    @Override
    public Long toDbValue(Date value) {
        return value.getTime();
    }
}
