package com.javaman.olcudefteri.add_order;

import com.javaman.olcudefteri.model.CustomerDetailModel;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderPresenter {

    void addCustomer(CustomerDetailModel customerDetailModel , String headerData);
    void onDestroy();
}
