package com.javaman.olcudefteri.orders;

import com.javaman.olcudefteri.model.OrdersDeleteModel;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.response_model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.response_model.OrderSummaryReponseModel;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersIntractor {

    interface onGetOrdersFinishedListener{
        void onSuccess(OrderSummaryReponseModel orderSummaryReponseModel);
        void onFailure(String message);
    }

    interface onDeleteOrdersFinishedListener{
        void onSuccess(String message, ArrayList<OrderDetailResponseModel> orders);
        void onFailure(String message);
    }

    void sendPageRequestToServer(String xAuthToken, PageModel pageModel, onGetOrdersFinishedListener listener);
    void sendDeleteOrderListRequestToServer(String xAuthToken , ArrayList<OrderDetailResponseModel> orders, onDeleteOrdersFinishedListener listener);
}
