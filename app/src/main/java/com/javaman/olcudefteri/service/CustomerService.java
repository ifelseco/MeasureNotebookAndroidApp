package com.javaman.olcudefteri.service;

import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.AddCustomerModel;
import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by javaman on 15.02.2018.
 */

public interface CustomerService {

    @POST("/customer/add/")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<AddCustomerResponse> addCustomer(@Header("X-Auth-Token") String xAuthToken,
                                          @Body AddCustomerModel addCustomerModel);

    @GET("/customer/search/")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<CustomerSummaryModel> customerSearch(@Header("X-Auth-Token") String xAuthToken , @Query("text") String text);


    @GET("/customer/{id}/orders")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OrderSummaryModel> getCustomerOrders(@Header("X-Auth-Token") String xAuthToken , @Path("id") long customerId);


}
