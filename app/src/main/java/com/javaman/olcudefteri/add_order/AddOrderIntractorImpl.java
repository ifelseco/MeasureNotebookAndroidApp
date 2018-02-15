package com.javaman.olcudefteri.add_order;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.AddCustomerResponse;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.ApiError;
import com.javaman.olcudefteri.api.ApiUtils;
import com.javaman.olcudefteri.api.AuthResponse;
import com.javaman.olcudefteri.login.LoginService;
import com.javaman.olcudefteri.model.CustomerDetailModel;

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
    public void addCustomer(CustomerDetailModel customerDetailModel,String headerData, final onSendCustomerListener listener) {
        if (TextUtils.isEmpty(customerDetailModel.getNameSurname())) {
            listener.onNameEmptyError();
        } else if (TextUtils.isEmpty(customerDetailModel.getMobilePhone()) || TextUtils.isEmpty(customerDetailModel.getFixedPhone())) {
            listener.onPhoneEmptyError();
        } else {


            customerService = ApiClient.getClient().create(CustomerService.class);
            String contentType = "application/json";
            String xAuthToken=headerData;


            Call<AddCustomerResponse> addCustomerResponse = customerService.addCustomer(contentType,xAuthToken,customerDetailModel);

            addCustomerResponse.enqueue(new Callback<AddCustomerResponse>() {
                @Override
                public void onResponse(Call<AddCustomerResponse> call, Response<AddCustomerResponse> response) {

                    //request servera ulaştı ve herhangi bir response döndü

                    if (response.isSuccessful()) {

                        //response [200 ,300) aralığında ise

                        AddCustomerResponse addCustomerResponse = response.body();

                        Log.d("Response body", response.body().toString());
                        Log.d("Auth response:", addCustomerResponse.toString());


                        listener.onSuccess();

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
