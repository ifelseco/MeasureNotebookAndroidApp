package com.javaman.olcudefteri.intractor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.utill.ApiClient;

import com.javaman.olcudefteri.intractor.BaseIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.LoginUserModel;
import com.javaman.olcudefteri.service.LoginService;
import com.javaman.olcudefteri.utill.NoConnectivityException;

import org.json.JSONObject;

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
                    


                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureCheckSession(message);
                }else {

                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.getString("responseMessage")!=null){

                            listener.onFailureCheckSession("Bir hata oluştu : "+jObjError.getString("responseMessage"));

                        }else{
                            listener.onFailureCheckSession("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureCheckSession("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureLogout("İnternet bağlantısı yok.");
                }
                else {

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


                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureCheckSession(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureCheckSession(message);
                }else {

                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailureCheckSession("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailureCheckSession("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureCheckSession("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureCheckSession("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailureCheckSession("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }
}
