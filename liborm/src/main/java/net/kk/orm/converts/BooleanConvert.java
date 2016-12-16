package net.kk.orm.converts;


import net.kk.orm.SQLiteType;

public class BooleanConvert implements IConvert<Integer, Boolean> {
    public final static SQLiteType TYPE = SQLiteType.INTEGER;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public BooleanConvert() {
    }

    @Override
    public Boolean toValue(Integer val) {
        return val != null && val > 0;
    }

    @Override
    public Integer toDbValue(Boolean value) {
        return value == Boolean.TRUE ? 1 : 0;
    }
}
