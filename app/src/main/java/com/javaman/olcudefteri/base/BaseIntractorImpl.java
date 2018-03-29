package com.javaman.olcudefteri.base;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;

import com.javaman.olcudefteri.api.model.response.ApiError;
import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.login.service.LoginService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class BaseIntractorImpl implements BaseIntractor {

    LoginService loginService;

    @Override
    public void logout(String sessionId, final onBaseProcessListener listener) {


        loginService = ApiClient.getClient().create(LoginService.class);


        Call<BaseModel> token = loginService.logout(sessionId);

        token.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {

                    //response [200 ,300) aralığında ise

                    BaseModel baseModel = response.body();
                    listener.onSuccess(baseModel.getResponseMessage());
                    


                }else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailure("Hata: "+apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailure("Beklenmedik hata..." + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailure(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailure("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailure("Ağ hatası : " + t.getMessage());
                }


            }
        });


    }


    @Override
    public void checkSession(String sessionId, onBaseProcessListener listener) {

    }
}
