package com.javaman.olcudefteri.login;

import com.javaman.olcudefteri.model.response_model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by javaman on 08.02.2018.
 */

public interface LoginService {

    @GET("/token")
    Call<AuthResponse> sendCredential(@Header("Authorization") String authorization , @Header("Content-Type") String contentType);

}
