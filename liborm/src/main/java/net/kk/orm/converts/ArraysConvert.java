package net.kk.orm.converts;


import net.kk.orm.Orm;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;

abstract class ArraysConvert<T> implements IConvert<String, T[]> {
    public final static SQLiteType TYPE = SQLiteType.TEXT;

    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    protected abstract T[] createArrays(int len);

    protected abstract T toT(String val);

    protected String toString(T t) {
        if (t == null) {
            return NULL;
        }
        return "" + t;
    }

    protected static final String NULL = "___NULL";

    @Override
    public T[] toValue(Orm orm, String val) {
        if (val == null) return null;
        String[] vs = val.split(",");
        T[] is = createArrays(vs.length);
        for (int i = 0; i < vs.length; i++) {
            String v = vs[i];
            if(!NULL.equals(v)){
                is[i] = toT(v);
            }
        }
        return is;
    }

    @Override
    public String toDbValue(Orm orm, T[] value, SQLiteOpera opera) {
        if (value == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(toString(value[i]));
        }
        return stringBuilder.toString();
    }
}
