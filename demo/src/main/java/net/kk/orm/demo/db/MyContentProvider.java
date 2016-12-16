package net.kk.orm.demo.db;

import android.content.Context;

import net.kk.orm.OrmContentProvider;
import net.kk.orm.OrmSQLiteOpenHelper;
import net.kk.orm.demo.BuildConfig;
import net.kk.orm.demo.bean.SetBean;
import net.kk.orm.demo.bean.StubBean;

import java.util.Arrays;

public class MyContentProvider extends OrmContentProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".db.orm";
    private static final int VERSION = 2;
    private static final String DBNAME = "my.db";
    private MyOrmSQLiteHelper mMyOrmSQLiteHelper;

    public MyContentProvider() {
    }

    @Override
    protected OrmSQLiteOpenHelper getSQLiteOpenHelper(Context context) {
        if (mMyOrmSQLiteHelper == null) {
            mMyOrmSQLiteHelper = new MyOrmSQLiteHelper(context, DBNAME, VERSION,
                    Arrays.asList(SetBean.class, StubBean.class));
        }
        return mMyOrmSQLiteHelper;
    }
}
