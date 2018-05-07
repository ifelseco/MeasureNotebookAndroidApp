package com.javaman.olcudefteri.service;

import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.OrderUpdateModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderService {

    @DELETE("/order/{id}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<BaseModel> deleteOrder(@Header("X-Auth-Token") String xAuthToken , @Path("id") long id);


    @PUT("/order/update")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<BaseModel> updateOrder(@Header("X-Auth-Token") String xAuthToken, @Body OrderUpdateModel orderUpdateModel);

}
