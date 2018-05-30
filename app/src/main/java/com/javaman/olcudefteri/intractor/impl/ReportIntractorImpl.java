package com.javaman.olcudefteri.intractor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.utill.ApiClient;
import com.javaman.olcudefteri.intractor.ReportIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.service.ReportService;
import com.javaman.olcudefteri.utill.NoConnectivityException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class ReportIntractorImpl implements ReportIntractor {

    private ReportService reportService;
    OrderSummaryModel orderSummaryModel;

    @Override
    public void getEndOfDay(String headerData, onReportProcess listener) {
        reportService = ApiClient.getClient().create(ReportService.class);
        orderSummaryModel = new OrderSummaryModel();
        Call<OrderSummaryModel> orderSummaryModelCall = reportService.getEndOfDayOrders(headerData);
        orderSummaryModelCall.enqueue(new Callback<OrderSummaryModel>() {
            @Override
            public void onResponse(Call<OrderSummaryModel> call, Response<OrderSummaryModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccess(orderSummaryModel);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                }else if(response.code() == 403){
                    String message = "Yetkisiz kullanıcı!";
                    listener.onFailure(message);
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailure(message);
                }else {
                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailure("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailure("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailure("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSummaryModel> call, Throwable t) {

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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailure("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailure("Ağ hatası : " + t.getMessage()+t.getClass());
                }


            }
        });
    }

    @Override
    public void getNextMeasure(String headerData, onReportProcess listener) {
        reportService = ApiClient.getClient().create(ReportService.class);
        orderSummaryModel = new OrderSummaryModel();
        Call<OrderSummaryModel> orderSummaryModelCall = reportService.getNextMeasure(headerData);
        orderSummaryModelCall.enqueue(new Callback<OrderSummaryModel>() {
            @Override
            public void onResponse(Call<OrderSummaryModel> call, Response<OrderSummaryModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccess(orderSummaryModel);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                }else if(response.code() == 403){
                    String message = "Yetkisiz kullanıcı!";
                    listener.onFailure(message);
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailure(message);
                }else {
                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailure("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailure("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailure("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSummaryModel> call, Throwable t) {

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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailure("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailure("Ağ hatası : " + t.getMessage()+t.getClass());
                }


            }
        });
    }

    @Override
    public void getNextDelivery(String headerData, onReportProcess listener) {
        reportService = ApiClient.getClient().create(ReportService.class);
        orderSummaryModel = new OrderSummaryModel();
        Call<OrderSummaryModel> orderSummaryModelCall = reportService.getNextDelivery(headerData);
        orderSummaryModelCall.enqueue(new Callback<OrderSummaryModel>() {
            @Override
            public void onResponse(Call<OrderSummaryModel> call, Response<OrderSummaryModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccess(orderSummaryModel);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                }else if(response.code() == 403){
                    String message = "Yetkisiz kullanıcı!";
                    listener.onFailure(message);
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailure(message);
                }else {
                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailure("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else{
                            listener.onFailure("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailure("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSummaryModel> call, Throwable t) {

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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailure("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailure("Ağ hatası : " + t.getMessage()+t.getClass());
                }


            }
        });
    }
}
