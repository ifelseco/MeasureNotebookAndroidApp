package com.javaman.olcudefteri.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.response_model.ApiError;
import com.javaman.olcudefteri.api.response_model.BaseResponse;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;
import com.javaman.olcudefteri.notification.FirebaseService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 20.02.2018.
 */

public class HomeIntractorImpl implements HomeIntractor {

    FirebaseService firebaseService;


    @Override
    public void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel) {

        firebaseService= ApiClient.getClient().create(FirebaseService.class);

        Call<BaseResponse> sendRegIdResponse = firebaseService.sendRegId(xAuthToken,regIdModel);

        sendRegIdResponse.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {

                    //response [200 ,300) aralığında ise

                    BaseResponse baseResponse = response.body();

                    //listener.showNotification();

                } else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Hata Mesaj:", apiError.getStatus() +" "+ apiError.getMessage());
                        //listener.onFailure(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        // listener.onFailure("Beklenmedik hata..." + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        //Log.d("Request Error :", apiError.getStatus() +" "+ apiError.getMessage());
                        //listener.onFailure(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        //listener.onFailure("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    //listener.onFailure("Ağ hatası : " + t.getMessage());
                }


            }
        });







    }
}
