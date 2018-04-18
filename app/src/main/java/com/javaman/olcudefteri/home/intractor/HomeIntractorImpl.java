package com.javaman.olcudefteri.home.intractor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.model.response.ApiError;
import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.home.model.NotificationDetailModel;
import com.javaman.olcudefteri.home.model.NotificationSummaryModel;
import com.javaman.olcudefteri.home.service.NotificationService;
import com.javaman.olcudefteri.notification.FirebaseRegIdModel;
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
    NotificationService notificationService;
    NotificationSummaryModel notificationSummaryModel;
    BaseModel baseModel;

    @Override
    public void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel) {

        firebaseService= ApiClient.getClient().create(FirebaseService.class);

        Call<BaseModel> sendRegIdResponse = firebaseService.sendRegId(xAuthToken,regIdModel);

        sendRegIdResponse.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {

                    //response [200 ,300) aralığında ise

                    BaseModel baseResponse = response.body();

                    //listener.showNotification();

                } else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Hata Mesaj:", apiError.getStatus() +" "+ apiError.getMessage());
                        //listener.onFailureGetOrderLines(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        // listener.onFailureGetOrderLines("Beklenmedik hata..." + e.getMessage());
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

                        //Log.d("Request Error :", apiError.getStatus() +" "+ apiError.getMessage());
                        //listener.onFailureGetOrderLines(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        //listener.onFailureGetOrderLines("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    //listener.onFailureGetOrderLines("Ağ hatası : " + t.getMessage());
                }


            }
        });







    }

    @Override
    public void getNotifiationsFromServer(String xAuthToken, final onNotificationProcessListener listener) {
        notificationService = ApiClient.getClient().create(NotificationService.class);

        Call<NotificationSummaryModel> notificationSummaryModelCall = notificationService.getNotifications(xAuthToken);
        notificationSummaryModelCall.enqueue(new Callback<NotificationSummaryModel>() {
            @Override
            public void onResponse(Call<NotificationSummaryModel> call, Response<NotificationSummaryModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    notificationSummaryModel = response.body();
                    listener.onSuccessGetNotification(notificationSummaryModel);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureGetNotification(message);
                    listener.navigateToLogin();
                }else if (response.code() == 403) {
                    String message = "Sadece yetkili kullanıcılara bildirim gelir";
                    listener.onFailureGetNotification(message);
                } else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        listener.onFailureGetNotification(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetNotification("Beklenmedik hata..." + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationSummaryModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        listener.onFailureGetNotification(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetNotification("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureGetNotification("Ağ hatası : " + t.getMessage());
                }


            }
        });

    }

    @Override
    public void deleteNotification(String xAuthToken, final NotificationDetailModel notificationDetailModel, final onNotificationProcessListener listener) {
        notificationService = ApiClient.getClient().create(NotificationService.class);
        long id=notificationDetailModel.getId();
        Call<BaseModel> baseModelCall=notificationService.deleteNotification(xAuthToken,id);
        baseModelCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    baseModel=response.body();
                    listener.onSuccessDelete(notificationDetailModel);
                }else if(response.code()==401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureDelete(message);
                    listener.navigateToLogin();
                }else if (response.code() == 403) {
                    String message = "Sadece yetkili kullanıcılar bildirim silebilir.";
                    listener.onFailureDelete(message);
                }else{
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        listener.onFailureDelete(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDelete("Beklenmedik hata..." + e.getMessage());
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
                        listener.onFailureDelete(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDelete("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureDelete("Ağ hatası : " + t.getMessage());
                }
            }
        });
    }

    @Override
    public void deleteAllNotification(String xAuthToken, final onNotificationProcessListener listener) {
        notificationService = ApiClient.getClient().create(NotificationService.class);
        Call<BaseModel> baseModelCall=notificationService.deleteAllNotification(xAuthToken);
        baseModelCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    baseModel=response.body();
                    listener.onSuccessDeleteAll(baseModel.getResponseMessage());
                }else if(response.code()==401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureDelete(message);
                    listener.navigateToLogin();
                }else if (response.code() == 403) {
                    String message = "Sadece yetkili kullanıcılar bildirim silebilir.";
                    listener.onFailureDeleteAll(message);
                }else{
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        listener.onFailureDeleteAll(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDeleteAll("Beklenmedik hata..." + e.getMessage());
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
                        listener.onFailureDeleteAll(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDeleteAll("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureDeleteAll("Ağ hatası : " + t.getMessage());
                }
            }
        });
    }
}
