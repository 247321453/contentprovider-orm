package net.kk.orm.converts;


import net.kk.orm.api.Orm;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;

class FloatsConvert implements IConvert<String, float[]> {
    @Override
    public SQLiteType getSQLiteType() {
        return SQLiteType.TEXT;
    }

    @Override
    public float[] toValue(Orm orm, String val) {
        if (val == null) return null;
        String[] vs = val.split(",");
        float[] is = new float[vs.length];
        for (int i = 0; i < vs.length; i++) {
            String v = vs[i];
            is[i] = Float.parseFloat(v);
        }
        return is;
    }

    @Override
    public String toDbValue(Orm orm, float[] value, SQLiteOpera opera) {
        if (value == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(value[i]);
        }
        return stringBuilder.toString();
    }
}
