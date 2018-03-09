package com.javaman.olcudefteri.orders.service;

import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.orders.model.AddCustomerModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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
}
