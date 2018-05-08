package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.OrderSummaryModel;

public interface ReportIntractor {

    interface onReportProcess{
        void onSuccess(OrderSummaryModel orderSummaryModel);
        void onFailure(String message);
        void navigateToLogin();
    }

    void getEndOfDay(String headerData , onReportProcess listener);
    void getNextMeasure(String headerData , onReportProcess listener);
    void getNextDelivery(String headerData , onReportProcess listener);
}
