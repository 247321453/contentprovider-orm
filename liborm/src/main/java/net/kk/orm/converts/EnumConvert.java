package net.kk.orm.converts;


import android.text.TextUtils;

import net.kk.orm.SQLiteType;
import java.lang.reflect.Method;

public class EnumConvert<T> implements IConvert<String, Object> {
    public final static SQLiteType TYPE = SQLiteType.TEXT;
    private Class<T> mTClass;

    public EnumConvert(Class<T> TClass) {
        mTClass = TClass;
    }

    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    @Override
    public Object toValue(String val) {
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        Method values = null;
        try {
            values = mTClass.getMethod("values");
            Object[] vals = (Object[]) values.invoke(null);
            for (Object o : vals) {
                //isString
                String v = String.valueOf(o);
                if (TextUtils.equals(v, val)) {
                    return o;
                }
            }
            return vals.length > 0 ? vals[0] : null;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String toDbValue(Object value) {
        return String.valueOf(value);
    }
}
