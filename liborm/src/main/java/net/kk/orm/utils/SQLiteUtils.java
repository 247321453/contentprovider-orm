package net.kk.orm.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.kk.orm.api.Orm;

import java.util.Locale;

public class SQLiteUtils {
    public static boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            Log.w(Orm.TAG, "check table " + tableName, e);
        }
        return result;
    }

    public static String mask(String name) {
        String chkname = name.toLowerCase(Locale.US);
        if (name.startsWith("\"") && name.endsWith("\"")) {
            return name;
        }
        if (chkname.contains(" as") || chkname.contains(" join")) {
            return name;
        }
        name = name.trim();
        if (name.contains(",") || name.contains(".")) {
            if (!name.contains("\",\"")) {
                name = name.replace(",", "\",\"");
            }
            if (!name.contains("\".\"")) {
                name = name.replace(".", "\".\"");
            }
            return "\"" + name + "\"";
        }
        return "\"" + name.trim() + "\"";
    }
}
