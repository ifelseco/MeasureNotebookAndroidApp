package com.javaman.olcudefteri;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

/**
 * Created by javaman on 15.12.2017.
 */

public class MeasureNotebookApp extends Application {
    private static final String TAG = MeasureNotebookApp.class.getSimpleName();
    private static Context mContext;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        Log.i(TAG, "onCreate()");
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory()");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
