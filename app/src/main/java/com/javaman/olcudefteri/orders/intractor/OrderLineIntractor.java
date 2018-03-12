package com.javaman.olcudefteri.orders.intractor;

import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;

/**
 * Created by javaman on 12.03.2018.
 */

public interface OrderLineIntractor {

    interface onGetOrderLineFinishedListener{
        void onSuccess(OrderLineSummaryResponseModel orderLineSummaryReponseModel);
        void onFailure(String message);
        void navigateToLogin();
    }

    void sendGetOrderLineRequest(String xAuthToken, Long ordeRId, onGetOrderLineFinishedListener listener);
}
