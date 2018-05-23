package com.javaman.olcudefteri.intractor.impl;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.intractor.LoginIntractor;
import com.javaman.olcudefteri.service.LoginService;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.utill.ApiUtils;
import com.javaman.olcudefteri.model.AuthResponse;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 06.02.2018.
 */

public class LoginIntractorImpl implements LoginIntractor {

    LoginService loginService;



    public void login(String username, String password,boolean rememberMeActive, final onLoginFinishedListener listener) {

        if (TextUtils.isEmpty(username)) {
            listener.onUserNameEmptyError();
        } else if (TextUtils.isEmpty(password)) {
            listener.onPasswordEmptyError();
        } else {


            loginService = ApiClient.getClient().create(LoginService.class);
            final String auth = ApiUtils.getAuthToken(username, password);
            String contentType = "application/x-www-form-urlencoded";

            Call<AuthResponse> token = loginService.sendCredential(auth, contentType);

            token.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                    //request servera ulaştı ve herhangi bir response döndü

                    if (response.isSuccessful()) {

                        //response [200 ,300) aralığında ise

                        AuthResponse authResponse = response.body();

                        Log.d("Response body", response.body().toString());
                        Log.d("Auth response:", authResponse.toString());
                        Log.d("Session Id:", ""+response.headers().get("X-Auth-Token"));
                        String sessionId=response.headers().get("X-Auth-Token");

                        listener.openSession(sessionId);
                        listener.onSuccess(authResponse);
                        listener.setRememberMe(username,password,rememberMeActive);

                    }else if(response.code()==401){
                        String message = "Kullanıcı adı yada parola hatalı.";
                        listener.onFailure(message);
                    }else if(response.code()==403){
                        String message = "Sisteme erişim yetkiniz yok.";
                        listener.onFailure(message);
                    }
                    else if(response.code()==503){
                        String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                        listener.onFailure(message);
                    }else{

                        //response [200 ,300) aralığında değil ise
                        try {
                            String errorBody=response.errorBody().string();
                            JSONObject jObjError = new JSONObject(errorBody);
                            if(jObjError.get("baseModel")!=null){
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
                public void onFailure(Call<AuthResponse> call, Throwable t) {

                    //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                    if (t instanceof HttpException) {

                        Gson gson = new GsonBuilder().create();

                        try {

                            String errorBody = ((HttpException) t).response().errorBody().string();
                            ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                            Log.d("Request Error :", apiError.getStatus() +" "+ apiError.getMessage());
                            listener.onFailure(apiError.getStatus() +" "+ apiError.getMessage());

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


}
