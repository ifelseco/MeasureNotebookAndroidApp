package com.javaman.olcudefteri.dispatcher.service;

import com.javaman.olcudefteri.dispatcher.model.CountModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface DispatcherService {

    @GET("/notification/count")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<CountModel> getNotificationCount(@Header("X-Auth-Token") String xAuthToken);

}
