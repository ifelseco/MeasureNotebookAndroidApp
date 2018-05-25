package com.javaman.olcudefteri.intractor.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.intractor.CustomerIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.service.CustomerService;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class CustomerIntractorImpl implements CustomerIntractor {

    CustomerService customerService;

    @Override
    public void customerSearch(String headerData, String text, onCustomerSearchListener listener) {
        customerService = ApiClient.getClient().create(CustomerService.class);
        Call<CustomerSummaryModel> customerSummaryModelCall = customerService.customerSearch(headerData, text);
        customerSummaryModelCall.enqueue(new Callback<CustomerSummaryModel>() {
            @Override
            public void onResponse(Call<CustomerSummaryModel> call, Response<CustomerSummaryModel> response) {
                if (response.isSuccessful()) {
                    CustomerSummaryModel customerSummaryModel = response.body();
                    listener.onSuccessCustomnerSearch(customerSummaryModel);
                } else if (response.code() == 403) {
                    String message = "Yetkisiz kullanıcı...";
                    listener.onFailureSearchCustomer(message);
                } else if (response.code() == 401) {
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureSearchCustomer(message);
                    listener.navigateLogin();
                } else if (response.code() == 503) {
                    String message = "Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureSearchCustomer(message);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if (jObjError.get("baseModel") != null) {
                            listener.onFailureSearchCustomer("Bir hata oluştu : " + jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        } else if (jObjError.getString("responseMessage") != null) {
                            listener.onFailureSearchCustomer("Bir hata oluştu : " + jObjError.getString("responseMessage"));

                        } else {
                            listener.onFailureSearchCustomer("Bir hata oluştu : " + jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureSearchCustomer("Beklenmedik hata : " + e.getMessage() + "\n" + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSummaryModel> call, Throwable t) {
                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        listener.onFailureSearchCustomer(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureSearchCustomer("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureSearchCustomer("Ağ hatası : " + t.getMessage());
                }
            }
        });
    }

    @Override
    public void getCustomerOrders(String headerData, long customerId, onCustomerOrdersListener listener) {
        customerService=ApiClient.getClient().create(CustomerService.class);
        Call<OrderSummaryModel> orderSummaryModelCall=customerService.getCustomerOrders(headerData,customerId);
        orderSummaryModelCall.enqueue(new Callback<OrderSummaryModel>() {
            @Override
            public void onResponse(Call<OrderSummaryModel> call, Response<OrderSummaryModel> response) {

                if (response.isSuccessful()) {
                    OrderSummaryModel orderSummaryModel= response.body();
                    listener.onSuccessCustomnerOrders(orderSummaryModel);
                } else if (response.code() == 403) {
                    String message = "Yetkisiz kullanıcı...";
                    listener.onFailureCustomnerOrders(message);
                } else if (response.code() == 401) {
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureCustomnerOrders(message);
                    listener.navigateLogin();
                } else if (response.code() == 503) {
                    String message = "Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureCustomnerOrders(message);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if (jObjError.get("baseModel") != null) {
                            listener.onFailureCustomnerOrders("Bir hata oluştu : " + jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        } else if (jObjError.getString("responseMessage") != null) {
                            listener.onFailureCustomnerOrders("Bir hata oluştu : " + jObjError.getString("responseMessage"));

                        } else {
                            listener.onFailureCustomnerOrders("Bir hata oluştu : " + jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureCustomnerOrders("Beklenmedik hata : " + e.getMessage() + "\n" + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSummaryModel> call, Throwable t) {
                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        listener.onFailureCustomnerOrders(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureCustomnerOrders("Beklenmedik hata..." + e.getMessage());
                    }
                } else {

                    listener.onFailureCustomnerOrders("Ağ hatası : " + t.getMessage());
                }
            }
        });
    }
}
