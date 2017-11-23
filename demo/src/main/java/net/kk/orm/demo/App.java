package net.kk.orm.demo;

import android.app.Application;

import net.kk.orm.Orm;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Orm.DEBUG = true;
    }
}
