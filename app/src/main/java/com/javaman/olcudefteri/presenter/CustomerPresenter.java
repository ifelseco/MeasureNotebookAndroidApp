package com.javaman.olcudefteri.presenter;

public interface CustomerPresenter {
    void customerSearch(String headerData,String text);
    void getCustomerOrders(String headerData,long customerId);
    void onDestroy();
}
