package com.javaman.olcudefteri.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by javaman on 19.02.2018.
 */

public class MyFirebaseIIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToPref(refreshedToken);


    }



    private void sendRegistrationToPref(String token){
        SharedPreferences sharedPreferences=getSharedPreferences("firebase",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("firebase_reg_id",token);
        editor.commit();
    }
}
