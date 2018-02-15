package com.javaman.olcudefteri.add_order;

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
}
