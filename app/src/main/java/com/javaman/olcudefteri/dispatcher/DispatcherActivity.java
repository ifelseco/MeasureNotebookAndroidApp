package com.javaman.olcudefteri.dispatcher;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.javaman.olcudefteri.SplashScreenActivity;
import com.javaman.olcudefteri.dispatcher.presenter.DispatcherPresenter;
import com.javaman.olcudefteri.dispatcher.presenter.DispatcherPresenterImpl;
import com.javaman.olcudefteri.dispatcher.view.DispatcherView;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

/**
 * Created by javaman on 15.12.2017.
 */

public class DispatcherActivity extends AppCompatActivity implements DispatcherView{

    private static final String TAG = DispatcherActivity.class.getSimpleName();
    SharedPreferenceHelper sharedPreferenceHelper;
    private DispatcherPresenter mDispatcherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        mDispatcherPresenter=new DispatcherPresenterImpl(this);
        getNotificationCountFromServer();

    }

    private void redirectActivity() {
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
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


    @Override
    public void getNotificationCountFromServer() {
        String headerData=getSessionIdFromPref();
        mDispatcherPresenter.getNotificationCount(headerData);
    }

    @Override
    public void saveNotfCountToPref(int count) {
        sharedPreferenceHelper.setIntegerPreference("notf-count",count);
    }

    @Override
    public void redirect() {
        redirectActivity();
    }

    @Override
    public void logout() {

    }

    @Override
    public void checkSession() {

    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(DispatcherActivity.this, LoginActivity.class));
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void removeKeyFromPref(String key) {

    }

    @Override
    public void showAlert(String message, boolean isToast) {
        if(isToast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void hideProgress() {

    }
}
