package com.javaman.olcudefteri.intractor.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.model.AddCustomerModel;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.utill.ApiClient;
import com.javaman.olcudefteri.intractor.CustomerIntractor;
import com.javaman.olcudefteri.model.ApiError;
import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.service.CustomerService;
import com.javaman.olcudefteri.utill.NoConnectivityException;

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
                        if (jObjError.has("baseModel") ) {
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureSearchCustomer("İnternet bağlantısı yok.");
                }
                else {

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
                        if (jObjError.has("baseModel")) {
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
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureCustomnerOrders("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailureCustomnerOrders("Ağ hatası : " + t.getMessage());
                }
            }
        });
    }

    @Override
    public void updateCustomer(CustomerDetailModel customerDetailModel, String headerData, onCustomerUpdateListener listener) {
        String nameSurname = customerDetailModel.getNameSurname();
        String fixedPhone = customerDetailModel.getFixedPhone();
        String mobilePhone = customerDetailModel.getMobilePhone();

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

                sendCustomer(headerData,customerDetailModel,listener);
            }
        }else{
            String existingPhone=findExistingPhone(fixedPhone,mobilePhone);

            if(existingPhone.equals(fixedPhone)){
                if(validatePhoneNumber(existingPhone)){
                    sendCustomer(headerData,customerDetailModel,listener);
                }else{
                    listener.onPhoneFormatError(false,true);
                }
            }else if(existingPhone.equals(mobilePhone)){
                if(validatePhoneNumber(existingPhone)){
                    sendCustomer(headerData,customerDetailModel,listener);
                }else{
                    listener.onPhoneFormatError(true,false);
                }
            }
        }

    }

    //http service codes
    private void sendCustomer(String headerData, CustomerDetailModel customerDetailModel, onCustomerUpdateListener listener) {
        customerService = ApiClient.getClient().create(CustomerService.class);
        String xAuthToken = headerData;
        Call<BaseModel> baseModelCall = customerService.updateCustomer(xAuthToken, customerDetailModel);
        baseModelCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

                //request servera ulaştı ve herhangi bir response döndü

                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    BaseModel baseModel = response.body();
                    listener.onSuccessCustomnerUpdate(customerDetailModel);
                    Log.d("Response body", response.body().toString());
                    Log.d("Auth response:", baseModel.toString());
                } else if (response.code() == 401) {
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureCustomnerUpdate(message);
                    listener.navigateLogin();
                } else if (response.code() == 503) {
                    String message = "Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureCustomnerUpdate(message);
                } else {

                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(errorBody);
                        if(jObjError.getString("responseMessage")!=null){

                            listener.onFailureCustomnerUpdate("Bir hata oluştu : "+jObjError.getString("responseMessage"));

                        }else {
                            listener.onFailureCustomnerUpdate("Bir hata oluştu : " + jObjError.getString("message"));
                        }

                    } catch (Exception e) {
                        listener.onFailureCustomnerUpdate("Beklenmedik hata : " + e.getMessage() + "\n" + response.message());
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

                        Log.d("Request Error :", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureCustomnerUpdate(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureCustomnerUpdate("Beklenmedik hata..." + e.getMessage());
                    }
                }else if (t instanceof NoConnectivityException) {
                    listener.onFailureCustomnerUpdate("İnternet bağlantısı yok.");
                }
                else {

                    listener.onFailureCustomnerUpdate("Ağ hatası : " + t.getMessage());
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
