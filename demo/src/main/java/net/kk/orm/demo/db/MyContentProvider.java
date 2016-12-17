package net.kk.orm.demo.db;

import android.content.Context;

import net.kk.orm.OrmContentProvider;
import net.kk.orm.OrmSQLiteOpenHelper;
import net.kk.orm.SimpleOrmSQLiteHelper;
import net.kk.orm.demo.bean.SetBean;
import net.kk.orm.demo.bean.StubBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
