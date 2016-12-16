package net.kk.orm.demo.db;

import net.kk.orm.demo.BuildConfig;


public interface Datas {
    String AUTHORITY = BuildConfig.APPLICATION_ID + ".db.orm";
    int VERSION = 2;
    String DBNAME = "my.db";

    interface Set {
        String TABLE = "set";
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE + "/";
        String ID = "id";
        String NAME = "name";
        String USERS = "users";
        String STUB = "stub";
    }

    interface Stub {
        String TABLE = "stub";
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE + "/";
        String NAME = "name";
        String ADDRESS = "address";
    }
}
