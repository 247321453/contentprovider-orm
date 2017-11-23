package net.kk.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MultiProcessSQLiteHelper implements ISQLiteHelper {
    private ISQLiteHelper mBase;

    public MultiProcessSQLiteHelper(ISQLiteHelper helper) {
        mBase = helper;
    }

    protected boolean isSingleProcess(Uri uri) {
        OrmSQLiteOpenHelper helper = get(uri);
        //根据table区分？
        return helper != null;
    }

    protected OrmSQLiteOpenHelper get(Uri uri) {
        return OrmSQLiteOpenHelper.get(uri.getAuthority());
    }

    @Override
    public Cursor query(Uri uri, String[] cols, String selection, String[] selectionArgs, String sortOrder) {
        if (isSingleProcess(uri)) {
            return get(uri).query(uri, cols, selection, selectionArgs, sortOrder);
        }
        return mBase.query(uri, cols, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (isSingleProcess(uri)) {
            return get(uri).insert(uri, values);
        }
        return mBase.insert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {
        if (isSingleProcess(uri)) {
            return get(uri).update(uri, values, where, selectionArgs);
        }
        return mBase.update(uri, values, where, selectionArgs);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (isSingleProcess(uri)) {
            return get(uri).bulkInsert(uri, values);
        }
        return mBase.bulkInsert(uri, values);
    }

    @Override
    public int delete(Uri uri, String where, String[] selectionArgs) {
        if (isSingleProcess(uri)) {
            return get(uri).delete(uri, where, selectionArgs);
        }
        return mBase.delete(uri, where, selectionArgs);
    }
}
