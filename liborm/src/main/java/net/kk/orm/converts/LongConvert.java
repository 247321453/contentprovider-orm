package net.kk.orm.converts;


import net.kk.orm.SQLiteType;

public class LongConvert implements IConvert<Long, Long> {
    public final static SQLiteType TYPE = SQLiteType.LONG;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public LongConvert() {
    }

    @Override
    public Long toValue(Long val) {
        return val;
    }

    @Override
    public Long toDbValue(Long value) {
        return value;
    }
}
