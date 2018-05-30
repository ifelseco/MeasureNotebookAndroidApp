package com.javaman.olcudefteri.intractor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.utill.ApiClient;
import com.javaman.olcudefteri.intractor.AddOrderLineIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.AddOrderLineListResponse;
import com.javaman.olcudefteri.model.AddOrderLineResponse;
import com.javaman.olcudefteri.model.CalculationResponse;
import com.javaman.olcudefteri.service.OrderLineService;
import com.javaman.olcudefteri.utill.NoConnectivityException;

import org.json.JSONObject;

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
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureAddOrderLine(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureAddOrderLine(message);
                }else {

                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailureAddOrderLine("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailureAddOrderLine("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureAddOrderLine("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureAddOrderLine("İnternet bağlantısı yok.");
                }
                else {

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
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureAddOrderLines(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureAddOrderLines(message);
                }else {

                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailureAddOrderLines("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailureAddOrderLines("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureAddOrderLines("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureAddOrderLines("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailureAddOrderLines("Ağ hatası : " + t.getMessage());
                }


            }
        });
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
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureCalculateOrderLines(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureCalculateOrderLines(message);
                }else {

                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailureCalculateOrderLines("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailureCalculateOrderLines("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureCalculateOrderLines("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureCalculateOrderLines("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailureCalculateOrderLines("Ağ hatası : " + t.getMessage());
                }


            }
        });

    }
}
