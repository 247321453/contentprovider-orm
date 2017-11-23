package net.kk.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.ColumnConvert;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Union;
import net.kk.orm.converts.IConvert;
import net.kk.orm.converts.TypeConverts;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;
import net.kk.orm.utils.SQLiteUtils;

import java.lang.reflect.Field;

/**
 * @hide
 */
class OrmColumn extends IOrmBase {
    private Column mColumn;
    private Union mUnion;
    private PrimaryKey mPrimaryKey;
    private Field mField;
    private final Class<?> pClass;
    private final Class<?> mRClass;
    private IConvert<?, ?> mConvert;
    private String mDefaultText;
    private String mColumnName;

    OrmColumn(Field field) {
        pClass = field.getType();
        mRClass = TypeConverts.wrapper(pClass);
        this.mField = field;
        mColumn = field.getAnnotation(Column.class);
        if (mColumn == null) {
            mColumn = new DefaultColumn(field);
        }
        mUnion = field.getAnnotation(Union.class);
        mPrimaryKey = field.getAnnotation(PrimaryKey.class);
        ColumnConvert columnConvert = field.getAnnotation(ColumnConvert.class);
        if (columnConvert == null || columnConvert.value().equals(IConvert.class)) {
            mConvert = TypeConverts.get().find(mRClass, mColumn);
            if (mConvert == null) {
                mConvert = createUniconKeyConvert(mRClass, mColumn);
            }
        } else {
            try {
                mConvert = columnConvert.value().newInstance();
            } catch (Exception e) {
                if (Orm.DEBUG) {
                    Log.w(Orm.TAG, "create convert " + columnConvert.value());
                }
            }
        }
        if (mConvert == null) {
            if (Parcelable.class.isAssignableFrom(pClass)) {
                mConvert = new ParcelableConvert<>(pClass);
            }
        }
    }

    private IConvert<?, ?> createUniconKeyConvert(Class<?> type, Column column) {
        IConvert<?, ?> typeConvert = null;
        String col = null;
        OrmTable<?> tOrmTable = Orm.table(type);
        //主键
        OrmColumn ormColumn = null;
        if (mUnion == null || TextUtils.isEmpty(mUnion.key())) {
            ormColumn = tOrmTable.findKey();
            if (ormColumn != null) {
                col = ormColumn.getColumnName();
            }
        } else {
            col = column.value();
            ormColumn = tOrmTable.getColumn(col);
        }
        if (col != null && ormColumn != null) {
            String key2 = type.getName() + "@" + col;
            typeConvert = TypeConverts.get().getUniconKey(key2);
            if (typeConvert == null) {
                if (tOrmTable.getKeyColumns() != null && tOrmTable.getKeyColumns().size() > 1) {
                    //TODO 复合主键
                } else if (Integer.class.equals(ormColumn.getType())) {
                    typeConvert = new UniconKeyConvert<>(type, Integer.class, ormColumn);
                } else if (Long.class.equals(ormColumn.getType())) {
                    typeConvert = new UniconKeyConvert<>(type, Long.class, ormColumn);
                } else if (String.class.equals(ormColumn.getType())) {
                    typeConvert = new UniconKeyConvert<>(type, String.class, ormColumn);
                } else if (Double.class.equals(ormColumn.getType())) {
                    typeConvert = new UniconKeyConvert<>(type, Double.class, ormColumn);
                } else if (Float.class.equals(ormColumn.getType())) {
                    typeConvert = new UniconKeyConvert<>(type, Float.class, ormColumn);
                } else {
                    //不支持类型主键
                }
                if (typeConvert != null) {
                    TypeConverts.get().registerUniconKey(key2, typeConvert);
                }
            }
        }
        return typeConvert;
    }

    public Column getColumn() {
        return mColumn;
    }

    public Class<?> getType() {
        return mRClass;
    }

    public boolean isNumberField() {
        return mRClass == Integer.class
                || mRClass == Long.class;
    }

    //
    public SQLiteType getSQLiteType() {
        if (mConvert == null) {
            return SQLiteType.TEXT;
        }
        return mConvert.getSQLiteType();
    }

    public boolean isReadOnly() {
        return mUnion != null && mUnion.readOnly();
    }

    public boolean isAutoIncrement() {
        return mPrimaryKey != null && mPrimaryKey.autoIncrement();
    }

    @Override
    public String toString() {
        return "OrmColumn{" +
                "name=" + getColumnNameOrg() +
                ", Field=" + mField +
                ", pClass=" + pClass +
                '}';
    }

    public boolean hasDefaultValue() {
        return !"___NULL".equals(mColumn.defaultValue());
    }

    public String getDefaultValue() {
        if (TextUtils.isEmpty(mDefaultText)) {
            mDefaultText = mColumn.defaultValue();
            if ("___NULL".equals(mDefaultText) || mDefaultText == null) {
                mDefaultText = "null";
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
        return mPrimaryKey != null;
    }

    public String getColumnName() {
        if (TextUtils.isEmpty(mColumnName)) {
            mColumnName = SQLiteUtils.mask(getColumnNameOrg());
        }
        return mColumnName;
    }

    public String getColumnNameOrg() {
        if (TextUtils.isEmpty(mColumn.value())) {
            return mField.getName();
        }
        return mColumn.value();
    }

    public Object toDbValue(Orm orm, Object val, SQLiteOpera opera) {
        if (mConvert == null) {
            return null;
        }
        if (val != null) {
            try {
                IConvert iConvert = mConvert;
                val = iConvert.toDbValue(orm, val, opera);
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

    public Object getDbValueByParent(Orm orm, Object parent, SQLiteOpera opera) {
        Object val = null;
        try {
            val = mField.get(parent);
        } catch (IllegalAccessException e) {
        }
        return toDbValue(orm, val, opera);
    }

    public Object getValue(Object parent) {
        Object val = null;
        try {
            val = mField.get(parent);
        } catch (IllegalAccessException e) {
        }
        return val;
    }

    public void write(Orm orm, Object parent, ContentValues contentValues, SQLiteOpera opera) {
        final SQLiteType type = getSQLiteType();
        final Object val = getDbValueByParent(orm, parent, opera);
        final String name = getColumnNameOrg();
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

    public Object read(Orm orm, Cursor cursor) {
        if (mConvert == null) {
            return null;
        }
        int index = -1;
        try {
            if (Build.VERSION.SDK_INT <= 17) {
                index = cursor.getColumnIndex(SQLiteUtils.removeTable(getColumnNameOrg()));
            } else {
                index = cursor.getColumnIndex(getColumnNameOrg());
            }
        } catch (Throwable e) {
            if (Build.VERSION.SDK_INT > 17) {
                index = cursor.getColumnIndex(SQLiteUtils.removeTable(getColumnNameOrg()));
            } else {
                index = cursor.getColumnIndex(getColumnNameOrg());
            }
        }
        if (index < 0) {
            if (Orm.DEBUG) {
                Log.w(Orm.TAG, "no find column " + getColumnNameOrg());
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
