package com.javaman.olcudefteri.login;


import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.response_model.ApiError;
import com.javaman.olcudefteri.api.ApiUtils;
import com.javaman.olcudefteri.api.response_model.AuthResponse;

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


    @Override
    public void dummyLogin(String username, String password, final onLoginFinishedListener listener) {

        if (TextUtils.isEmpty(username)) {
            listener.onUserNameEmptyError();
        } else if (TextUtils.isEmpty(password)) {
            listener.onPasswordEmptyError();
        } else if (username.equals("admin") && password.equals("12345")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.onSuccess();
                }
            },3000);

            listener.onSuccess();


            //or postToServer(username,password);
        } else {
            listener.onFailure("Kullanıcı adı yada parola hatalı");
        }


    }

    public void login(String username, String password, final onLoginFinishedListener listener) {

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
                        listener.onSuccess();


                    } else {

                        //response [200 ,300) aralığında değil ise

                        Gson gson = new GsonBuilder().create();

                        try {

                            String errorBody = response.errorBody().string();

                            ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                            Log.d("Hata Mesaj:", apiError.getStatus() +" "+ apiError.getMessage());
                            listener.onFailure(apiError.getStatus() +" "+ apiError.getMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onFailure("Beklenmedik hata..." + e.getMessage());
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
