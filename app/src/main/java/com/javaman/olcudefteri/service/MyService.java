package com.javaman.olcudefteri.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

/**
 * Created by javaman on 15.12.2017.
 */

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    SharedPreferenceHelper sharedPreferenceHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        return START_NOT_STICKY;
    }




    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved()");
        /*sharedPreferenceHelper.removeKey("lastActivity");
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");*/
        String firebaseRegId=sharedPreferenceHelper.getStringPreference("firebase_reg_id","");
        int notf_count=sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
        boolean rememberMe=sharedPreferenceHelper.getBooleanPreference("rememberMe",false);
        String key1=sharedPreferenceHelper.getStringPreference(LoginActivity.ARG_LOGIN_1,"");
        String key2=sharedPreferenceHelper.getStringPreference(LoginActivity.ARG_LOGIN_2,"");
        sharedPreferenceHelper.removeAll();
        sharedPreferenceHelper.setStringPreference("firebase_reg_id",firebaseRegId);
        sharedPreferenceHelper.setIntegerPreference("notf-count",notf_count);
        sharedPreferenceHelper.setBooleanPreference("rememberMe",rememberMe);
        sharedPreferenceHelper.setStringPreference(LoginActivity.ARG_LOGIN_1,key1);
        sharedPreferenceHelper.setStringPreference(LoginActivity.ARG_LOGIN_2,key2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory()");
    }


}
