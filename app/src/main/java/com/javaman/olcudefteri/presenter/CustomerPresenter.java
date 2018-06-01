package com.javaman.olcudefteri.presenter;

import com.javaman.olcudefteri.model.CustomerDetailModel;

public interface CustomerPresenter {
    void customerSearch(String headerData,String text);
    void getCustomerOrders(String headerData,long customerId);
    void updateCustomer(CustomerDetailModel customerDetailModel, String headerData);
    void onDestroy();
}
