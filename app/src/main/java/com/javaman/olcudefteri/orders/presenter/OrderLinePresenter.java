package com.javaman.olcudefteri.orders.presenter;

/**
 * Created by javaman on 12.03.2018.
 */

public interface OrderLinePresenter {
    void sendGetOrderLineRequest(String xAuthToken , Long orderId);
    void onDestroy();


}
