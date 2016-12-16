package net.kk.orm.converts;


import net.kk.orm.SQLiteType;
import net.kk.orm.converts.IConvert;

public class DoubleConvert implements IConvert<Double, Double> {
    public final static SQLiteType TYPE = SQLiteType.DOUBLE;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public DoubleConvert() {
    }

    @Override
    public Double toValue(Double val) {
        return val;
    }

    @Override
    public Double toDbValue(Double value) {
        return value;
    }
}
