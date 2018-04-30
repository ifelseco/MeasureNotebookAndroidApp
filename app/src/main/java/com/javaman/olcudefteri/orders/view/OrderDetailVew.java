package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;

/**
 * Created by javaman on 07.03.2018.
 */

public interface OrderDetailVew {

    void sendGetOrderLineRequest(Long orderId,boolean isUpdate);
    void showProgress();
    void hideProgress();
    String getSessionIdFromPref();
    void navigateToLogin();
    void checkSession();
    void showAlert(String message);
    void getOrderLines(OrderLineSummaryResponseModel orderLineSummaryResponseModel);


}
