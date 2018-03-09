package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.model.AddCustomerModel;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderPresenter {

    void addCustomer(AddCustomerModel addCustomerModel , String headerData);
    void onDestroy();
}
