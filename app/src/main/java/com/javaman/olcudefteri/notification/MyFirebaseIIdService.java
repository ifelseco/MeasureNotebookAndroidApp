package com.javaman.olcudefteri.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

/**
 * Created by javaman on 19.02.2018.
 */

public class MyFirebaseIIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferenceHelper sharedPreferenceHelper;


    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToPref(refreshedToken);


    }



    private void sendRegistrationToPref(String token){
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.setStringPreference("firebase_reg_id",token);
    }
}
