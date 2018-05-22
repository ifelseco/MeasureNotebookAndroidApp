package com.javaman.olcudefteri.service;

import com.javaman.olcudefteri.model.OrderSummaryModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;


public interface ReportService {

    @GET("/report/orders/endOfDay/")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OrderSummaryModel> getEndOfDayOrders(@Header("X-Auth-Token") String xAuthToken);

    @GET("/report/next/measure/")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OrderSummaryModel> getNextMeasure(@Header("X-Auth-Token") String xAuthToken);


    @GET("/report/next/delivery/")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OrderSummaryModel> getNextDelivery(@Header("X-Auth-Token") String xAuthToken);

}
