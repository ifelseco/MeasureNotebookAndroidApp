package com.javaman.olcudefteri.orders.intractor;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.model.response.ApiError;
import com.javaman.olcudefteri.orders.model.AddCustomerModel;
import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.orders.model.response.AddOrderLineListResponse;
import com.javaman.olcudefteri.orders.model.response.AddOrderLineResponse;
import com.javaman.olcudefteri.orders.model.response.CalculationResponse;
import com.javaman.olcudefteri.orders.service.CustomerService;
import com.javaman.olcudefteri.orders.service.OrderLineService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 15.02.2018.
 */

public class AddOrderLineIntractorImpl implements AddOrderLineIntractor {

    OrderLineService orderLineService;


    @Override
    public void addOrderLine(OrderLineDetailModel orderLineDetailModel, String headerData, final onAddOrderLineListener listener) {
        orderLineService = ApiClient.getClient().create(OrderLineService.class);
        String xAuthToken=headerData;
        Call<AddOrderLineResponse> addOrderLineResponse = orderLineService.addOrderLine(xAuthToken,orderLineDetailModel);
        addOrderLineResponse.enqueue(new Callback<AddOrderLineResponse>() {
            @Override
            public void onResponse(Call<AddOrderLineResponse> call, Response<AddOrderLineResponse> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    AddOrderLineResponse addOrderLineResponse = response.body();
                    listener.onSuccessAddOrderLine(addOrderLineResponse,orderLineDetailModel);
                    Log.d("Response body", response.body().toString());
                    Log.d("Auth response:", addOrderLineResponse.toString());
                } else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        AddOrderLineResponse apiError = gson.fromJson(errorBody, AddOrderLineResponse.class);

                        Log.d("Hata Mesaj:", response.code() +" "+ apiError.getBaseModel().getResponseMessage());
                        listener.onFailureAddOrderLine("Server hatası :"+response.code() +"\n"+ apiError.getBaseModel().getResponseMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureAddOrderLine("Beklenmedik hata..." + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<AddOrderLineResponse> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() +" "+ apiError.getMessage());
                        listener.onFailureAddOrderLine(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureAddOrderLine("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureAddOrderLine("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }

    @Override
    public void addOrderLines(AddOrderLineDetailListModel orderLineDetailListModel, String headerData, final onAddOrderLinesListener listener) {
        orderLineService = ApiClient.getClient().create(OrderLineService.class);
        String xAuthToken=headerData;
        Call<AddOrderLineListResponse> addOrderLineListResponse = orderLineService.addOrderLines(xAuthToken,orderLineDetailListModel);
        addOrderLineListResponse.enqueue(new Callback<AddOrderLineListResponse>() {
            @Override
            public void onResponse(Call<AddOrderLineListResponse> call, Response<AddOrderLineListResponse> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    AddOrderLineListResponse addOrderLineListResponse = response.body();
                    listener.onSuccessAddOrderLines(addOrderLineListResponse);
                    Log.d("Response body", response.body().toString());
                    Log.d("Auth response:", addOrderLineListResponse.toString());
                } else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        AddOrderLineListResponse apiError = gson.fromJson(errorBody, AddOrderLineListResponse.class);

                        Log.d("Hata Mesaj:", response.code() +" "+ apiError.getBaseModel().getResponseMessage());
                        listener.onFailureAddOrderLines("Server hatası :"+response.code() +"\n"+ apiError.getBaseModel().getResponseMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureAddOrderLines("Beklenmedik hata..." + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<AddOrderLineListResponse> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() +" "+ apiError.getMessage());
                        listener.onFailureAddOrderLines(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureAddOrderLines("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureAddOrderLines("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }

    @Override
    public void deleteOrderLine(long id, String headerData, onDeleteOrderLineListener listener) {

    }

    @Override
    public void deleteOrderLines(DeleteOrderLinesModel deleteOrderLinesModel, String headerData, onDeleteOrderLinesListener listener) {

    }

    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel, String headerData, final onCalculateOrderLineListener listener) {
        orderLineService = ApiClient.getClient().create(OrderLineService.class);
        String xAuthToken=headerData;
        Call<CalculationResponse> calculationResponse = orderLineService.calculateOrderLine(xAuthToken,orderLineDetailListModel);
        calculationResponse.enqueue(new Callback<CalculationResponse>() {
            @Override
            public void onResponse(Call<CalculationResponse> call, Response<CalculationResponse> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    CalculationResponse calculationResponse = response.body();
                    listener.onSuccessCalculateOrderLines(calculationResponse);
                    Log.d("Response body", response.body().toString());
                    Log.d("Auth response:", calculationResponse.toString());
                } else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        CalculationResponse apiError = gson.fromJson(errorBody, CalculationResponse.class);

                        Log.d("Hata Mesaj:", response.code() +" "+ apiError.getBaseModel().getResponseMessage());
                        listener.onFailureCalculateOrderLines("Server hatası :"+response.code() +"\n"+ apiError.getBaseModel().getResponseMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureCalculateOrderLines("Beklenmedik hata..." + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<CalculationResponse> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() +" "+ apiError.getMessage());
                        listener.onFailureCalculateOrderLines(apiError.getStatus() +" "+ apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureCalculateOrderLines("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureCalculateOrderLines("Ağ hatası : " + t.getMessage());
                }


            }
        });

    }
}
