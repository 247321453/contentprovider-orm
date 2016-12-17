package net.kk.orm.converts;

import net.kk.orm.Orm;

public class JsonTextConvert<T> extends CustomConvert<T> {
    private Class<T> mTClass;
    private IOjectConvert convert;

    public JsonTextConvert(Class<T> TClass, IOjectConvert convert) {
        mTClass = TClass;
        this.convert = convert;
    }

    protected Class<T> getType() {
        return mTClass;
    }

    @Override
    public T toValue(Orm orm, String text) {
        return convert.fromJson(text, getType());
    }

    @Override
    public String toDbValue(Orm orm, T value) {
        return convert.toJson(value);
    }
}
