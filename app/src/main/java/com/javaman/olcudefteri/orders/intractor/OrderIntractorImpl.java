package com.javaman.olcudefteri.orders.intractor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.model.response.ApiError;
import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.OrdersDeleteModel;
import com.javaman.olcudefteri.orders.model.response.AddOrderLineResponse;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.service.OrderLineService;
import com.javaman.olcudefteri.orders.service.OrderService;
import com.javaman.olcudefteri.orders.service.OrdersService;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class OrderIntractorImpl implements OrderIntractor {

    OrderService orderService;

    @Override
    public void orderUpdate(String xAuthToken, final OrderUpdateModel orderUpdateModel, final onOrderProcessListener listener) {
        orderService = ApiClient.getClient().create(OrderService.class);
        Call<BaseModel> baseModelCall = orderService.updateOrder(xAuthToken,orderUpdateModel);
        baseModelCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    BaseModel baseModel = response.body();
                    listener.onSuccessUpdateOrder(baseModel,orderUpdateModel);
                } else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        BaseModel apiError = gson.fromJson(errorBody, BaseModel.class);

                        Log.d("Hata Mesaj:", response.code() +" "+ apiError.getResponseMessage());
                        listener.onFailureUpdateOrder("Server hatası :"+response.code() +"\n"+ apiError.getResponseMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureUpdateOrder("Beklenmedik hata..." + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        listener.onFailureUpdateOrder(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureUpdateOrder("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureUpdateOrder("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }

    @Override
    public void orderDelete(String xAuthToken, long id, final onOrderProcessListener listener) {
        orderService = ApiClient.getClient().create(OrderService.class);
        Call<BaseModel> baseResponseCall = orderService.deleteOrder(xAuthToken, id);
        baseResponseCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getResponseMessage();
                    listener.onSuccessDeleteOrder(message);
                } else if (response.code() == 403) {
                    String message = "Sadece izinli kullanıcılar silme işlemi yapabilir.";
                    listener.onFailureDeleteOrder(message);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureDeleteOrder(message);
                    listener.navigateToLogin();
                }
                else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        BaseModel apiError = gson.fromJson(errorBody, BaseModel.class);
                        Log.d("Hata Mesaj:", apiError.getResponseCode() + " " + apiError.getResponseMessage());
                        listener.onFailureDeleteOrder(apiError.getResponseCode() + " " + apiError.getResponseMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDeleteOrder("Beklenmedik hata..." + e.getMessage());
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
                        listener.onFailureDeleteOrder(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDeleteOrder("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureDeleteOrder("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }
}
