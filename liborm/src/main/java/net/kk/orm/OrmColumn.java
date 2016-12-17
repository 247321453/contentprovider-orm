package net.kk.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;


import net.kk.orm.annotations.Column;
import net.kk.orm.converts.IConvert;
import net.kk.orm.converts.JsonTextConvert;
import net.kk.orm.converts.TypeConverts;

import java.lang.reflect.Field;

class OrmColumn extends IOrm {
    private Column mColumn;
    private Field mField;
    private final Class<?> pClass;
    private final Class<?> mRClass;
    private IConvert<?, ?> mConvert;
    private String mDefaultText;

    OrmColumn(Field field) {
        pClass = field.getType();
        mRClass = TypeConverts.wrapper(pClass);
        this.mField = field;
        mColumn = field.getAnnotation(Column.class);
        if (mColumn == null) {
            mColumn = new DefaultColumn(field);
        }
        if (mColumn.convert().equals(IConvert.class)) {
            mConvert = TypeConverts.get().find(pClass);
        } else {
            try {
                mConvert = mColumn.convert().newInstance();
            } catch (Exception e) {
                if (Orm.DEBUG) {
                    Log.w(Orm.TAG, "create convert " + mColumn.convert());
                }
            }
        }
        if (mConvert == null) {
            if (Orm.DEBUG) {
                Log.w(Orm.TAG, "no find convert " + mRClass);
            }
            mConvert = new JsonTextConvert<>(pClass, Orm.getJsonConvert());
        }
    }

    public Column getColumn() {
        return mColumn;
    }

    public Class<?> getType() {
        return pClass;
    }

    public boolean isNumberField() {
        return mRClass == Integer.class
                || mRClass == Long.class;
    }

    //
    public SQLiteType getSQLiteType() {
        return mConvert.getSQLiteType();
    }

    public boolean isAutoIncrement() {
        return mColumn.autoIncrement();
    }

    @Override
    public String toString() {
        return "OrmColumn{" +
                "name=" + getColumnName() +
                ", Field=" + mField +
                ", pClass=" + pClass +
                '}';
    }

    public String getDefaultValue() {
        if (TextUtils.isEmpty(mDefaultText)) {
            mDefaultText = mColumn.defaultValue();
            if (mDefaultText == null || "___NULL".equals(mDefaultText)) {
                mDefaultText = "";
            } else if (getSQLiteType() == SQLiteType.TEXT) {
                if (mDefaultText.startsWith("'") && mDefaultText.endsWith("'")) {

                } else {
                    mDefaultText = "'" + mDefaultText.replace("'", "''") + "'";
                }
            }
        }
        return mDefaultText;
    }

    public String getColumnType() {
        return String.valueOf(getSQLiteType());
    }

    public boolean isPrimaryKey() {
        return mColumn.primaryKey() || mColumn.autoIncrement();
    }

    public String getColumnName() {
        if (TextUtils.isEmpty(mColumn.value())) {
            return mField.getName();
        }
        return mColumn.value().trim();
    }

    public Object toDbValue(Orm orm, Object val) {
        if (val != null) {
            final SQLiteType type = getSQLiteType();
            try {
                IConvert iConvert = mConvert;
                val = iConvert.toDbValue(orm, val);
            } catch (Exception e) {
                if (Orm.DEBUG) {
                    Log.w(Orm.TAG, "convert:" + val, e);
                }
            }
        }
        if (Orm.DEBUG) {
            Log.d(Orm.TAG, "convert:" + val);
        }
        return val;
    }

    public Object getDbValueByParent(Orm orm, Object parent) {
        Object val = null;
        try {
            val = mField.get(parent);
        } catch (IllegalAccessException e) {
        }
        return toDbValue(orm, val);
    }

    public Object getValue(Object parent) {
        Object val = null;
        try {
            val = mField.get(parent);
        } catch (IllegalAccessException e) {
        }
        return val;
    }

    public void write(Orm orm, Object parent, ContentValues contentValues) {
        final SQLiteType type = getSQLiteType();
        final Object val = getDbValueByParent(orm, parent);
        final String name = getColumnName();
        try {
            switch (type) {
                case INTEGER:
                    contentValues.put(name, (Integer) val);
                    break;
                case LONG:
                    contentValues.put(name, (Long) val);
                    break;
                case DOUBLE:
                    contentValues.put(name, (Double) val);
                    break;
                case FLOAT:
                    contentValues.put(name, (Float) val);
                    break;
                case BLOB:
                    contentValues.put(name, (byte[]) val);
                    break;
                case TEXT:
                default:
                    contentValues.put(name, (String) val);
                    break;
            }
        } catch (Exception e) {
        }
    }

    public Object read(Orm orm,Cursor cursor) {
        int index = cursor.getColumnIndex(getColumnName());
        if (index < 0) {
            if (Orm.DEBUG) {
                Log.w(Orm.TAG, "no find column " + getColumnName());
            }
            return null;
        }
        Object obj = null;
        SQLiteType type = getSQLiteType();
        switch (type) {
            case INTEGER:
                obj = cursor.getInt(index);
                break;
            case LONG:
                obj = cursor.getLong(index);
                break;
            case DOUBLE:
                obj = cursor.getDouble(index);
                break;
            case FLOAT:
                obj = cursor.getFloat(index);
                break;
            case BLOB:
                obj = cursor.getBlob(index);
                break;
            case TEXT:
            default:
                obj = cursor.getString(index);
                break;
        }
        if (obj == null) {
            return null;
        }
        return ((IConvert) mConvert).toValue(orm, obj);
    }

    public void setValue(Object o, Object value) {
        try {
            mField.set(o, value);
        } catch (Exception e) {
            if (Orm.DEBUG) {
                Log.w(Orm.TAG, "set " + mField.getName() + "=" + value);
            }
        }
    }

    public void setId(Object o, long id) {
        if (mRClass == Integer.class) {
            setValue(o, (int) id);
        } else {
            setValue(o, id);
        }
    }
}
