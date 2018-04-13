package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.event.OrderDeleteEvent;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;

public interface OrderView {
    void showProgress(String message);
    void hideProgress();
    void deleteOrder(OrderDeleteEvent orderDeleteEvent);
    void updateOrder(OrderUpdateModel orderUpdateModel);
    String getSessionIdFromPref();
    void navigateToLogin();
    void navigateToOrders();
    void checkSession();
    void showAlert(String message,boolean isError);
    void updateView(OrderUpdateModel orderUpdateModel);
}
