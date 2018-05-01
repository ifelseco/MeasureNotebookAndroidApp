package com.javaman.olcudefteri.orders.intractor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaman.olcudefteri.api.ApiClient;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.OrdersDeleteModel;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.api.model.response.ApiError;
import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryPageReponseModel;
import com.javaman.olcudefteri.orders.service.OrderService;
import com.javaman.olcudefteri.orders.service.OrdersService;

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
    OrderService orderService;
    OrderSummaryPageReponseModel orderSummaryPageReponseModel;
    OrderSummaryModel orderSummaryModel;
    BaseModel baseResponse;
    List<OrderDetailResponseModel> orderDetailResponseModels = new ArrayList<>();
    ArrayList<OrderDetailResponseModel> deleteOrders = new ArrayList<>();

    @Override
    public void sendPageRequestToServer(String xAuthToken, PageModel pageModel, final onGetOrdersFinishedListener listener) {

        ordersService = ApiClient.getClient().create(OrdersService.class);
        orderSummaryPageReponseModel = new OrderSummaryPageReponseModel();
        Call<OrderSummaryPageReponseModel> orderSummaryReponseModelCall = ordersService.getOrders(xAuthToken, pageModel);
        orderSummaryReponseModelCall.enqueue(new Callback<OrderSummaryPageReponseModel>() {
            @Override
            public void onResponse(Call<OrderSummaryPageReponseModel> call, Response<OrderSummaryPageReponseModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryPageReponseModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccessGetOrders(orderSummaryPageReponseModel);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureGetOrders(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureGetOrders(message);
                }

                else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureGetOrders(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetOrders("Beklenmedik hata..." + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSummaryPageReponseModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureGetOrders(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetOrders("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureGetOrders("Ağ hatası : " + t.getMessage()+t.getClass());
                }


            }
        });


    }

    @Override
    public void sendDeleteOrderListRequestToServer(String xAuthToken, final ArrayList<OrderDetailResponseModel> orders, final onDeleteOrdersFinishedListener listener) {
        ordersService = ApiClient.getClient().create(OrdersService.class);
        baseResponse = new BaseModel();

        OrdersDeleteModel ordersDeleteModel = new OrdersDeleteModel();
        ArrayList<Long> orderIds = new ArrayList<>();
        for (OrderDetailResponseModel order : orders) {
            orderIds.add(order.getId());
        }
        ordersDeleteModel.setOrderIds(orderIds);
        Call<BaseModel> baseResponseCall = ordersService.deleteOrders(xAuthToken, ordersDeleteModel);

        baseResponseCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getResponseMessage();
                    listener.onSuccessDeleteOrders(message, orders);
                } else if (response.code() == 403) {
                    String message = "Sadece yönetici izni olanlar silme işlemi yapabilir.";
                    listener.onFailureDeleteOrders(message);
                }else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureDeleteOrders(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureDeleteOrders(message);
                }
                else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureDeleteOrders(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureDeleteOrders("Beklenmedik hata..." + e.getMessage());
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
                    listener.onFailureDeleteOrders(apiError.getStatus() + " " + apiError.getMessage());

                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailureDeleteOrders("Beklenmedik hata..." + e.getMessage());

                }
            } else {

                listener.onFailureDeleteOrders("Ağ hatası : " + t.getMessage());
            }


        }
    });
}

    @Override
    public void sendPageRequestWithFilter(String xAuthToken, final int orderStatus, PageModel pageModel, final onGetFilterOrdersFinishedListener listener) {
        ordersService = ApiClient.getClient().create(OrdersService.class);
        orderSummaryPageReponseModel = new OrderSummaryPageReponseModel();
        Call<OrderSummaryPageReponseModel> orderSummaryReponseModelCall = ordersService.getOrdersWithFilter(xAuthToken, orderStatus,pageModel);
        orderSummaryReponseModelCall.enqueue(new Callback<OrderSummaryPageReponseModel>() {
            @Override
            public void onResponse(Call<OrderSummaryPageReponseModel> call, Response<OrderSummaryPageReponseModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryPageReponseModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccessGetFilterOrders(orderSummaryPageReponseModel,orderStatus);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureGetFilterOrders(message,orderStatus);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureGetFilterOrders(message,orderStatus);
                }
                else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureGetFilterOrders(apiError.getStatus() + " " + apiError.getMessage(),orderStatus);
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetFilterOrders("Beklenmedik hata..." + e.getMessage(),orderStatus);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSummaryPageReponseModel> call, Throwable t) {

                //request servera ulaşamadı yada request oluşurken herhangi bir exception oluştu

                if (t instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) t).response().errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);

                        Log.d("Request Error :", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureGetFilterOrders(apiError.getStatus() + " " + apiError.getMessage(),orderStatus);

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetFilterOrders("Beklenmedik hata..." + e.getMessage(),orderStatus);

                    }
                } else {

                    listener.onFailureGetFilterOrders("Ağ hatası : " + t.getMessage()+t.getClass(),orderStatus);
                }


            }
        });

    }

    @Override
    public void getTailorOrdersWithFilter(String xAuthToken, final int orderStatus, final onGetTailorFilterOrdersFinishedListener listener) {
        ordersService = ApiClient.getClient().create(OrdersService.class);
        orderSummaryModel = new OrderSummaryModel();
        Call<OrderSummaryModel> orderSummaryModelCall = ordersService.getTailorOrderWithFilter(xAuthToken, orderStatus);
        orderSummaryModelCall.enqueue(new Callback<OrderSummaryModel>() {
            @Override
            public void onResponse(Call<OrderSummaryModel> call, Response<OrderSummaryModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccessGetTailorFilterOrders(orderSummaryModel,orderStatus);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureGetTailorFilterOrders(message);
                    listener.navigateToLogin();
                } else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureGetTailorFilterOrders(message);
                }
                else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureGetTailorFilterOrders(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetTailorFilterOrders("Beklenmedik hata..." + e.getMessage());
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
                        listener.onFailureGetTailorFilterOrders(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureGetTailorFilterOrders("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureGetTailorFilterOrders("Ağ hatası : " + t.getMessage()+t.getClass());
                }


            }
        });
    }

    @Override
    public void orderUpdate(String xAuthToken, final OrderUpdateModel orderUpdateModel, final OrdersIntractor.onOrderProcessListener listener) {
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
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureUpdateOrder(message);
                    listener.navigateToLogin();
                }else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureUpdateOrder(message);
                }
                else {

                    //response [200 ,300) aralığında değil ise

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = response.errorBody().string();

                        BaseModel apiError = gson.fromJson(errorBody, BaseModel.class);

                        Log.d("Hata Mesaj:", response.code() +" "+ apiError.getResponseMessage());
                        listener.onFailureUpdateOrder("Server hatası :"+response.code() +"\n"+ apiError.getResponseMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureUpdateOrder("Beklenmedik hata..." + e.getMessage());
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
    public void orderSearch(String xAuthToken, String orderNumber, onSearchOrderFinishedListener listener) {
        ordersService = ApiClient.getClient().create(OrdersService.class);
        orderSummaryModel = new OrderSummaryModel();
        Call<OrderSummaryModel> orderSummaryModelCall = ordersService.orderSearch(xAuthToken, orderNumber);
        orderSummaryModelCall.enqueue(new Callback<OrderSummaryModel>() {
            @Override
            public void onResponse(Call<OrderSummaryModel> call, Response<OrderSummaryModel> response) {
                //request servera ulaştı ve herhangi bir response döndü
                if (response.isSuccessful()) {
                    //response [200 ,300) aralığında ise
                    orderSummaryModel = response.body();
                    //orderSummaryPageReponseModel.getOrderDetailPage().setContent(orderDetailResponseModels);
                    listener.onSuccessSearchOder(orderSummaryModel);
                } else if(response.code() == 401){
                    String message = "Oturum zaman aşımına uğradı ,tekrar giriş yapınız!";
                    listener.onFailureSearchOder(message);
                    listener.navigateToLogin();
                } else if(response.code()==503){
                    String message="Servis şuanda çalışmıyor, daha sonra tekrar deneyiniz.";
                    listener.onFailureSearchOder(message);
                }
                else {
                    //response [200 ,300) aralığında değil ise
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errorBody = response.errorBody().string();
                        ApiError apiError = gson.fromJson(errorBody, ApiError.class);
                        Log.d("Hata Mesaj:", apiError.getStatus() + " " + apiError.getMessage());
                        listener.onFailureSearchOder(apiError.getStatus() + " " + apiError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureSearchOder("Beklenmedik hata..." + e.getMessage());
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
                        listener.onFailureSearchOder(apiError.getStatus() + " " + apiError.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailureSearchOder("Beklenmedik hata..." + e.getMessage());

                    }
                } else {

                    listener.onFailureSearchOder("Ağ hatası : " + t.getMessage()+t.getClass());
                }


            }
        });
    }


}

