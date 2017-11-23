package net.kk.orm;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;

class ContentSQLHelper implements ISQLiteHelper {
    private ContentResolver contentResolver;

    public ContentSQLHelper(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public int bulkInsert(Uri url, ContentValues[] values) {
        return contentResolver.bulkInsert(url, values);
    }

    @Override
    public Uri insert(Uri url, ContentValues values) {
        return contentResolver.insert(url, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {
        return contentResolver.update(uri, values, where, selectionArgs);
    }

    @Override
    public int delete(Uri url, String where, String[] selectionArgs) {
        return contentResolver.delete(url, where, selectionArgs);
    }
}
