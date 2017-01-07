package net.kk.orm.demo.db;

import android.content.Context;

import net.kk.orm.providers.OrmContentProvider;
import net.kk.orm.providers.OrmSQLiteOpenHelper;
import net.kk.orm.providers.SimpleOrmSQLiteHelper;
import net.kk.orm.demo.bean.SetBean;
import net.kk.orm.demo.bean.StubBean;

import java.util.Arrays;

public class MyContentProvider extends OrmContentProvider {

    private OrmSQLiteOpenHelper mMyOrmSQLiteHelper;

    @Override
    protected OrmSQLiteOpenHelper getSQLiteOpenHelper(Context context) {
        if (mMyOrmSQLiteHelper == null) {
            mMyOrmSQLiteHelper = new SimpleOrmSQLiteHelper(context, Datas.DBNAME, Datas.VERSION,
                    Arrays.asList(SetBean.class, StubBean.class));
        }
        return mMyOrmSQLiteHelper;
    }
}
