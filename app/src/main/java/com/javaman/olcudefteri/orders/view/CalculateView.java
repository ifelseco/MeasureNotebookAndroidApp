package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.CalculationResponse;

/**
 * Created by javaman on 26.03.2018.
 */

public interface CalculateView {

    void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel);
    void showAlert(String message);
    void showProgress();
    void hideProgress();
    String getSessionIdFromPref();
    void updateAmount(CalculationResponse calculationResponse);
}
