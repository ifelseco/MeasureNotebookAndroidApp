package com.javaman.olcudefteri.orders.view;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderView {

    void showProgress();
    void hideProgress();
    void setNameEmptyError();
    void setPhoneEmptyError();
    void navigateToOrder();
    void showAlert(String message);
    void checkSession();
    String getSessionIdFromPref();
}
