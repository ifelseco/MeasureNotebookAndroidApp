package com.javaman.olcudefteri.orders.service;


import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrderLineService {

    @GET("/order/{id}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OrderLineSummaryResponseModel> getOrderLines(@Header("X-Auth-Token") String xAuthToken, @Path("id") Long id);



}
