package net.kk.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OrmSQLiteOpenHelper extends SQLiteOpenHelper {
    protected Context context;
    private Map<String, OrmTable> mTableMap = new HashMap<>();

    //
    protected abstract List<Class<?>> getTables();

    public abstract IContentResolver getContentResolver();

    private static final String LAST_ID = "#";

    public OrmSQLiteOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public OrmSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public boolean isIdUri(Uri uri) {
        String last = uri.getLastPathSegment();
        if (LAST_ID.equals(last)) {
            return true;
        }
        return false;
    }

    public OrmTable<?> getTable(Uri uri) {
        OrmTable<?> table = mTableMap.get(uri.toString());
        if (table == null) {
            if (Orm.DEBUG) {
                Log.w(Orm.TAG, "get table=" + uri + " tabels=" + mTableMap);
            }
//        } else {
//            Log.d(Orm.TAG, "get table=" + uri + " " + table);
        }
        return table;
    }

    public String getIdColumn(Uri uri) {
        OrmTable<?> table = getTable(uri);
        if (table != null) {
            OrmColumn column = table.findNumberKey();
            if (column != null) {
                return column.getColumnName();
            }
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
                    OrmColumn column = tOrmTable.findNumberKey();
                    if (column != null) {
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<Class<?>> classes = getTables();
        if (classes != null) {
            if (Orm.DEBUG) {
                Log.d(Orm.TAG, "create table count " + classes.size());
            }
            for (Class<?> c : classes) {
                OrmTable<?> tOrmTable = Orm.table(c);
                if (!tOrmTable.isOnlyRead()) {
                    tOrmTable.onCreate(db);
                }
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
                if (!tOrmTable.isOnlyRead()) {
                    tOrmTable.onUpgrade(db, oldVersion, newVersion);
                }
            }
        }
    }
}
