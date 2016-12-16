package net.kk.orm;

import java.lang.reflect.Constructor;

abstract class IOrm {

    protected <T> T create(Class<T> pClass) {
        T t = null;
        try {
            t = pClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Constructor<T> constructor = null;
            try {
                constructor = pClass.getDeclaredConstructor();
                t = constructor.newInstance();
            } catch (Exception e1) {
            }
        }
        return t;
    }
}
