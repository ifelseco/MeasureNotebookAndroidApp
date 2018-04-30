package com.javaman.olcudefteri.orders.view;


import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

public interface OrderLineView{

    void showProgress(String message);
    void hideProgress();
    void deleteOrderLne(OrderLineDetailModel orderLineDetailModel);
    void updateOrderLine(OrderLineDetailModel orderLineDetailModel);
    String getSessionIdFromPref();
    void navigateToLogin();
    void checkSession();
    void showAlert(String message,boolean isError,boolean isToast);
    void deleteItemFromAdapter(OrderLineDetailModel orderLineDetailModel);
    void updateAdapter(OrderLineDetailModel orderLineDetailModel);



}
