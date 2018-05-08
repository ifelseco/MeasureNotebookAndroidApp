package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.OrderSummaryModel;

public interface ReportView extends BaseView {

    void getEndOfDay();
    void nextMeasure();
    void nextDelivery();
    void getOrders(OrderSummaryModel orderSummaryModel);
}
