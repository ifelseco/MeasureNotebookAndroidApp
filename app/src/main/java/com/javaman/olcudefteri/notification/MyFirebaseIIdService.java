package com.javaman.olcudefteri.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.add_order.CustomerService;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.response_model.AddCustomerResponse;
import com.javaman.olcudefteri.api.response_model.ApiError;
import com.javaman.olcudefteri.api.response_model.BaseResponse;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

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
