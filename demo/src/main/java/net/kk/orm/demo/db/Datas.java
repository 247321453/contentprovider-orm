package net.kk.orm.demo.db;

import net.kk.orm.demo.BuildConfig;


public interface Datas {
    String AUTHORITY = BuildConfig.APPLICATION_ID + ".db.orm";
    int VERSION = 3;
    String DBNAME = "my.db";

    interface Set {
        String TABLE = "set";
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE + "/";
        String ID = "id";
        String NAME = "name";
        String USERS = "users";
        String STUB = "stub";
        String STUBS = "stubs";
        String STUBS2 = "stubs2";
    }

    interface Stub2 {
        String TABLE = "stub2";
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE + "/";
        String NAME = "name";
        String ADDRESS = "address";
    }

    interface Stub {
        String TABLE = "stub";
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE + "/";
        String NAME = "name";
        String ADDRESS = "address";
    }
}
