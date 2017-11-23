package net.kk.orm.demo.db;

import android.content.Context;

import net.kk.orm.demo.bean.SetBean;
import net.kk.orm.demo.bean.StubBean;
import net.kk.orm.demo.bean.StubBean2;
import net.kk.orm.OrmContentProvider;
import net.kk.orm.OrmSQLiteOpenHelper;

import java.util.Arrays;

public class MyContentProvider extends OrmContentProvider {

    private OrmSQLiteOpenHelper mMyOrmSQLiteHelper;

    @Override
    protected OrmSQLiteOpenHelper createSQLiteOpenHelper(Context context) {
        if (mMyOrmSQLiteHelper == null) {
            mMyOrmSQLiteHelper = new OrmSQLiteOpenHelper(context, Datas.DBNAME, Datas.VERSION,
                    Datas.AUTHORITY,
                    Arrays.asList(SetBean.class, StubBean.class, StubBean2.class));
        }
        return mMyOrmSQLiteHelper;
    }
}
