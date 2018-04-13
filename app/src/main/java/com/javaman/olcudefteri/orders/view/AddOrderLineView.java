package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.response.AddOrderLineResponse;

/**
 * Created by javaman on 21.03.2018.
 */

public interface AddOrderLineView {

    void addOrderLine(OrderLineDetailModel orderLineDetailModel);
    void addOrderLineList(AddOrderLineDetailListModel orderLineDetailListModel);
    void updateOrder(OrderUpdateModel orderUpdateModel);

    String getSessionIdFromPref();
    void navigateToLogin();
    void checkSession();
    void showAlert(String message,boolean isError);
    void showProgress();
    void hideProgress();
    void updateCart(double orderTotalAmount);
    void updateView(OrderUpdateModel orderUpdateModel);

}
