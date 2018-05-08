package com.javaman.olcudefteri.service;

import com.javaman.olcudefteri.model.AppUtilInfoModel;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface DispatcherService {

    @GET("/util/android")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<AppUtilInfoModel> getAppUtilInfo(@Header("X-Auth-Token") String xAuthToken);

}
