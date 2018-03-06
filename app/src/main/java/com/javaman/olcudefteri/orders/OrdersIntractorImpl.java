package com.javaman.olcudefteri.orders;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.api.ApiUtils;
import com.javaman.olcudefteri.login.LoginService;
import com.javaman.olcudefteri.model.OrdersDeleteModel;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.response_model.ApiError;
import com.javaman.olcudefteri.model.response_model.AuthResponse;
import com.javaman.olcudefteri.model.response_model.BaseResponse;
import com.javaman.olcudefteri.model.response_model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.response_model.OrderSummaryReponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrdersIntractorImpl implements OrdersIntractor {

    OrdersService ordersService;
    OrderSummaryReponseModel orderSummaryReponseModel;
    BaseResponse baseResponse;
    List<OrderDetailResponseModel> orderDetailResponseModels=new ArrayList<>();
    ArrayList<OrderDetailResponseModel> deleteOrders=new ArrayList<>();

    @Override
    public void sendPageRequestToServer(String xAuthToken, PageModel pageModel,final onGetOrdersFinishedListener listener) {

        ordersService = ApiClient.getClient().create(OrdersService.class);
        orderSummaryReponseModel=new OrderSummaryReponseModel();
        Call<OrderSummaryReponseModel> orderSummaryReponseModelCall=ordersService.getOrders(xAuthToken,pageModel);
        orderSummaryReponseModelCall.enqueue(new Callback<OrderSummaryReponseModel>() {
            @Override
            public void onResponse(Call<OrderSummaryReponseModel> call, Response<OrderSummaryReponseModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryReponseModel = response.body();
                    //orderSummaryReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccess(orderSummaryReponseModel);
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
            public void onFailure(Call<OrderSummaryReponseModel> call, Throwable t) {

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

    @Override
    public void sendDeleteOrderListRequestToServer(String xAuthToken, final ArrayList<OrderDetailResponseModel> orders, final onDeleteOrdersFinishedListener listener) {
        ordersService = ApiClient.getClient().create(OrdersService.class);
        baseResponse=new BaseResponse();

        OrdersDeleteModel ordersDeleteModel=new OrdersDeleteModel();
        ArrayList<Long> orderIds=new ArrayList<>();
        for(OrderDetailResponseModel order:orders){
            orderIds.add(order.getId());
        }
        ordersDeleteModel.setOrderIds(orderIds);
        Call<BaseResponse> baseResponseCall=ordersService.deleteOrders(xAuthToken,ordersDeleteModel);

        baseResponseCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()){
                    String message=response.body().getResponseMessage();
                    listener.onSuccess(message,orders);
                }else{
                    if(response.code()==403){
                        String message="Sadece yönetici izni olanlar silme işlemi yapabilir.";
                        listener.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
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

