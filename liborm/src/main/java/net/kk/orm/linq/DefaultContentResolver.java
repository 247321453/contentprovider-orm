package net.kk.orm.linq;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;

import net.kk.orm.api.IContentResolver;

class DefaultContentResolver implements IContentResolver {
    private ContentResolver contentResolver;

    public DefaultContentResolver(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    public DefaultContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
        }
        return null;
    }

    @Override
    public int delete(Uri url, String where, String[] selectionArgs) {
        return contentResolver.delete(url, where, selectionArgs);
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
        contentResolver.unregisterContentObserver(observer);
    }

    @Override
    public boolean registerContentObserver(Uri uri, boolean notifyForDescendents, ContentObserver observer, int userHandle) {
        contentResolver.registerContentObserver(uri, notifyForDescendents, observer);
        return true;
    }
}
