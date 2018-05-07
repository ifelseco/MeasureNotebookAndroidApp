package com.javaman.olcudefteri.service;

import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.NotificationSummaryModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;


public interface NotificationService {

    @GET("/notification/list")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<NotificationSummaryModel> getNotifications(@Header("X-Auth-Token") String xAuthToken);


    @DELETE("/notification/{id}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<BaseModel> deleteNotification(@Header("X-Auth-Token") String xAuthToken , @Path("id") long id);


    @DELETE("/notification/list")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<BaseModel> deleteAllNotification(@Header("X-Auth-Token") String xAuthToken);
}
