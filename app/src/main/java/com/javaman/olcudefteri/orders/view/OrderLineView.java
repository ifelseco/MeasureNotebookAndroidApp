package com.javaman.olcudefteri.orders.view;


import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

public interface OrderLineView{

    void showProgress(String message);
    void hideProgress();
    void deleteOrderLne(OrderLineDetailModel orderLineDetailModel);
    void updateOrderLine();
    String getSessionIdFromPref();
    void navigateToLogin();
    void checkSession();
    void showAlert(String message,boolean isError,boolean isToast);
    void updateView(OrderLineDetailModel orderLineDetailModel);
}
