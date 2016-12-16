package net.kk.orm.demo.db;

import android.content.Context;

import net.kk.orm.IContentResolver;
import net.kk.orm.Orm;
import net.kk.orm.OrmSQLiteOpenHelper;

import java.util.List;

public class MyOrmSQLiteHelper extends OrmSQLiteOpenHelper {
    private IContentResolver mContentResolver;
    private List<Class<?>> mClassList;

    public MyOrmSQLiteHelper(Context context, String name, int version, List<Class<?>> list) {
        super(context, name, version);
        this.mClassList = list;
    }

    @Override
    protected List<Class<?>> getTables() {
        return mClassList;
    }

    @Override
    public IContentResolver getContentResolver() {
        if (mContentResolver == null) {
            mContentResolver = Orm.getContentResolver(context);
        }
        return mContentResolver;
    }
}
