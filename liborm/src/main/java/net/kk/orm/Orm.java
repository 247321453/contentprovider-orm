package net.kk.orm;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import net.kk.orm.converts.IConvert;
import net.kk.orm.utils.IJsonConvert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Orm {
    public static final String TAG = "orm";
    public static final boolean DEBUG = false;
    private IContentResolver helper;
    private static Map<Class<?>, OrmTable<?>> sOrmTableHashMap = new HashMap<>();
    private static IJsonConvert sIJsonConvert;

    @SuppressWarnings("unchecked")
    public static <T> OrmTable<T> table(Class<T> pClass) {
        final Class<?> key = TypeConverts.wrapper(pClass);
        OrmTable<?> column = sOrmTableHashMap.get(key);
        if (column == null) {
            synchronized (OrmTable.class) {
                if (column == null) {
                    column = new OrmTable(pClass);
                    sOrmTableHashMap.put(key, column);
                }
            }
        }
        return (OrmTable<T>) column;
    }

    public static void initJson(IJsonConvert IJsonConvert) {
        sIJsonConvert = IJsonConvert;
    }

    public static IJsonConvert getJsonConvert() {
        return sIJsonConvert;
    }

    /***
     * 如果使用自定义类
     */
    public Orm(IContentResolver helper) {
        this.helper = helper;
    }

    public Orm(Context context) {
        this.helper = new DefaultContentResolver(context);
    }

    public IContentResolver getContentResolver() {
        return helper;
    }

    public static IContentResolver getContentResolver(Context context) {
        return new DefaultContentResolver(context);
    }

    public <T> T read(Class<T> tClass, Cursor cursor) {
        OrmTable<T> tOrmTable = table(tClass);
        return tOrmTable.read(cursor);
    }

    public void register(Class<?> pClass, IConvert<?, ?> typeConvert) {
        TypeConverts.get().register(pClass, typeConvert);
    }

    public <T> OrmSelector<T> select(Class<T> pClass) {
        return new OrmSelector<T>(helper, pClass);
    }

    public <T> int delete(Class<T> pClass, WhereBuilder<T> whereBuilder) {
        OrmTable<T> table = table(pClass);
        if (table == null) {
            return 0;
        }
        String where = whereBuilder.getWhereString();
        String[] whereArgs = whereBuilder.getWhereItems();
        try {
            if (table.getTableUri() != null) {
                return helper.delete(table.getTableUri(), where, whereArgs);
            }
            return 0;
        } catch (Exception e) {
            Log.e(Orm.TAG, "delete", e);
            return -1;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> int delete(T object) {
        if (object == null) return 0;
        OrmTable<T> table = (OrmTable<T>) table(object.getClass());
        if (table == null) {
            return 0;
        }
        WhereBuilder<T> whereBuilder = new WhereBuilder<>(table).only(object);
        return delete(table.getType(), whereBuilder);
    }

    public <T> long replace(T object) {
        //检查key是否存在
        if (update(object) > 0) {
            return 1;
        }
        return insert(object);
    }

    public <T> int update(Class<T> pClass, ContentValues contentValues, WhereBuilder<T> whereBuilder, String... cols) {
        OrmTable<T> table = table(pClass);
        if (table == null) {
            return 0;
        }
        String where;
        String[] whereArgs;
        if (whereBuilder == null) {
            where = null;
            whereArgs = null;
        } else {
            where = whereBuilder.getWhereString();
            whereArgs = whereBuilder.getWhereItems();
        }
//        if(Orm.DEBUG)
        Log.d(Orm.TAG, "update " + contentValues.size() + " where " + where + " args=" + Arrays.toString(whereArgs));
        if (contentValues.size() == 0) {
            return 0;
        }
        try {
            if (table.getTableUri() != null) {
                return helper.update(table.getTableUri(), contentValues, where, whereArgs);
            }
            return -1;
        } catch (Exception e) {
            Log.e(Orm.TAG, "update", e);
            return -1;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> int update(T object, WhereBuilder<T> whereBuilder, String... cols) {
        if (object == null) return -1;
        OrmTable<T> table = (OrmTable<T>) table(object.getClass());
        if (table == null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        table.write(object, contentValues, true,
                (cols == null || cols.length == 0) ? null : Arrays.asList(cols));
        return update(table.getType(), contentValues, whereBuilder, cols);
    }

    /***
     * 如果没有主key，则是全部更新
     */
    @SuppressWarnings("unchecked")
    public <T> int update(T object, String... cols) {
        if (object == null) return 0;
        OrmTable<T> table = (OrmTable<T>) table(object.getClass());
        if (table == null) {
            return 0;
        }
        WhereBuilder<T> whereBuilder = new WhereBuilder<>(table).only(object);
        return update(object, whereBuilder, cols);
    }

    public <T> long insert(T object) {
        if (object == null) return 0;
        OrmTable<?> table = table(object.getClass());
        if (table == null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        table.write(object, contentValues, false, null);
        try {
            long id;
            if (table.getTableUri() != null) {
                Uri uri = helper.insert(table.getTableUri(), contentValues);
                try {
                    id = ContentUris.parseId(uri);
                } catch (Exception e) {
                    id = 1;
                }
            } else {
                id = -1;// helper.getWritableDatabase().insert(table.getTableName(), cols, contentValues);
            }
            OrmColumn column = table.findAuto();
            if (column == null) {
                return id;
            }
            column.setId(object, id);
            return id;
        } catch (Exception e) {
            Log.e(Orm.TAG, "insert", e);
            return -1;
        }
    }
}
