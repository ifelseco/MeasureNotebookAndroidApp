package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

/**
 * Created by javaman on 21.03.2018.
 */

public interface AddOrderLineView {

    void addOrderLine(OrderLineDetailModel orderLineDetailModel);
    void updateOrder(OrderDetailModel orderDetailModel);
    void calculateOrderLine(OrderLineDetailModel orderLineDetailModel);
}
