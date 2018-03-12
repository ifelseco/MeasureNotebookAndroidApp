package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;

import java.util.ArrayList;

/**
 * Created by javaman on 07.03.2018.
 */

public interface OrderDetailVew {

    void sendGetOrderLineRequest(Long orderId);
    void showProgress();
    void hideProgress();
    void deleteOrder(Long orderId);
    void updateOrder(OrderDetailResponseModel orderDetailResponseModel);
    String getSessionIdFromPref();
    void navigateToLogin();
    void checkSession();
    void showAlert(String message);
    void getOrderLines(OrderLineSummaryResponseModel orderLineSummaryResponseModel);
}
