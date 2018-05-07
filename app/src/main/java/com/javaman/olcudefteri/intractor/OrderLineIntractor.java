package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.OrderLineSummaryResponseModel;

/**
 * Created by javaman on 12.03.2018.
 */

public interface OrderLineIntractor {



    interface onOrderLineProcessListener {
        void onSuccessGetOrderLines(OrderLineSummaryResponseModel orderLineSummaryReponseModel);
        void onFailureGetOrderLines(String message);
        void onSuccessDeleteOrderLine(OrderLineDetailModel orderLineDetailModel, String message);
        void onFailureDeleteOrderLine(String message);
        void navigateToLogin();
    }

    void sendGetOrderLineRequest(String xAuthToken, Long ordeRId, onOrderLineProcessListener listener);
    void deleteOrderLine(String xAuthToken, OrderLineDetailModel orderLineDetailModel, onOrderLineProcessListener listener);
}
