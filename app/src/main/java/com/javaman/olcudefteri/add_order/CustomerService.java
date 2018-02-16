package com.javaman.olcudefteri.add_order;

import com.javaman.olcudefteri.api.response_model.AddCustomerResponse;
import com.javaman.olcudefteri.model.CustomerDetailModel;

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
                                          @Body CustomerDetailModel customerDetailModel);
}
