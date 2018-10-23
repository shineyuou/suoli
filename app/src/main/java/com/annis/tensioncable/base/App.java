package com.annis.tensioncable.base;

import android.support.multidex.MultiDex;

import org.litepal.LitePalApplication;

public class App extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
