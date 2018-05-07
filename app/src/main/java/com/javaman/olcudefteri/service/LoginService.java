package com.javaman.olcudefteri.service;

import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.AuthResponse;
import com.javaman.olcudefteri.model.LoginUserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by javaman on 08.02.2018.
 */

public interface LoginService {

    @GET("/token")
    Call<AuthResponse> sendCredential(@Header("Authorization") String authorization , @Header("Content-Type") String contentType);

    @POST("/user/logout")
    Call<BaseModel> logout(@Header("X-Auth-Token") String xAuthToken);

    @GET("/checkSession")
    Call<LoginUserModel> checkSession(@Header("X-Auth-Token") String xAuthToken);






}
