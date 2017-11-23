package net.kk.orm;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.kk.orm.utils.SQLiteUtils.mask;

public class OrmSQLiteOpenHelper extends SQLiteOpenHelper implements ISQLiteHelper {
    private Context context;
    private final Map<String, OrmTable> mTableMap = new HashMap<>();
    private static final String LAST_ID = "#";
    private final List<Class<?>> mTables;
    private String mAuthority;
    private String mDbName;
    private int mDbVersion;
    private Map<String, String> mCacheTableNames = new HashMap<>();
    private static OrmSQLiteOpenHelper sOrmSQLiteOpenHelper;

    public static OrmSQLiteOpenHelper get() {
        return sOrmSQLiteOpenHelper;
    }

    public Context getContext() {
        return context;
    }

    public String getAuthority() {
        return mAuthority;
    }

    public String getDbName() {
        return mDbName;
    }

    public int getDbVersion() {
        return mDbVersion;
    }

    private List<Class<?>> getTables() {
        return mTables;
    }

    public OrmSQLiteOpenHelper(Context context, String name, int version, String authority,
                               List<Class<?>> classes) {
        this(context, name, version, authority, classes, null);
    }

    public OrmSQLiteOpenHelper(Context context, String name, int version, String authority,
                               List<Class<?>> classes,
                               SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, version);
        sOrmSQLiteOpenHelper = this;
        this.context = context;
        mTables = new ArrayList<>();
        if (classes != null) {
            mTables.addAll(classes);
        }
        mAuthority = authority;
        mDbName = name;
        mDbVersion = version;
    }

    public boolean isIdUri(Uri uri) {
        String last = uri.getLastPathSegment();
        if (LAST_ID.equals(last)) {
            return true;
        }
        return false;
    }

    public boolean isSingleProcess(Uri uri) {
        return uri.getAuthority().equals(getAuthority());
    }

    public OrmTable<?> getTable(Uri uri) {
        OrmTable<?> table = mTableMap.get(uri.toString());
        if (table == null) {
            if (Orm.DEBUG) {
                Log.w(Orm.TAG, "get table=" + uri + " tabels=" + mTableMap);
            }
        }
        return table;
    }

    public String getIdColumn(Uri uri) {
        OrmTable<?> table = getTable(uri);
        if (table != null) {
            return table.getNumberKeyName();
        }
        return null;
    }

    public void initClass() {
        List<Class<?>> classes = getTables();
        if (classes != null) {
            if (Orm.DEBUG) {
                Log.d(Orm.TAG, "create table count " + classes.size());
            }
            for (Class<?> c : classes) {
                OrmTable<?> tOrmTable = Orm.table(c);
                mTableMap.put(tOrmTable.getTableUri().toString(), tOrmTable);
                if (!tOrmTable.isOnlyRead()) {
                    if (!TextUtils.isEmpty(tOrmTable.getNumberKeyName())) {
                        mTableMap.put(tOrmTable.getTableUri()
                                        .buildUpon()
                                        .appendEncodedPath(LAST_ID)
                                        .build().toString()
                                , tOrmTable);
                    }
                }
            }
        }
    }

    protected void onCreate(SQLiteDatabase db, OrmTable<?> tOrmTable) {
        if (!tOrmTable.isOnlyRead()) {
            tOrmTable.onCreate(db);
        }
    }

    protected void onUpgrade(SQLiteDatabase db, OrmTable<?> tOrmTable, int oldVersion, int newVersion) {
        if (!tOrmTable.isOnlyRead()) {
            tOrmTable.onUpgrade(db, oldVersion, newVersion);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<Class<?>> classes = getTables();
        if (classes != null) {
            if (Orm.DEBUG) {
                Log.d(Orm.TAG, "create table count " + classes.size());
            }
            for (Class<?> c : classes) {
                OrmTable<?> tOrmTable = Orm.table(c);
                onCreate(db, tOrmTable);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        List<Class<?>> classes = getTables();
        if (classes != null) {
            if (Orm.DEBUG) {
                Log.d(Orm.TAG, "update table count " + classes.size());
            }
            for (Class<?> c : classes) {
                OrmTable<?> tOrmTable = Orm.table(c);
                onUpgrade(db, tOrmTable, oldVersion, newVersion);
            }
        }
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
            mCacheTableNames.put(key, name);
        }
        return name;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            String where = idName + "=" + id;
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
        return cursor;
    }

    private Uri insertInner(Uri uri, ContentValues values) {
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return uri;
        SQLiteDatabase db = getWritableDatabase();
        if (isIdUri(uri)) {
            long id = ContentUris.parseId(uri);
            String idName = getIdColumn(uri);
            values.put(idName, id);
            db.insert(table, null, values);
        } else {
            long id = db.insert(table, null, values);
            uri = ContentUris.withAppendedId(uri, id);
        }
        notifyChange(make(uri, TYPE_INSERT, values));
        return uri;
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

    protected void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return insertInner(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numValues = 0;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            numValues = values.length;
            for (int i = 0; i < numValues; i++) {
                insertInner(uri, values[i]);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return numValues;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) return 0;
        int count;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        notifyChange(make(uri, TYPE_UPDATE, values));
        return count;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) {
            return 0;
        }
        SQLiteDatabase db = getWritableDatabase();
        int count;
        db.beginTransaction();
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        notifyChange(make(uri, TYPE_DELETE, null, selection, selectionArgs));
        return count;
    }
}
