package com.javaman.olcudefteri.orders.intractor;

import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersIntractor {

    interface onGetOrdersFinishedListener{
        void onSuccess(OrderSummaryReponseModel orderSummaryReponseModel);
        void onFailure(String message);
        void navigateToLogin();
    }

    interface onDeleteOrdersFinishedListener{
        void onSuccess(String message, ArrayList<OrderDetailResponseModel> orders);
        void onFailure(String message);
        void navigateToLogin();
    }

    void sendPageRequestToServer(String xAuthToken, PageModel pageModel, onGetOrdersFinishedListener listener);
    void sendDeleteOrderListRequestToServer(String xAuthToken , ArrayList<OrderDetailResponseModel> orders, onDeleteOrdersFinishedListener listener);
}
