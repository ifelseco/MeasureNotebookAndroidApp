package com.javaman.olcudefteri.orders.intractor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.model.response.ApiError;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.service.OrderLineService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 12.03.2018.
 */

public class OrderLineIntractorImpl implements OrderLineIntractor{

    OrderLineService orderLineService;
    OrderLineSummaryResponseModel orderLineSummaryResponseModel;

    @Override
    public void sendGetOrderLineRequest(String xAuthToken, Long orderId, final onGetOrderLineFinishedListener listener) {

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
                    //orderSummaryReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccess(orderLineSummaryResponseModel);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                } else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailure(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailure("Beklenmedik hata..." + e.getMessage());
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
                        listener.onFailure(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailure("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailure("Ağ hatası : " + t.getMessage());
                }


            }
        });


    }
}
