package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.AddCustomerResponse;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderView {

    void showProgress();
    void hideProgress();
    void setNameEmptyError();
    void setPhoneEmptyError();
    void navigateToOrder(AddCustomerResponse addCustomerResponse);
    void showAlert(String message);
    String getSessionIdFromPref();
}
