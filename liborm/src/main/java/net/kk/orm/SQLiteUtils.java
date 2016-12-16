package net.kk.orm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class SQLiteUtils {
    public static boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c table  sqlite_master where type ='table' and name ='" + mask(tableName.trim()) + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
    public static String mask(String name){
        if(name.startsWith("\"")&&name.endsWith("\"")){
            return name;
        }
        return "\""+name.trim()+"\"";
    }
}
