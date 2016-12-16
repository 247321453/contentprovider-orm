package net.kk.orm;


import android.util.Log;

import net.kk.orm.converts.BooleanConvert;
import net.kk.orm.converts.BytesConvert;
import net.kk.orm.converts.DoubleConvert;
import net.kk.orm.converts.EnumConvert;
import net.kk.orm.converts.FloatConvert;
import net.kk.orm.converts.IConvert;
import net.kk.orm.converts.IntegerConvert;
import net.kk.orm.converts.JsonTextConvert;
import net.kk.orm.converts.LongConvert;
import net.kk.orm.converts.StringConvert;

import java.util.HashMap;
import java.util.Map;

class TypeConverts {
    private static final Map<Class<?>, IConvert<?, ?>> CLASS_TYPE_CONVERT_MAP = new HashMap<>();
    private static TypeConverts sTypeConverts = new TypeConverts();

    private TypeConverts() {
        register(Boolean.class, new BooleanConvert());
        register(byte[].class, new BytesConvert());
        register(Double.class, new DoubleConvert());
        register(Float.class, new FloatConvert());
        register(Integer.class, new IntegerConvert());
        register(Long.class, new LongConvert());
        register(String.class, new StringConvert());
    }

    public static TypeConverts get() {
        return sTypeConverts;
    }

    public void register(Class<?> pClass, IConvert<?, ?> typeConvert) {
        if (typeConvert == null) return;
        final Class<?> key = wrapper(pClass);
        CLASS_TYPE_CONVERT_MAP.put(key, typeConvert);
        if (Orm.DEBUG) {
            Log.v(Orm.TAG, "registers count " + CLASS_TYPE_CONVERT_MAP.size() + " add " + key + " " + typeConvert.getClass());
        }
    }

    public boolean isSupport(Class<?> pClass) {
        return CLASS_TYPE_CONVERT_MAP.containsKey(wrapper(pClass));
    }

    public IConvert<?, ?> find(Class<?> type) {
        final Class<?> key = wrapper(type);
        IConvert<?, ?> typeConvert = CLASS_TYPE_CONVERT_MAP.get(key);
        if (typeConvert != null) {
            if (Orm.DEBUG) {
                Log.v(Orm.TAG, "find convert " + key);
            }
            return typeConvert;
        } else {
            if (type.isEnum()) {
                typeConvert = new EnumConvert<>(type);
                CLASS_TYPE_CONVERT_MAP.put(key, typeConvert);
            } else {
                typeConvert = new JsonTextConvert<>(type);
            }
        }
        return typeConvert;
    }

    public static Class<?> wrapper(Class<?> type) {
        if (type == null) {
            return Object.class;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }
        return type;
    }
}
