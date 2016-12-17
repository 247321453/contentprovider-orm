package net.kk.orm;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.kk.orm.SQLiteUtils.mask;

public class OrmSelector<T> {
    private OrmTable<T> mTable;
    private IContentResolver helper;
    private String mLimit;
    private String mOrderBy;
    private WhereBuilder whereBuilder;
    //    private String mHaving;
    private String mOffest;
    private String sql;
    private int offset = -1;
    private Orm orm;

    OrmSelector(Orm orm, Class<T> pClass) {
        mTable = Orm.table(pClass);
        this.orm = orm;
        this.helper = orm.getContentResolver();
    }

    public OrmSelector<T> limit(int count) {
        return limit(0, count);
    }

    public OrmSelector<T> limit(int start, int count) {
        mLimit = " limit " + start + "," + count;
        return this;
    }

    public OrmSelector<T> offest(String offest) {
        mOffest = offest;
        return this;
    }

    public OrmSelector<T> orderBy(String name) {
        return orderBy(name, false);
    }
//
//    public OrmSelector<T> where(WhereBuilder whereBuilder) {
//        this.whereBuilder = whereBuilder;
//        return this;
//    }

    public OrmSelector<T> where(String columnName, String op, Object value) {
        return and(columnName, op, value);
    }

    public T findById(Object id) {
        OrmColumn col = mTable.findKey();
        if (col == null) {
            return null;
        }
        return where(col.getColumnName(), "=", id).findFirst();
    }

    public OrmSelector<T> and(String columnName, String op, Object value) {
        if (this.whereBuilder == null) {
            this.whereBuilder = new WhereBuilder<>(orm, mTable);
        }
        whereBuilder.and(columnName, op, value);
        return this;
    }

    public OrmSelector<T> in(String columnName, List<T> values) {
        if (this.whereBuilder == null) {
            this.whereBuilder = new WhereBuilder<>(orm, mTable);
        }
        whereBuilder.in(columnName, values);
        return this;
    }

    public OrmSelector<T> between(String columnName, T start, T end) {
        if (this.whereBuilder == null) {
            this.whereBuilder = new WhereBuilder<>(orm, mTable);
        }
        whereBuilder.between(columnName, start, end);
        return this;
    }

    public OrmSelector<T> or(String columnName, String op, Object value) {
        if (this.whereBuilder == null) {
            this.whereBuilder = new WhereBuilder<>(orm, mTable);
        }
        whereBuilder.or(columnName, op, value);
        return this;
    }

    public OrmSelector<T> orderBy(String name, boolean desc) {
        String key;
        if (desc) {
            key = name + " desc";
        } else {
            key = name + " asc";
        }
        if (TextUtils.isEmpty(mOrderBy)) {
            mOrderBy = key;
        } else {
            mOrderBy += "," + key;
        }
        return this;
    }

    public T findFirst() {
        Cursor cursor = queryAll(mTable.getAllColumns());
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                T t = mTable.read(orm, cursor);
                return t;
            }
        }
        return null;
    }

    public List<T> findAll() {
        Cursor cursor = queryAll(mTable.getAllColumns());
        if (cursor != null) {
            List<T> list = new ArrayList<T>();
            if (cursor.moveToFirst()) {
                do {
                    T t = mTable.read(orm, cursor);
                    list.add(t);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return list;
        } else {
            return null;
        }
    }

    public int count() {
        int count = 0;
        Cursor cursor = queryAll(new String[]{"count(*) as c"});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                try {
                    count = cursor.getInt(cursor.getColumnIndex("c"));
                } catch (Exception e) {

                }
            }
        }
        return count;
    }

    private String getSortString() {
        StringBuilder exstring = new StringBuilder();
        if (!TextUtils.isEmpty(mOrderBy)) {
            exstring.append(mOrderBy);
        } else {
            List<OrmColumn> columns = mTable.getKeyColumns();
            if (columns == null) {
                return null;
            }
            int count = 0;
            for (OrmColumn column : columns) {
                if (count > 0) {
                    exstring.append(",");
                }
                exstring.append(column.getColumnName());
                count++;
            }
        }
        if (!TextUtils.isEmpty(mLimit)) {
            exstring.append(mLimit);
        }
        if (offset >= 0) {
            exstring.append("OFFSET ").append(offset);
        }
        return exstring.toString();
    }

    private Cursor queryAll(String[] cols) {
        try {
            if (whereBuilder != null) {
                return helper.query(mTable.getTableUri(), cols, whereBuilder.getWhereString(),
                        whereBuilder.getWhereItems(), getSortString());
            }
            return helper.query(mTable.getTableUri(), cols, null, null, getSortString());
        } catch (Exception e) {
            Log.e(Orm.TAG, "query", e);
            return null;
        }
    }
}
