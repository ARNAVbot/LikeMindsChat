package com.example.likemindschat;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    private static Context sAppContext;

    public static Context getAppContext() {
        return sAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }
}
