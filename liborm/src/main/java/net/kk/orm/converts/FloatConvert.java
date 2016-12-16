package net.kk.orm.converts;


import net.kk.orm.SQLiteType;
import net.kk.orm.converts.IConvert;
public class FloatConvert implements IConvert<Float, Float> {
    public final static SQLiteType TYPE = SQLiteType.FLOAT;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public FloatConvert() {
    }

    @Override
    public Float toValue(Float val) {
        return val;
    }

    @Override
    public Float toDbValue(Float value) {
        return value;
    }
}
