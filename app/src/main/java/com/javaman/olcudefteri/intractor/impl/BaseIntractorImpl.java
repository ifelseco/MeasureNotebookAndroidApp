package com.javaman.olcudefteri.intractor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;

import com.javaman.olcudefteri.intractor.BaseIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.LoginUserModel;
import com.javaman.olcudefteri.service.LoginService;

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
                    listener.onSuccessLogout(baseModel.getResponseMessage());
                    


                }else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureLogout("Hata: "+apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureLogout("Beklenmedik hata..." + e.getMessage());
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
                        listener.onFailureLogout(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureLogout("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureLogout("Ağ hatası : " + t.getMessage());
                }


            }
        });


    }


    @Override
    public void checkSession(String sessionId, final onBaseProcessListener listener) {
        loginService = ApiClient.getClient().create(LoginService.class);


        Call<LoginUserModel> token = loginService.checkSession(sessionId);

        token.enqueue(new Callback<LoginUserModel>() {
            @Override
            public void onResponse(Call<LoginUserModel> call, Response<LoginUserModel> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {

                    //response [200 ,300) aralığında ise

                    LoginUserModel loginUserModel= response.body();
                    listener.onSuccessCheckSession(loginUserModel);



                }else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        String message="Oturumunuz zaman aşımına uğradı.\nTekrar giriş yapınız";
                        listener.onFailureCheckSession(message);

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureCheckSession("Beklenmedik hata..." + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<LoginUserModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureCheckSession(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureCheckSession("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureCheckSession("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }
}
