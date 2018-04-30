package com.frog.el_attentionattacher.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * 仅用于获取全局context
 * framed by Wen Sun
 */

public class BasicApp extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context = getApplicationContext();
    }

}