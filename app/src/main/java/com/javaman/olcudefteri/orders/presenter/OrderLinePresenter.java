package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

/**
 * Created by javaman on 12.03.2018.
 */

public interface OrderLinePresenter {
    void sendGetOrderLineRequest(String xAuthToken , Long orderId);
    void deleteOrderLine(String xAuthToken, OrderLineDetailModel orderLineDetailModel);
    void onDestroy();


}
