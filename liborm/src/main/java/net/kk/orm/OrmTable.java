package net.kk.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.kk.orm.SQLiteUtils.mask;

class OrmTable<T> extends IOrm {
    private final List<String> mColumNames = new ArrayList<>();
    private final List<OrmColumn> mColums = new ArrayList<>();
    private final List<OrmColumn> mkeyColums = new ArrayList<>();
    private Table mTable;
    private Uri mUri;
    private Class<T> mTClass;

    OrmTable(Class<T> pClass) {
        mTClass = pClass;
        mTable = pClass.getAnnotation(Table.class);
        if (mTable == null) {
            mTable = new DefaultTable(pClass);
        }
        if (!TextUtils.isEmpty(mTable.uri())) {
            mUri = Uri.parse(mTable.uri());
        }
        findAllFields(pClass);
    }

    public OrmColumn getColumn(String name) {
        for (OrmColumn column : mkeyColums) {
            if (column.getColumnName().equals(name)) {
                return column;
            }
        }
        for (OrmColumn column : mColums) {
            if (column.getColumnName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    public String[] getAllColumns() {
        String[] names = new String[mkeyColums.size() + mColums.size()];
        int i = 0;
        for (OrmColumn column : mkeyColums) {
            names[i++] = column.getColumnName();
        }
        for (OrmColumn column : mColums) {
            names[i++] = column.getColumnName();
        }
        return names;
    }

    public Class<T> getType() {
        return mTClass;
    }

    public Uri getTableUri() {
        return mUri;
    }

    public T read(Cursor cursor) {
        T t = create(getType());
        if (t != null) {
            for (OrmColumn column : mkeyColums) {
                Object val = column.read(cursor);
                column.setValue(t, val);
            }
            for (OrmColumn column : mColums) {
                Object val = column.read(cursor);
                column.setValue(t, val);
            }
        }
        return t;
    }

    @Override
    public String toString() {
        return "OrmTable{" +
                "name=" + getTableName() +
                ", uri=" + mUri +
                ", class=" + mTClass +
                '}';
    }

    public String write(Object t, ContentValues contentValues, boolean isupdate, List<String> cols) {
        if (t == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (OrmColumn column : mkeyColums) {
            //自增的不写入
            if (column.isAutoIncrement()) {
                continue;
            }
            if (isupdate) {
                if (column.isPrimaryKey()) {
                    continue;
                }
            }
            String key = column.getColumnName();
            if (cols != null) {
                if (!cols.contains(key)) {
                    continue;
                }
            }
            column.write(t, contentValues);
            if (count > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(mask(key));
            count++;
        }
        for (OrmColumn column : mColums) {
            //自增的不写入
            if (column.isAutoIncrement()) {
                continue;
            }
            String key = column.getColumnName();
            if (cols != null) {
                if (!cols.contains(key)) {
                    continue;
                }
            }
            column.write(t, contentValues);
            if (count > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(mask(key));
            count++;
        }
        return stringBuilder.toString();
    }

    public String getTableName() {
        return mTable.value();
    }

    public void onCreate(SQLiteDatabase db) {
        if (SQLiteUtils.tabbleIsExist(db, getTableName())) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("create table ");
        builder.append(mask(getTableName()));
        builder.append(" (");
        //自增主键
        //复合主键

        if (mkeyColums.size() == 1) {
            OrmColumn column = mkeyColums.get(0);
            if (column.isAutoIncrement()) {
                builder.append(mask(column.getColumnName()))
                        .append(" INTEGER PRIMARY KEY autoincrement");
            } else {
                builder.append(mask(column.getColumnName()))
                        .append(" ")
                        .append(column.getColumnType())
                        .append(" PRIMARY KEY");
            }
            if (!TextUtils.isEmpty(column.getDefaultValue())) {
                builder.append(" default ")
                        .append(" ")
                        .append(column.getDefaultValue());
            }
            builder.append(",");
        } else {
            int kindex = 0;
            for (OrmColumn column : mkeyColums) {
                if (kindex > 0) {
                    builder.append(",");
                }
                builder.append(mask(column.getColumnName()));
                builder.append(" ");
                builder.append(column.getColumnType());
                if (!TextUtils.isEmpty(column.getDefaultValue())) {
                    builder.append(" default ")
                            .append(" ")
                            .append(column.getDefaultValue());
                }
                kindex++;
            }
        }
        int builders = 0;
        for (OrmColumn column : mColums) {
            if (builders > 0) {
                builder.append(",");
            }
            builder.append(mask(column.getColumnName()));
            builder.append(" ");
            builder.append(column.getColumnType());
            if (!TextUtils.isEmpty(column.getDefaultValue())) {
                builder.append(" default ")
                        .append(" ")
                        .append(column.getDefaultValue());
            }
            builders++;
        }
        if (mkeyColums.size() > 1) {
            //复合主键？
            builder.append(", PRIMARY KEY (");
            int kindex = 0;
            for (OrmColumn column : mkeyColums) {
                if (kindex > 0) {
                    builder.append(",");
                }
                builder.append(mask(column.getColumnName()));
                kindex++;
            }
            builder.append(")");
        }
        builder.append(") ");
        builder.append(mTable.createSql());
        builder.append(";");
        String sql = builder.toString();
        try {
//            Log.d(Orm.TAG, "create : " + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            Log.e(Orm.TAG, "create : " + sql, e);
            throw e;
        }
        if (Orm.DEBUG) {
            Log.d(Orm.TAG, "create table " + sql);
        }
    }

    public OrmColumn findNumberKey() {
        for (OrmColumn column : mkeyColums) {
            if (column.isPrimaryKey() && column.isNumberField()) {
                return column;
            }
        }
        return null;
    }

    public OrmColumn findKey() {
        return mkeyColums.size() > 0 ? mkeyColums.get(0) : null;
    }

    public List<OrmColumn> getKeyColumns() {
        return mkeyColums;
    }

    public OrmColumn findAuto() {
        for (OrmColumn column : mkeyColums) {
            if (column.isAutoIncrement()) {
                return column;
            }
        }
        return null;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //检查字段
        if (SQLiteUtils.tabbleIsExist(db, getTableName())) ;
        Cursor cursor = db.rawQuery("select * from " + mask(getTableName()) + " limit 1;", null);
        if (cursor != null) {
            List<String> cs = Arrays.asList(cursor.getColumnNames());
            cursor.close();
            for (OrmColumn column : mColums) {
                if (!cs.contains(column.getColumnName())) {
                    addColumn(db, column);
                }
            }
        }
    }

    private void addColumn(SQLiteDatabase db, OrmColumn column) {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE ")
                .append(mask(getTableName()))
                .append(" ADD COLUMN ")
                .append(mask(column.getColumnName()))
                .append(" ")
                .append(column.getColumnType());
        if (!TextUtils.isEmpty(column.getDefaultValue())) {
            builder.append(" default ")
                    .append(" ")
                    .append(column.getDefaultValue());
        }
        String sql = builder.toString();
        if (Orm.DEBUG) {
            Log.d(Orm.TAG, "alter table " + sql);
        }
        db.execSQL(sql);
    }

    private boolean enableField(Field field) {
        //忽略静态变量
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }
        if (Modifier.isTransient(field.getModifiers())) {
            return false;
        }
        //主动忽略
//        if (field.getAnnotation(Column.class) != null) {
//            return true;
//        }
        return true;
    }

    private void addField(Field field) {
        OrmColumn column = new OrmColumn(field);
        if (mColumNames.contains(column.getColumnName())) {
            //字段已经存在
            Log.w(Orm.TAG, "column is readly exist."+column);
            return;
        }
        mColumNames.add(column.getColumnName());
        if (column.isPrimaryKey()) {
            mkeyColums.add(column);
        } else {
            mColums.add(column);
        }
    }

    private void findAllFields(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                if (enableField(f)) {
                    f.setAccessible(true);
                    addField(f);
                }
            }
        }
        if (fields != null) {
            fields = cls.getFields();
            for (Field f : fields) {
                if (enableField(f)) {
                    addField(f);
                }
            }
        }
        Class<?> sup = cls.getSuperclass();
        if (sup != null && sup != Object.class) {
            findAllFields(sup);
        }
    }
}
