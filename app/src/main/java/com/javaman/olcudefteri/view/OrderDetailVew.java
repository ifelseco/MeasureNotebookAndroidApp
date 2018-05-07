package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.OrderLineSummaryResponseModel;

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
