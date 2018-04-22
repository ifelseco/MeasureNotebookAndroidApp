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
        void onSuccessGetOrders(OrderSummaryReponseModel orderSummaryReponseModel);
        void onFailureGetOrders(String message);
        void navigateToLogin();
    }

    interface onGetFilterOrdersFinishedListener{
        void onSuccessGetFilterOrders(OrderSummaryReponseModel orderSummaryReponseModel, int orderStatus);
        void onFailureGetFilterOrders(String message, int orderStatus);
        void navigateToLogin();
    }

    interface onDeleteOrdersFinishedListener{
        void onSuccessDeleteOrders(String message, ArrayList<OrderDetailResponseModel> orders);
        void onFailureDeleteOrders(String message);
        void navigateToLogin();
    }

    void sendPageRequestToServer(String xAuthToken, PageModel pageModel, onGetOrdersFinishedListener listener);
    void sendDeleteOrderListRequestToServer(String xAuthToken , ArrayList<OrderDetailResponseModel> orders, onDeleteOrdersFinishedListener listener);
    void sendPageRequestWithFilter(String xAuthToken, int orderStatus,PageModel pageModel, onGetFilterOrdersFinishedListener listener);

}
