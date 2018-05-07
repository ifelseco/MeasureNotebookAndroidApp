package com.javaman.olcudefteri.service;


import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.AddOrderLineListResponse;
import com.javaman.olcudefteri.model.AddOrderLineResponse;
import com.javaman.olcudefteri.model.CalculationResponse;
import com.javaman.olcudefteri.model.OrderLineSummaryResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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


    @POST("/orderLine/add/")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<AddOrderLineResponse> addOrderLine(@Header("X-Auth-Token") String xAuthToken,
                                            @Body OrderLineDetailModel orderLineDetailModel);


    @POST("/orderLine/calculate")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<CalculationResponse> calculateOrderLine(@Header("X-Auth-Token") String xAuthToken,
                                                 @Body AddOrderLineDetailListModel orderLineDetailListModel);

    @POST("/orderLine/list/add/")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<AddOrderLineListResponse> addOrderLines(@Header("X-Auth-Token") String xAuthToken,
                                                @Body AddOrderLineDetailListModel orderLineDetailListModel);


    @DELETE("/orderLine/{id}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<BaseModel> deleteOrderLine(@Header("X-Auth-Token") String xAuthToken , @Path("id") long id);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @HTTP(method = "DELETE" , path = "/orderLine/list" , hasBody = true)
    Call<BaseModel> deleteOrderLines(@Header("X-Auth-Token") String xAuthToken , @Body DeleteOrderLinesModel deleteOrderLinesModel);






}
