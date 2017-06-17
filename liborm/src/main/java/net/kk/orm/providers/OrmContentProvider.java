package net.kk.orm.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import net.kk.orm.Orm;
import net.kk.orm.OrmTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static net.kk.orm.utils.SQLiteUtils.mask;

public abstract class OrmContentProvider extends ContentProvider {

    protected abstract OrmSQLiteOpenHelper getSQLiteOpenHelper(Context context);

    protected OrmSQLiteOpenHelper mOrmSQLiteOpenHelper;
    private Map<String, String> mCacheTableNames = new HashMap<>();
    private Map<String, String> mCacheTabletypes = new HashMap<>();
    private Map<String, String> mCacheTableIds = new HashMap<>();

    @Override
    public boolean onCreate() {
        mOrmSQLiteOpenHelper = getSQLiteOpenHelper(getContext());
        mOrmSQLiteOpenHelper.initClass();
        return true;
    }

    protected boolean checkRead(Uri uri) {
        return true;
    }

    protected boolean checkWrite(Uri uri) {
        return true;
    }

    protected String makeType(String table, boolean isId) {
        if (isId) {
            return "vnd.android.cursor.dir/orm.table." + table;
        }
        return "vnd.android.cursor.dir/orm.table." + table + ".id";
    }

    protected boolean isIdUri(Uri uri) {
        return mOrmSQLiteOpenHelper.isIdUri(uri);
    }

    protected String getIdColumn(Uri uri) {
        String key = uri.toString();
        String id = mCacheTableIds.get(key);
        if (id == null) {
            id = mOrmSQLiteOpenHelper.getIdColumn(uri);
            mCacheTableIds.put(key, id);
        }
        return id;
    }

    @Override
    public String getType(Uri uri) {
        String key = uri.toString();
        String type = mCacheTabletypes.get(key);
        if (type == null) {
            OrmTable<?> table = getTable(uri);
            if (table != null) {
                type = makeType(table.getTableType(), isIdUri(uri));
            } else {
                type = makeType("unknown", false);
            }
            mCacheTabletypes.put(key, type);
        }
        return type;

    }

    @SuppressWarnings("unchecked")
    public OrmTable<?> getTable(Uri uri) {
        OrmTable<?> table = mOrmSQLiteOpenHelper.getTable(uri);
        if (table == null) {
            return null;
        }
        return table;
    }

    public String getTableName(Uri uri) {
        String key = uri.toString();
        String name = mCacheTableNames.get(key);
        if (name == null) {
            OrmTable<?> table = getTable(uri);
            if (table == null) {
                mCacheTableNames.put(key, "");
                return null;
            }
            if (table.isOnlyRead()) {
                name = table.getTableName();
            } else {
                name = mask(table.getTableName());
            }
//            Log.d("orm", table.getTableName()+" mask "+name);
            mCacheTableNames.put(key, name);
        }
        return name;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        if (uri == null || !checkRead(uri)) return null;
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return null;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            String where = idName + "=" + id;// 获取指定id的记录
            where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";
            try {
                cursor = db.query(table, columns, where, selectionArgs, null, null, sortOrder);
            } catch (Exception e) {
                Log.e(Orm.TAG, "query " + uri, e);
            }
        } else {
            try {
                if (Orm.DEBUG)
                    Log.d(Orm.TAG, "select " + Arrays.toString(columns) + " from " + table
                            + " where " + selection + " " + Arrays.toString(selectionArgs) + " order by " + sortOrder);
                cursor = db.query(table, columns, selection, selectionArgs, null, null, sortOrder);
            } catch (Exception e) {
                Log.e(Orm.TAG, "query " + uri, e);
            }
        }
//        if (cursor != null) {
//            cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        }
        return cursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (uri == null || !checkWrite(uri)) return 0;
        int numValues = 0;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction(); //开始事务
        try {
            //数据库操作
            numValues = values.length;
            for (int i = 0; i < numValues; i++) {
                insertInner(uri, values[i]);
            }
            db.setTransactionSuccessful(); //别忘了这句 Commit
        } finally {
            db.endTransaction(); //结束事务
        }
        return numValues;
    }

    private Uri insertInner(Uri uri, ContentValues values) {
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return uri;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getWritableDatabase();
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            values.put(idName, id);
            db.insert(table, null, values);
        } else {
            long id = db.insert(table, null, values);
            uri = ContentUris.withAppendedId(uri, id);
        }
        getContext().getContentResolver().notifyChange(make(uri, TYPE_INSERT, values), null);
        return uri;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uri == null || !checkWrite(uri)) return null;
        return insertInner(uri, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri == null || !checkWrite(uri)) return 0;
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) {
            return 0;
        }
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getWritableDatabase();
        int count;
        db.beginTransaction(); //开始事务
        try {
            if (isIdUri(uri)) {
                long id = ContentUris.parseId(uri);
                String idName = getIdColumn(uri);
                String where = idName + "=" + id;
                where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";
                count = db.delete(table, where, selectionArgs);
            } else {
                count = db.delete(table, selection, selectionArgs);
            }
            db.setTransactionSuccessful(); //别忘了这句 Commit
        } finally {
            db.endTransaction(); //结束事务
        }
        getContext().getContentResolver().notifyChange(make(uri, TYPE_DELETE, null, selection, selectionArgs), null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uri == null || !checkWrite(uri)) return 0;
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return 0;
        int count;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction(); //开始事务
        try {
            if (isIdUri(uri)) {
                long id = ContentUris.parseId(uri);
                String idName = getIdColumn(uri);
                String where = idName + "=" + id;
                where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";
                count = db.update(table, values, where, selectionArgs);
            } else {
                count = db.update(table, values, selection, selectionArgs);
            }
            db.setTransactionSuccessful(); //别忘了这句 Commit
        } finally {
            db.endTransaction(); //结束事务
        }
        getContext().getContentResolver().notifyChange(make(uri, TYPE_UPDATE, values), null);
        return count;
    }

    public static final int TYPE_INSERT = 0;
    public static final int TYPE_DELETE = 1;
    public static final int TYPE_UPDATE = 2;
    public static final String QUERY_TYPE = "orm_type";

    private Uri make(Uri uri, int type, ContentValues contentValues) {
        return make(uri, type, contentValues, null, null);
    }

    protected Uri make(Uri uri, int type, ContentValues contentValues, String selection, String[] selectionArgs) {
        return uri.buildUpon().appendQueryParameter(QUERY_TYPE, "" + type).build();
    }
}
