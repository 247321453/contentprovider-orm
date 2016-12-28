package net.kk.orm.converts;


import net.kk.orm.api.Orm;
import net.kk.orm.enums.SQLiteType;

class LongsConvert implements IConvert<String, long[]> {
    @Override
    public SQLiteType getSQLiteType() {
        return SQLiteType.TEXT;
    }

    @Override
    public long[] toValue(Orm orm, String val) {
        if (val == null) return null;
        String[] vs = val.split(",");
        long[] is = new long[vs.length];
        for (int i = 0; i < vs.length; i++) {
            String v = vs[i];
            is[i] = Long.parseLong(v);
        }
        return is;
    }

    @Override
    public String toDbValue(Orm orm, long[] value, int opera) {
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
