package com.javaman.olcudefteri.intractor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.intractor.HomeIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.AppUtilInfoModel;
import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.NotificationSummaryModel;
import com.javaman.olcudefteri.service.AppInfoService;
import com.javaman.olcudefteri.service.NotificationService;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;
import com.javaman.olcudefteri.service.FirebaseService;

import org.json.JSONObject;

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
    private AppInfoService appInfoService;

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

                }

                else {

                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.getString("responseMessage")!=null){
                            Log.e("FBase RegId Send Error:",jObjError.getString("responseMessage"));
                        }
                        else{
                            Log.e("FBase RegId Send Error",jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        Log.e("Beklenmedik hata : ",e.getMessage()+"\n"+response.message());
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
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureGetNotification(message);
                }
                else {
                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailureGetNotification("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailureGetNotification("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureGetNotification("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureDelete(message);
                }

                else{
                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.getString("responseMessage")!=null){
                            listener.onFailureDelete("Bir hata oluştu : "+jObjError.getString("responseMessage"));
                        }
                        else{
                            listener.onFailureDelete("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureDelete("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureDelete(message);
                }

                else{
                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.getString("responseMessage")!=null){
                            listener.onFailureDelete("Bir hata oluştu : "+jObjError.getString("responseMessage"));
                        }else{
                            listener.onFailureDelete("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureDelete("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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

    @Override
    public void getAppUtilInfo(String headerData, final onAppInfoListener listener) {
        appInfoService = ApiClient.getClient().create(AppInfoService.class);
        Call<AppUtilInfoModel> appUtilInfoModelCall = appInfoService.getAppUtilInfo(headerData);
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
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailure(message);
                }

                else{
                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailure("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailure("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailure("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
