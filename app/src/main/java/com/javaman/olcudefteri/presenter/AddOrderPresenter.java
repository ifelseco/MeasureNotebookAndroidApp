package com.javaman.olcudefteri.presenter;

import com.javaman.olcudefteri.model.AddCustomerModel;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderPresenter {

    void addCustomer(AddCustomerModel addCustomerModel , String headerData);
    void onDestroy();
}
