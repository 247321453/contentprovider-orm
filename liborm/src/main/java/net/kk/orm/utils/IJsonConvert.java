package net.kk.orm.utils;

public interface IJsonConvert {
    <T> T fromJson(String json, Class<T> classOfT);
    String toJson(Object src);
}
