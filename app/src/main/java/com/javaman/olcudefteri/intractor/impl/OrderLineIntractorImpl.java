package com.javaman.olcudefteri.intractor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.intractor.OrderLineIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.service.OrderLineService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 12.03.2018.
 */

public class OrderLineIntractorImpl implements OrderLineIntractor {

    OrderLineService orderLineService;
    OrderLineSummaryResponseModel orderLineSummaryResponseModel;

    @Override
    public void sendGetOrderLineRequest(String xAuthToken, Long orderId, final onOrderLineProcessListener listener) {

        orderLineService = ApiClient.getClient().create(OrderLineService.class);
        orderLineSummaryResponseModel = new OrderLineSummaryResponseModel();
        Call<OrderLineSummaryResponseModel> orderLineSummaryResponseModelCall = orderLineService.getOrderLines(xAuthToken, orderId);
        orderLineSummaryResponseModelCall.enqueue(new Callback<OrderLineSummaryResponseModel>() {
            @Override
            public void onResponse(Call<OrderLineSummaryResponseModel> call, Response<OrderLineSummaryResponseModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderLineSummaryResponseModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccessGetOrderLines(orderLineSummaryResponseModel);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureGetOrderLines(message);
                    listener.navigateToLogin();
                } else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureGetOrderLines(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetOrderLines("Beklenmedik hata..." + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderLineSummaryResponseModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureGetOrderLines(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetOrderLines("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureGetOrderLines("Ağ hatası : " + t.getMessage());
                }


            }
        });


    }

    @Override
    public void deleteOrderLine(String xAuthToken, final OrderLineDetailModel orderLineDetailModel, final onOrderLineProcessListener listener) {
        orderLineService = ApiClient.getClient().create(OrderLineService.class);
        long id=orderLineDetailModel.getId();
        Call<BaseModel> baseResponseCall = orderLineService.deleteOrderLine(xAuthToken, id);
        baseResponseCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

                if (response.isSuccessful()) {
                    String message = response.body().getResponseMessage();
                    listener.onSuccessDeleteOrderLine(orderLineDetailModel,message);
                } else if (response.code() == 403) {
                    String message = "Sadece yönetici izni olanlar\nsilme işlemi yapabilir.";
                    listener.onFailureDeleteOrderLine(message);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı\ntekrar giriş yapınız!";
                    listener.onFailureDeleteOrderLine(message);
                    listener.navigateToLogin();
                }
                else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureDeleteOrderLine(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDeleteOrderLine("Beklenmedik hata..." + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureDeleteOrderLine(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDeleteOrderLine("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureDeleteOrderLine("Ağ hatası : " + t.getMessage());
                }

            }
        });
    }
}
