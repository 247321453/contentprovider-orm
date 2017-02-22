package net.kk.orm;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;

public interface IContentResolver {
    Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                 String sortOrder);

    Uri insert(Uri url, ContentValues values);

    int update(Uri uri, ContentValues values,
               String where, String[] selectionArgs);

    Cursor query(final Uri uri, String[] projection,
                 String selection, String[] selectionArgs,
                 String sortOrder, CancellationSignal cancellationSignal);

    int insertList(Uri url, ContentValues[] values);

    int delete(Uri url, String where,
               String[] selectionArgs);

    void unregisterContentObserver(ContentObserver observer);

    boolean registerContentObserver(Uri uri, boolean notifyForDescendents,
                                    ContentObserver observer, int userHandle);
//    SQLiteDatabase getReadableDatabase();
//    SQLiteDatabase getWritableDatabase();
}
