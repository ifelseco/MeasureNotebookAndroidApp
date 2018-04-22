package com.javaman.olcudefteri.orders.service;

import com.javaman.olcudefteri.orders.model.OrdersDeleteModel;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersService {

    @POST("/order/list")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OrderSummaryReponseModel> getOrders(@Header("X-Auth-Token") String xAuthToken , @Body PageModel pageModel);


    @POST("/order/list/{orderStatus}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OrderSummaryReponseModel> getOrdersWithFilter(@Header("X-Auth-Token") String xAuthToken , @Path("orderStatus") int orderStatus, @Body PageModel pageModel);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @HTTP(method = "DELETE" , path = "/order/list" , hasBody = true)
    Call<BaseModel> deleteOrders(@Header("X-Auth-Token") String xAuthToken , @Body OrdersDeleteModel ordersDeleteModel);

}
