package com.javaman.olcudefteri.orders.intractor;

import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryPageReponseModel;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersIntractor {

    interface onGetOrdersFinishedListener{
        void onSuccessGetOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel);
        void onFailureGetOrders(String message);
        void navigateToLogin();
    }

    interface onGetFilterOrdersFinishedListener{
        void onSuccessGetFilterOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel, int orderStatus);
        void onFailureGetFilterOrders(String message, int orderStatus);
        void navigateToLogin();
    }

    interface onGetTailorFilterOrdersFinishedListener{
        void onSuccessGetTailorFilterOrders(OrderSummaryModel orderSummaryModel, int orderStatus);
        void onFailureGetTailorFilterOrders(String message);
        void navigateToLogin();
    }

    interface onDeleteOrdersFinishedListener{
        void onSuccessDeleteOrders(String message, ArrayList<OrderDetailResponseModel> orders);
        void onFailureDeleteOrders(String message);
        void navigateToLogin();
    }

    interface onOrderProcessListener {
        void onSuccessUpdateOrder(BaseModel baseModel, OrderUpdateModel orderUpdateModel);
        void onFailureUpdateOrder(String message);
    }

    void sendPageRequestToServer(String xAuthToken, PageModel pageModel, onGetOrdersFinishedListener listener);
    void sendDeleteOrderListRequestToServer(String xAuthToken , ArrayList<OrderDetailResponseModel> orders, onDeleteOrdersFinishedListener listener);
    void sendPageRequestWithFilter(String xAuthToken, int orderStatus,PageModel pageModel, onGetFilterOrdersFinishedListener listener);
    void getTailorOrdersWithFilter(String xAuthToken, int orderStatus, onGetTailorFilterOrdersFinishedListener listener);
    void orderUpdate(String xAuthToken, OrderUpdateModel orderUpdateModel , onOrderProcessListener listener);

}
