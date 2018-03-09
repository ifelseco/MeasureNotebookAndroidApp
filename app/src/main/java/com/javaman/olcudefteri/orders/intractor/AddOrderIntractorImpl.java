package com.javaman.olcudefteri.orders.intractor;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.orders.service.CustomerService;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.model.response.ApiError;

import com.javaman.olcudefteri.orders.model.AddCustomerModel;

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
            String xAuthToken=headerData;
            Call<AddCustomerResponse> addCustomerResponse = customerService.addCustomer(xAuthToken,addCustomerModel);
            addCustomerResponse.enqueue(new Callback<AddCustomerResponse>() {
                @Override
                public void onResponse(Call<AddCustomerResponse> call, Response<AddCustomerResponse> response) {

                    //request servera ulaştı ve herhangi bir response döndü

                    if (response.isSuccessful()) {

                        //response [200 ,300) aralığında ise

                        AddCustomerResponse addCustomerResponse = response.body();





                        listener.onSuccess();


                        Log.d("Response body", response.body().toString());
                        Log.d("Auth response:", addCustomerResponse.toString());



                    } else {

                        //response [200 ,300) aralığında değil ise

                        Gson gson = new GsonBuilder().create();

                        try {

                            String errorBody = response.errorBody().string();

                            ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                            Log.d("Hata Mesaj:", apiError.getStatus() +" "+ apiError.getMessage());
                            listener.onFailure(apiError.getStatus() +" "+ apiError.getMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onFailure("Beklenmedik hata..." + e.getMessage());
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

                            Log.d("Request Error :", apiError.getStatus() +" "+ apiError.getMessage());
                            listener.onFailure(apiError.getStatus() +" "+ apiError.getMessage());

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
