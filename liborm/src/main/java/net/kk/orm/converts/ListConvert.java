package net.kk.orm.converts;


import android.text.TextUtils;

import net.kk.orm.Orm;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;

import java.util.ArrayList;
import java.util.List;

public abstract class ListConvert<T> implements IConvert<String, List<T>> {
    @Override
    public SQLiteType getSQLiteType() {
        return SQLiteType.TEXT;
    }

    @Override
    public List<T> toValue(Orm orm, String s) {
        if (s != null) {
            List<T> stubBeen = new ArrayList<>();
            String[] vs = s.split(",");
            for (String v : vs) {
                if (!TextUtils.isEmpty(v)) {
                    T stubBean = findById(orm, v);
                    stubBeen.add(stubBean);
                }
            }
            return stubBeen;
        }
        return null;
    }

    protected abstract T findById(Orm orm, String k);

    protected abstract String getKey(T t);

    @Override
    public String toDbValue(Orm orm, List<T> values, SQLiteOpera opera) {
        if (values != null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (T stubBean : values) {
                stringBuffer.append(getKey(stubBean)).append(",");
                try {
                    if (opera == SQLiteOpera.INSERT || opera == SQLiteOpera.UPDATE) {
                        orm.replace(stubBean);
                    } else if (opera == SQLiteOpera.DELETE) {
                        orm.delete(stubBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return stringBuffer.toString();
        }
        return null;
    }
}
