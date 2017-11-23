package net.kk.orm;

import android.os.Parcel;
import android.os.Parcelable;

import net.kk.orm.converts.IConvert;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;

import java.lang.reflect.Field;

public class ParcelableConvert<P extends Parcelable> implements IConvert<byte[], P> {
    private Class<?> mClass;
    private Parcelable.Creator mCreator;

    public ParcelableConvert(Class<?> tClass) {
        mClass = tClass;
        Field field = null;
        try {
            field = mClass.getField("CREATOR");
            mCreator = (Parcelable.Creator) field.get(null);
        } catch (Throwable e) {
        }
    }

    @Override
    public SQLiteType getSQLiteType() {
        return SQLiteType.BLOB;
    }

    @Override
    public P toValue(Orm orm, byte[] bytes) {
        if (bytes == null || mCreator == null) {
            return null;
        }
        Parcel p = Parcel.obtain();
        p.unmarshall(bytes, 0, bytes.length);
        p.setDataPosition(0);
        try {
            return (P) mCreator.createFromParcel(p);
        } catch (Throwable e) {
        }
        return null;
    }

    @Override
    public byte[] toDbValue(Orm orm, P value, SQLiteOpera opera) {
        if (value == null) {
            return null;
        }
        Parcel p = Parcel.obtain();
        value.writeToParcel(p, 0);
        byte[] data = p.marshall();
        p.recycle();
        return data;
    }
}
