package com.javaman.olcudefteri.orders.view;

import android.widget.Toast;

import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by javaman on 07.03.2018.
 */

public interface OrderDetailVew {

    void sendGetOrderLineRequest(Long orderId);
    void showProgress();
    void hideProgress();
    String getSessionIdFromPref();
    void navigateToLogin();
    void checkSession();
    void showAlert(String message);
    void getOrderLines(OrderLineSummaryResponseModel orderLineSummaryResponseModel);


}
