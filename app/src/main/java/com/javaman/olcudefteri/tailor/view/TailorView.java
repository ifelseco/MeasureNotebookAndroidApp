package com.javaman.olcudefteri.tailor.view;

import com.javaman.olcudefteri.home.view.HomeView;
import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryModel;

public interface TailorView extends HomeView{
    void getProcessedOrderFromServer();
    void getProcessingOrderFromServer();
    void getOrdersProcessing(OrderSummaryModel orderSummaryPageReponseModel);
    void getOrdersProcessed(OrderSummaryModel orderSummaryPageReponseModel);
    void sendOrderUpdateRequest(OrderDetailModel orderDetailModel);
    void updateList(OrderUpdateModel orderUpdateModel);
}
