package com.javaman.olcudefteri;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by javaman on 15.12.2017.
 */

public class DispatcherActivity extends Activity {

    private static final String TAG = DispatcherActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);


        Class<?> activityClass;

        try {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            activityClass = Class.forName(prefs.getString("lastActivity", SplashScreenActivity.class.getName()));
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
