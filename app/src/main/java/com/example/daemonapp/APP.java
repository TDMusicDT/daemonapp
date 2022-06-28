package com.example.daemonapp;

import android.app.Application;
import android.content.Context;

public class APP extends Application {
    private static Application instance;
    private static Context context;
    public static boolean is_init_daemon = false;

    @Override
    public void onCreate() {
        super.onCreate();
        APP.context = getApplicationContext();
        instance = this;
    }

    public static Context getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return APP.context;
    }
}
