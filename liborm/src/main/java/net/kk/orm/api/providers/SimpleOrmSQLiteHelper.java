package net.kk.orm.api.providers;

import android.content.Context;

import net.kk.orm.api.IContentResolver;
import net.kk.orm.api.Orm;
import net.kk.orm.api.providers.OrmSQLiteOpenHelper;

import java.util.List;

public class SimpleOrmSQLiteHelper extends OrmSQLiteOpenHelper {
    private IContentResolver mContentResolver;
    private List<Class<?>> mClassList;

    public SimpleOrmSQLiteHelper(Context context, String name, int version, List<Class<?>> list) {
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
