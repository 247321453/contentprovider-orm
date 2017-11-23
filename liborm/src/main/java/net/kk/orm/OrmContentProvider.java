package net.kk.orm;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public abstract class OrmContentProvider extends ContentProvider {
    protected abstract OrmSQLiteOpenHelper createSQLiteOpenHelper(Context context);
    protected OrmSQLiteOpenHelper mOrmSQLiteOpenHelper;
    private Map<String, String> mCacheTabletypes = new HashMap<>();

    @Override
    public boolean onCreate() {
        if (OrmSQLiteOpenHelper.get() != null) {
            mOrmSQLiteOpenHelper = OrmSQLiteOpenHelper.get();
        } else {
            mOrmSQLiteOpenHelper = createSQLiteOpenHelper(getContext());
            mOrmSQLiteOpenHelper.initClass();
        }
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

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        if (uri == null || !checkRead(uri)) return null;
        return mOrmSQLiteOpenHelper.query(uri, columns, selection, selectionArgs, sortOrder);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (uri == null || !checkWrite(uri)) return 0;
        return mOrmSQLiteOpenHelper.bulkInsert(uri, values);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uri == null || !checkWrite(uri)) return null;
        return mOrmSQLiteOpenHelper.insert(uri, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri == null || !checkWrite(uri)) return 0;
        return mOrmSQLiteOpenHelper.delete(uri, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uri == null || !checkWrite(uri)) return 0;
        return mOrmSQLiteOpenHelper.update(uri, values, selection, selectionArgs);
    }
}
