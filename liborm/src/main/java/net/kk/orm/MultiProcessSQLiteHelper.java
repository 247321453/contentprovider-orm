package net.kk.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

class MultiProcessSQLiteHelper implements ISQLiteHelper {
    private ISQLiteHelper mBase;

    public MultiProcessSQLiteHelper(ISQLiteHelper helper) {
        mBase = helper;
    }

    private boolean isSingleProcess(Uri uri) {
        if (OrmSQLiteOpenHelper.get() == null) {
            return false;
        }
        return OrmSQLiteOpenHelper.get().isSingleProcess(uri);
    }

    @Override
    public Cursor query(Uri uri, String[] cols, String selection, String[] selectionArgs, String sortOrder) {
        if (isSingleProcess(uri)) {
            return OrmSQLiteOpenHelper.get().query(uri, cols, selection, selectionArgs, sortOrder);
        }
        return mBase.query(uri, cols, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (isSingleProcess(uri)) {
            return OrmSQLiteOpenHelper.get().insert(uri, values);
        }
        return mBase.insert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {
        if (isSingleProcess(uri)) {
            return OrmSQLiteOpenHelper.get().update(uri, values, where, selectionArgs);
        }
        return mBase.update(uri, values, where, selectionArgs);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (isSingleProcess(uri)) {
            return OrmSQLiteOpenHelper.get().bulkInsert(uri, values);
        }
       return mBase.bulkInsert(uri, values);
    }

    @Override
    public int delete(Uri uri, String where, String[] selectionArgs) {
        if (isSingleProcess(uri)) {
            return OrmSQLiteOpenHelper.get().delete(uri, where, selectionArgs);
        }
        return mBase.delete(uri, where, selectionArgs);
    }
}
