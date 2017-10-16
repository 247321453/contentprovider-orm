package net.kk.orm;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import net.kk.orm.converts.IConvert;
import net.kk.orm.converts.IOjectConvert;
import net.kk.orm.converts.TypeConverts;
import net.kk.orm.enums.SQLiteOpera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orm {
    public static final String TAG = "orm";
    public static boolean DEBUG = false;
    private IContentResolver helper;
    private static Map<Class<?>, OrmTable<?>> sOrmTableHashMap = new HashMap<>();
    private static IOjectConvert sIJsonConvert;

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

    public static void initJson(IOjectConvert IJsonConvert) {
        sIJsonConvert = IJsonConvert;
    }

    public static IOjectConvert getJsonConvert() {
        return sIJsonConvert;
    }

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

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    public <T> T read(Class<T> tClass, Cursor cursor) {
        OrmTable<T> tOrmTable = table(tClass);
        return tOrmTable.read(this, cursor);
    }

    public <T> WhereBuilder<T> where(OrmTable<T> table) {
        return new WhereBuilder<T>(this, table);
    }

    public <T> WhereBuilder<T> where(Class<T> pClass) {
        return new WhereBuilder<T>(this, pClass);
    }

    public void register(Class<?> pClass, IConvert<?, ?> typeConvert) {
        TypeConverts.get().register(pClass, typeConvert);
    }

    public <T> OrmSelector<T> select(Class<T> pClass) {
        return new OrmSelector<T>(this, pClass);
    }

    public <T> int delete(Class<T> pClass, WhereBuilder<T> whereBuilder) throws Exception {
        OrmTable<T> table = table(pClass);
        if (table == null || table.isOnlyRead()) {
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
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> int delete(T object) throws Exception {
        if (object == null) return 0;
        OrmTable<T> table = (OrmTable<T>) table(object.getClass());
        if (table == null || table.isOnlyRead()) {
            return 0;
        }
        //触发外键删除
        ContentValues contentValues = new ContentValues();
        table.write(this, object, contentValues, SQLiteOpera.DELETE, null);
        WhereBuilder<T> whereBuilder = new WhereBuilder<>(this, table).onlyOrAll(object);
        return delete(table.getType(), whereBuilder);
    }

    public <T> long replace(T object, String... cols) throws Exception {
        if (update(object, cols) > 0) {
            return 1;
        }
        return insert(object);
    }

    public <T> int updateAll(Class<T> pClass, ContentValues contentValues, WhereBuilder<T> whereBuilder)
            throws Exception {
        OrmTable<T> table = table(pClass);
        if (table == null || table.isOnlyRead()) {
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
        if (Orm.DEBUG)
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
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> int update(T object, WhereBuilder<T> whereBuilder, String... cols) throws Exception {
        if (object == null) return -1;
        OrmTable<T> table = (OrmTable<T>) table(object.getClass());
        if (table == null || table.isOnlyRead()) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        table.write(this, object, contentValues, SQLiteOpera.UPDATE,
                (cols == null || cols.length == 0) ? null : Arrays.asList(cols));
        return updateAll(table.getType(), contentValues, whereBuilder);
    }

    @SuppressWarnings("unchecked")
    public <T> int update(T object, String... cols) throws Exception {
        if (object == null) return 0;
        OrmTable<T> table = (OrmTable<T>) table(object.getClass());
        if (table == null || table.isOnlyRead()) {
            return 0;
        }
        WhereBuilder<T> whereBuilder = new WhereBuilder<>(this, table).only(object);
        return update(object, whereBuilder, cols);
    }

    public <T> int insertList(List<T> list) throws Exception {
        if (list == null || list.size() == 0) return 0;
        OrmTable<?> table = table(list.get(0).getClass());
        if (table == null || table.isOnlyRead()) {
            return 0;
        }
        int count = list.size();
        ContentValues[] values = new ContentValues[count];
        for (int i = 0; i < count; i++) {
            T obj = list.get(i);
            values[i] = new ContentValues();
            ContentValues contentValues = values[i];
            table.write(this, obj, contentValues, SQLiteOpera.INSERT, null);
        }
        try {
            helper.insertList(table.getTableUri(), values);
        } catch (Exception e) {
        }
        return 0;
    }

    public <T> long insert(T object) throws Exception {
        if (object == null) return 0;
        OrmTable<?> table = table(object.getClass());
        if (table == null || table.isOnlyRead()) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        table.write(this, object, contentValues, SQLiteOpera.INSERT, null);
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
            throw e;
        }
    }
}
