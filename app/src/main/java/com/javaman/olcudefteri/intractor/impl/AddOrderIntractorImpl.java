package com.javaman.olcudefteri.intractor.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.intractor.AddOrderIntractor;
import com.javaman.olcudefteri.service.CustomerService;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.model.ApiError;

import com.javaman.olcudefteri.model.AddCustomerModel;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 15.02.2018.
 */

public class AddOrderIntractorImpl implements AddOrderIntractor {

    CustomerService customerService;

    @Override
    public void addCustomer(AddCustomerModel addCustomerModel, String headerData, final onSendCustomerListener listener) {
        if (TextUtils.isEmpty(addCustomerModel.getCustomerDetailModel().getNameSurname())) {
            listener.onNameEmptyError();
        } else if (TextUtils.isEmpty(addCustomerModel.getCustomerDetailModel().getMobilePhone()) ||
                TextUtils.isEmpty(addCustomerModel.getCustomerDetailModel().getFixedPhone())) {
            listener.onPhoneEmptyError();
        } else {


            customerService = ApiClient.getClient().create(CustomerService.class);
            String xAuthToken = headerData;
            Call<AddCustomerResponse> addCustomerResponse = customerService.addCustomer(xAuthToken, addCustomerModel);
            addCustomerResponse.enqueue(new Callback<AddCustomerResponse>() {
                @Override
                public void onResponse(Call<AddCustomerResponse> call, Response<AddCustomerResponse> response) {

                    //request servera ulaştı ve herhangi bir response döndü

                    if (response.isSuccessful()) {
                        //response [200 ,300) aralığında ise
                        AddCustomerResponse addCustomerResponse = response.body();
                        listener.onSuccess(addCustomerResponse);
                        Log.d("Response body", response.body().toString());
                        Log.d("Auth response:", addCustomerResponse.toString());
                    }else if(response.code() == 401){
                        String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                        listener.onFailure(message);
                        listener.navigateToLogin();
                    }else if(response.code()==503){
                        String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                        listener.onFailure(message);
                    }else {

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if(jObjError.get("baseModel")!=null){
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
                public void onFailure(Call<AddCustomerResponse> call, Throwable t) {

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
}
