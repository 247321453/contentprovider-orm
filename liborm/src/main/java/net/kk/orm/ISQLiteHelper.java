package net.kk.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface ISQLiteHelper {
    Cursor query(Uri uri, String[] cols, String selection, String[] selectionArgs,
                 String sortOrder);

    Uri insert(Uri uri, ContentValues values);

    int update(Uri uri, ContentValues values,
               String where, String[] selectionArgs);

    int bulkInsert(Uri uri, ContentValues[] values);

    int delete(Uri uri, String where,
               String[] selectionArgs);

}
