package com.javaman.olcudefteri.home.service;

import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.home.model.NotificationSummaryModel;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;

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
