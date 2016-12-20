package net.kk.orm.demo.game;

import android.content.Context;
import android.util.Log;

import net.kk.orm.api.Orm;
import net.kk.orm.api.providers.OrmContentProvider;
import net.kk.orm.api.providers.OrmSQLiteOpenHelper;
import net.kk.orm.api.providers.SimpleOrmSQLiteHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class CardContentProvider extends OrmContentProvider {

    private OrmSQLiteOpenHelper mMyOrmSQLiteHelper;
    private File mDbFile;

    @Override
    public boolean onCreate() {
        mDbFile = getContext().getDatabasePath(OrmCard.DBNAME);
        if (!mDbFile.exists()) {
            try {
                //TODO  简化代码
                InputStream inputStream = getContext().getAssets().open(OrmCard.DBNAME);
                File dir = mDbFile.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream outputStream = new FileOutputStream(mDbFile);
                byte[] data = new byte[4096];
                int len;
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data);
                }
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(Orm.TAG, "copy db", e);
            }
        }
        return super.onCreate();
    }

    @Override
    protected OrmSQLiteOpenHelper getSQLiteOpenHelper(Context context) {
        if (mMyOrmSQLiteHelper == null) {
            mMyOrmSQLiteHelper = new SimpleOrmSQLiteHelper(context, OrmCard.DBNAME, OrmCard.VERSION,
                    Arrays.asList(CardData.class, CardText.class, Card.class, CardFull.class, CardInfo.class));
        }
        return mMyOrmSQLiteHelper;
    }
}
