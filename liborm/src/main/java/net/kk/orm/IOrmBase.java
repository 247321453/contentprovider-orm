package net.kk.orm;

import android.util.Log;

import java.lang.reflect.Constructor;

abstract class IOrmBase {

    protected <T> T create(Class<T> pClass) {
        T t = null;
        try {
            t = pClass.newInstance();
        } catch (Exception e) {
            if (Orm.DEBUG) {
                Log.w(Orm.TAG, "create " + pClass.getName(), e);
            }
            Constructor<T> constructor = null;
            try {
                constructor = pClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                t = constructor.newInstance();
            } catch (Exception e1) {
                Log.e(Orm.TAG, "create " + pClass.getName(), e);
            }
        }
        return t;
    }
}
