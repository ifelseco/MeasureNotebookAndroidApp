package com.javaman.olcudefteri.presenter;

import com.javaman.olcudefteri.model.OrderUpdateModel;

public interface OrderPresenter {
    void orderUpdate(OrderUpdateModel orderUpdateModel, String headerData);
    void orderDelete(long id , String headerData);
    void onDestroy();
}
