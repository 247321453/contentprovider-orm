package net.kk.orm.demo;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kk.orm.Orm;
import net.kk.orm.utils.IJsonConvert;

public class App extends Application {
    static {
        //json
        Orm.initJson(new IJsonConvert() {
            private final Gson mGson = new GsonBuilder().create();

            @Override
            public <T> T fromJson(String json, Class<T> classOfT) {
                return mGson.fromJson(json, classOfT);
            }

            @Override
            public String toJson(Object src) {
                return mGson.toJson(src);
            }
        });
    }
}
