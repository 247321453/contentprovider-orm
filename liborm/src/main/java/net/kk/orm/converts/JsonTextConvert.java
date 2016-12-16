package net.kk.orm.converts;

import net.kk.orm.Orm;

public class JsonTextConvert<T> extends CustomConvert<T> {
    private Class<T> mTClass;

    public JsonTextConvert(Class<T> TClass) {
        mTClass = TClass;
    }

    protected Class<T> getType() {
        return mTClass;
    }

    @Override
    public T toValue(String text) {
        return Orm.getJsonConvert().fromJson(text, getType());
    }

    @Override
    public String toDbValue(T value) {
        return Orm.getJsonConvert().toJson(value);
    }
}
