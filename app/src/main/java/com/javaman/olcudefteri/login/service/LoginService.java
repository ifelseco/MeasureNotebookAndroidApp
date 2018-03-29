package com.javaman.olcudefteri.login.service;

import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.login.model.response.AuthResponse;

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
    Call<BaseModel> logout(@Header("Authorization") String authorization);




}
