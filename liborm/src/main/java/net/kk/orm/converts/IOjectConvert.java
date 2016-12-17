package net.kk.orm.converts;

public interface IOjectConvert {
    <T> T fromJson(String json, Class<T> classOfT);
    String toJson(Object src);
}
