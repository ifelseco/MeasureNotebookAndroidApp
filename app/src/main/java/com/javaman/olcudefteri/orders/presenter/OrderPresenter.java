package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.model.OrderUpdateModel;

public interface OrderPresenter {
    void orderUpdate(OrderUpdateModel orderUpdateModel, String headerData);
    void orderDelete(long id , String headerData);
    void onDestroy();
}
