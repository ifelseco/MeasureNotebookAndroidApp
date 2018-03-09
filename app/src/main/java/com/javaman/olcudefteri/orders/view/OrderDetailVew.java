package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;

/**
 * Created by javaman on 07.03.2018.
 */

public interface OrderDetailVew {

    void getOrderLine(Long orderId);
    void showProgress();
    void hideProgress();
    void deleteOrder(Long orderId);
    void updateOrder(OrderDetailResponseModel orderDetailResponseModel);
}
