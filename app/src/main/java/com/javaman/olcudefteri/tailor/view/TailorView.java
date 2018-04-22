package com.javaman.olcudefteri.tailor.view;

import com.javaman.olcudefteri.home.view.HomeView;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;

public interface TailorView extends HomeView{
    void getProcessedOrderFromServer();
    void getProcessingOrderFromServer();
    void getOrdersProcessing(OrderSummaryReponseModel orderSummaryReponseModel);
    void getOrdersProcessed(OrderSummaryReponseModel orderSummaryReponseModel);

}
