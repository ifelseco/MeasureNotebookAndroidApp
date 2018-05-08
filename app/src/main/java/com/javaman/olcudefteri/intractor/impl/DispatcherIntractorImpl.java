package com.javaman.olcudefteri.intractor.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.intractor.DispatcherIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.AppUtilInfoModel;
import com.javaman.olcudefteri.service.DispatcherService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class DispatcherIntractorImpl implements DispatcherIntractor {

    private DispatcherService dispatcherService;

    @Override
    public void getAppUtilInfo(String headerData, final onNotificationProcessListener listener) {
        dispatcherService = ApiClient.getClient().create(DispatcherService.class);
        Call<AppUtilInfoModel> appUtilInfoModelCall = dispatcherService.getAppUtilInfo(headerData);
        appUtilInfoModelCall.enqueue(new Callback<AppUtilInfoModel>() {
            @Override
            public void onResponse(Call<AppUtilInfoModel> call, Response<AppUtilInfoModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    AppUtilInfoModel appUtilInfoModel=response.body();
                    listener.onSuccess(appUtilInfoModel);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                }else if (response.code() == 403) {
                    String message = "Sadece yetkili kullanıcılara bildirim gelir";
                    listener.onFailure(message);
                } else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        AppUtilInfoModel apiError = gson.fromJson(errorBody, AppUtilInfoModel.class);
                        listener.onFailure(apiError.getBaseModel().getResponseMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailure("Beklenmedik hata..." + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AppUtilInfoModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
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
}
