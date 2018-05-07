package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.CalculationResponse;

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
