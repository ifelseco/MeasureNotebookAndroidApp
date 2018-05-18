package com.javaman.olcudefteri.ui.dispatcher;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.javaman.olcudefteri.ui.splash.SplashScreenActivity;
import com.javaman.olcudefteri.service.MyService;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

/**
 * Created by javaman on 15.12.2017.
 */

public class DispatcherActivity extends AppCompatActivity{

    private static final String TAG = DispatcherActivity.class.getSimpleName();
    SharedPreferenceHelper sharedPreferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
        redirectActivity();


    }

    private void redirectActivity() {
        Class<?> activityClass;

        try {
            activityClass = Class.forName(sharedPreferenceHelper.getStringPreference("lastActivity", SplashScreenActivity.class.getName()));
        } catch (ClassNotFoundException ex) {
            activityClass = SplashScreenActivity.class;
        }

        startActivity(new Intent(this, activityClass));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }



}
