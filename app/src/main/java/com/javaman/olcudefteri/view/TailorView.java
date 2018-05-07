package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.view.HomeView;
import com.javaman.olcudefteri.model.OrderDetailModel;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;

public interface TailorView extends HomeView{
    void getProcessedOrderFromServer();
    void getProcessingOrderFromServer();
    void getOrdersProcessing(OrderSummaryModel orderSummaryPageReponseModel);
    void getOrdersProcessed(OrderSummaryModel orderSummaryPageReponseModel);
    void sendOrderUpdateRequest(OrderDetailModel orderDetailModel);
    void updateList(OrderUpdateModel orderUpdateModel);
}
