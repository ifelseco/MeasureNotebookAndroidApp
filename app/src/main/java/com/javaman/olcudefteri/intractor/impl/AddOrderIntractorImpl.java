package com.javaman.olcudefteri.intractor.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.intractor.AddOrderIntractor;
import com.javaman.olcudefteri.service.CustomerService;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.utill.ApiClient;
import com.javaman.olcudefteri.model.ApiError;

import com.javaman.olcudefteri.model.AddCustomerModel;
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

public class AddOrderIntractorImpl implements AddOrderIntractor {

    CustomerService customerService;

    @Override
    public void addNewCustomer(AddCustomerModel addCustomerModel, String headerData, final onSendCustomerListener listener) {
        String nameSurname = addCustomerModel.getCustomerDetailModel().getNameSurname();
        String fixedPhone = addCustomerModel.getCustomerDetailModel().getFixedPhone();
        String mobilePhone = addCustomerModel.getCustomerDetailModel().getMobilePhone();

        if (TextUtils.isEmpty(nameSurname)) {
            listener.onNameEmptyError();
        }else if(twoPhoneNotExist(fixedPhone,mobilePhone)){
            listener.onPhoneEmptyError();
        }else if (twoPhoneExist(fixedPhone, mobilePhone)) {
            if (validatePhoneNumber(fixedPhone) && !validatePhoneNumber(mobilePhone)) {
                listener.onPhoneFormatError(true, false);
            } else if (!validatePhoneNumber(fixedPhone) && !validatePhoneNumber(mobilePhone)) {
                listener.onPhoneFormatError(true, true);
            } else if (!validatePhoneNumber(fixedPhone) && validatePhoneNumber(mobilePhone)) {
                listener.onPhoneFormatError(false, true);
            } else {

                sendCustomer(headerData,addCustomerModel,listener);
            }
        }else{
            String existingPhone=findExistingPhone(fixedPhone,mobilePhone);

            if(existingPhone.equals(fixedPhone)){
             if(validatePhoneNumber(existingPhone)){
                 sendCustomer(headerData,addCustomerModel,listener);
             }else{
                 listener.onPhoneFormatError(false,true);
             }
            }else if(existingPhone.equals(mobilePhone)){
               if(validatePhoneNumber(existingPhone)){
                   sendCustomer(headerData,addCustomerModel,listener);
               }else{
                   listener.onPhoneFormatError(true,false);
               }
            }
        }
    }




    @Override
    public void addOrderToCustomer(AddCustomerModel addCustomerModel, String headerData, onSendCustomerListener listener) {
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
                } else if (response.code() == 401) {
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                } else if (response.code() == 503) {
                    String message = "Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailure(message);
                } else {

                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if (jObjError.has("baseModel")) {
                            listener.onFailure("Bir hata oluştu : " + jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        } else {
                            listener.onFailure("Bir hata oluştu : " + jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailure("Beklenmedik hata : " + e.getMessage() + "\n" + response.message());
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailure("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailure("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }

    //http service codes
    private void sendCustomer(String headerData,AddCustomerModel addCustomerModel,onSendCustomerListener listener) {
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
                } else if (response.code() == 401) {
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailure(message);
                    listener.navigateToLogin();
                } else if (response.code() == 503) {
                    String message = "Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailure(message);
                } else {

                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if (jObjError.has("baseModel")) {
                            listener.onFailure("Bir hata oluştu : " + jObjError.getJSONObject("baseModel").getString("responseMessage"));
                        } else {
                            listener.onFailure("Bir hata oluştu : " + jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailure("Beklenmedik hata : " + e.getMessage() + "\n" + response.message());
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailure("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailure("Ağ hatası : " + t.getMessage());
                }


            }
        });
    }

    private String findExistingPhone(String fixedPhone, String mobilePhone) {
        if(!TextUtils.isEmpty(fixedPhone)){
            return fixedPhone;
        }else {
            return mobilePhone;
        }
    }

    private boolean validatePhoneNumber(String phone) {
        if (phone.length() == 11) {
            if (phone.matches("[0-9]+")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean twoPhoneExist(String fixedPhone, String mobilePhone) {
        if (TextUtils.isEmpty(fixedPhone) && TextUtils.isEmpty(mobilePhone)) {
            return false;
        } else if(!TextUtils.isEmpty(fixedPhone) && !TextUtils.isEmpty(mobilePhone)){
            return true;
        }else{
            return false;
        }
    }

    private boolean twoPhoneNotExist(String fixedPhone, String mobilePhone) {
        if (TextUtils.isEmpty(fixedPhone) && TextUtils.isEmpty(mobilePhone)) {
            return true;
        } else {
            return false;
        }
    }
}
