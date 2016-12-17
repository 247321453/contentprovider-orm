package net.kk.orm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

import static net.kk.orm.SQLiteUtils.mask;

public abstract class OrmContentProvider extends ContentProvider {

    protected abstract OrmSQLiteOpenHelper getSQLiteOpenHelper(Context context);

    protected OrmSQLiteOpenHelper mOrmSQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        mOrmSQLiteOpenHelper = getSQLiteOpenHelper(getContext());
        mOrmSQLiteOpenHelper.initClass();
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
        return mOrmSQLiteOpenHelper.getIdColumn(uri);
    }

    @Override
    public String getType(Uri uri) {
        OrmTable<?> table = getTable(uri);
        if (table != null) {
            return makeType(table.getTableType(), isIdUri(uri));
        }
        return makeType("unknown", false);
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
        OrmTable<?> table = mOrmSQLiteOpenHelper.getTable(uri);
        if (table == null) {
            return null;
        }
        if (!table.isOnlyRead()) {
            return mask(table.getTableName());
        }
        return table.getTableName();
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        if (uri == null) return null;
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return null;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getReadableDatabase();
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            String where = idName + "=" + id;// 获取指定id的记录
            where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";
            try {
                return db.query(table, columns, where, selectionArgs, null, null, sortOrder);
            } catch (Exception e) {
                Log.e(Orm.TAG, "query " + uri, e);
            }
        } else {
            try {
                return db.query(table, columns, selection, selectionArgs, null, null, sortOrder);
            } catch (Exception e) {
                Log.e(Orm.TAG, "query " + uri, e);
            }
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uri == null) return null;
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return uri;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getWritableDatabase();
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            values.put(idName, id);
            db.insert(table, null, values);
            return uri;
        } else {
            long id = db.insert(table, null, values);
            return ContentUris.withAppendedId(uri, id);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri == null) return 0;
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return 0;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getWritableDatabase();
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            String where = idName + "=" + id;
            where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";
            return db.delete(table, where, selectionArgs);
        } else {
            return db.delete(table, selection, selectionArgs);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uri == null) return 0;
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return 0;
        SQLiteDatabase db = mOrmSQLiteOpenHelper.getWritableDatabase();
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            String where = idName + "=" + id;
            where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";
            return db.update(table, values, where, selectionArgs);
        } else {
            return db.update(table, values, selection, selectionArgs);
        }
    }
}
