package net.kk.orm.demo.db;

import static net.kk.orm.demo.db.MyContentProvider.AUTHORITY;

public interface Datas {
    interface Set {
        String TABLE = "set";
        String CONTENT_URI_STRING =  "content://" + AUTHORITY +"/" + TABLE + "/";
        String ID = "id";
        String NAME = "name";
        String USERS = "users";
        String STUB="stub";
    }

    interface Stub {
        String TABLE = "stub";
        String CONTENT_URI_STRING = "content://" + AUTHORITY +"/" + TABLE + "/";
        String NAME = "name";
        String ADDRESS="address";
    }
}
