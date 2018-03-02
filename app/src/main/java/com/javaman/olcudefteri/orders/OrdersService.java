package com.javaman.olcudefteri.orders;

import com.javaman.olcudefteri.model.OrdersDeleteModel;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.response_model.BaseResponse;
import com.javaman.olcudefteri.model.response_model.OrderSummaryReponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @HTTP(method = "DELETE" , path = "/order/list" , hasBody = true)
    Call<BaseResponse> deleteOrders(@Header("X-Auth-Token") String xAuthToken , @Body OrdersDeleteModel ordersDeleteModel);

}
