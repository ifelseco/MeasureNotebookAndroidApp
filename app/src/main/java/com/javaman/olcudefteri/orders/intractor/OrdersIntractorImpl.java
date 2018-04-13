package com.javaman.olcudefteri.orders.intractor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.orders.model.OrdersDeleteModel;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.api.model.response.ApiError;
import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;
import com.javaman.olcudefteri.orders.service.OrdersService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrdersIntractorImpl implements OrdersIntractor {

    OrdersService ordersService;
    OrderSummaryReponseModel orderSummaryReponseModel;
    BaseModel baseResponse;
    List<OrderDetailResponseModel> orderDetailResponseModels = new ArrayList<>();
    ArrayList<OrderDetailResponseModel> deleteOrders = new ArrayList<>();

    @Override
    public void sendPageRequestToServer(String xAuthToken, PageModel pageModel, final onGetOrdersFinishedListener listener) {

        ordersService = ApiClient.getClient().create(OrdersService.class);
        orderSummaryReponseModel = new OrderSummaryReponseModel();
        Call<OrderSummaryReponseModel> orderSummaryReponseModelCall = ordersService.getOrders(xAuthToken, pageModel);
        orderSummaryReponseModelCall.enqueue(new Callback<OrderSummaryReponseModel>() {
            @Override
            public void onResponse(Call<OrderSummaryReponseModel> call, Response<OrderSummaryReponseModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryReponseModel = response.body();
                    //orderSummaryReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccess(orderSummaryReponseModel);
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
            public void onFailure(Call<OrderSummaryReponseModel> call, Throwable t) {

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

                    listener.onFailure("Ağ hatası : " + t.getMessage()+t.getClass());
                }


            }
        });


    }

    @Override
    public void sendDeleteOrderListRequestToServer(String xAuthToken, final ArrayList<OrderDetailResponseModel> orders, final onDeleteOrdersFinishedListener listener) {
        ordersService = ApiClient.getClient().create(OrdersService.class);
        baseResponse = new BaseModel();

        OrdersDeleteModel ordersDeleteModel = new OrdersDeleteModel();
        ArrayList<Long> orderIds = new ArrayList<>();
        for (OrderDetailResponseModel order : orders) {
            orderIds.add(order.getId());
        }
        ordersDeleteModel.setOrderIds(orderIds);
        Call<BaseModel> baseResponseCall = ordersService.deleteOrders(xAuthToken, ordersDeleteModel);

        baseResponseCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getResponseMessage();
                    listener.onSuccess(message, orders);
                } else if (response.code() == 403) {
                    String message = "Sadece yönetici izni olanlar silme işlemi yapabilir.";
                    listener.onFailure(message);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                }
                else{
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
        public void onFailure (Call <BaseModel> call, Throwable t){
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

