package net.kk.orm.converts;


import android.util.Log;

import net.kk.orm.annotations.Column;
import net.kk.orm.Orm;
import net.kk.orm.enums.SQLiteType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeConverts {
    private static final Map<Class<?>, IConvert<?, ?>> CLASS_TYPE_CONVERT_MAP = new HashMap<>();
    private static final Map<String, IConvert<?, ?>> CLASS_ID_CONVERT_MAP = new HashMap<>();
    private static TypeConverts sTypeConverts = new TypeConverts();

    private TypeConverts() {
        register(Boolean.class, new BooleanConvert());
        register(Date.class, new DateConvert());
        register(byte[].class, new SameConvert<>(byte[].class, SQLiteType.BLOB));
        register(Double.class, new SameConvert<>(Double.class, SQLiteType.DOUBLE));
        register(Float.class, new SameConvert<>(Float.class, SQLiteType.FLOAT));
        register(Integer.class, new SameConvert<>(Integer.class, SQLiteType.INTEGER));
        register(Long.class, new SameConvert<>(Long.class, SQLiteType.LONG));
        register(String.class, new SameConvert<>(String.class, SQLiteType.TEXT));

        register(Integer[].class, new ArraysIntegerConvert());
        register(Long[].class, new ArraysLongConvert());
        register(Float[].class, new ArraysFloatConvert());
        register(Double[].class, new ArraysDoubleConvert());
        register(String[].class, new ArraysStringConvert());

        register(int[].class, new IntsConvert());
        register(long[].class, new LongsConvert());
        register(float[].class, new FloatsConvert());
        register(double[].class, new DoublesConvert());
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

    public void registerUniconKey(String key, IConvert<?, ?> typeConvert) {
        CLASS_ID_CONVERT_MAP.put(key, typeConvert);
    }

    public IConvert<?, ?> getUniconKey(String key) {
        return CLASS_ID_CONVERT_MAP.get(key);
    }

    public IConvert<?, ?> find(Class<?> type, Column column) {
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
            }
        }
//        if (typeConvert == null) {
//            typeConvert = new JsonTextConvert<>(type, Orm.getJsonConvert());
//            CLASS_TYPE_CONVERT_MAP.put(key, typeConvert);
//        }
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
