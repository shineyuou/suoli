package com.annis.tensioncable.base;

import android.app.Application;
import android.support.multidex.MultiDex;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
