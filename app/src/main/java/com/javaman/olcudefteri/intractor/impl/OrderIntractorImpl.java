package com.javaman.olcudefteri.intractor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.intractor.OrderIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.service.OrderService;

import org.json.JSONObject;

import java.io.IOException;

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
                }else if (response.code() == 403) {
                    String message = "Sadece izinli kullanıcılar silme işlemi yapabilir.";
                    listener.onFailureUpdateOrder(message);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureUpdateOrder(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureUpdateOrder(message);
                }else{

                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailureUpdateOrder("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else if(jObjError.getString("responseMessage")!=null){
                            listener.onFailureUpdateOrder("Bir hata oluştu : "+jObjError.getString("responseMessage"));

                        }
                        else{
                            listener.onFailureUpdateOrder("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureUpdateOrder("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureDeleteOrder(message);
                }else{

                    //response [200 ,300) aralığında değil ise
                    try {
                        String errorBody=response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.has("baseModel")){
                            listener.onFailureDeleteOrder("Bir hata oluştu : "+jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        }else if(jObjError.getString("responseMessage")!=null){
                            listener.onFailureDeleteOrder("Bir hata oluştu : "+jObjError.getString("responseMessage"));
                        }
                        else{
                            listener.onFailureDeleteOrder("Bir hata oluştu : "+jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureDeleteOrder("Beklenmedik hata : "+e.getMessage()+"\n"+response.message());
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
