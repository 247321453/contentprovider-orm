package net.kk.orm.converts;


import net.kk.orm.SQLiteType;
import net.kk.orm.converts.IConvert;

public class IntegerConvert implements IConvert<Integer, Integer> {
    public final static SQLiteType TYPE = SQLiteType.INTEGER;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public IntegerConvert() {
    }

    @Override
    public Integer toValue(Integer val) {
        return val;
    }

    @Override
    public Integer toDbValue(Integer value) {
        return value;
    }
}
