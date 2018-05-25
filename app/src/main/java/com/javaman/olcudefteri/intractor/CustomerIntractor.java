package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;

public interface CustomerIntractor {

    interface onCustomerSearchListener{
        void onSuccessCustomnerSearch(CustomerSummaryModel customerSummaryModel);
        void onFailureSearchCustomer(String message);
        void navigateLogin();
    }

    interface onCustomerOrdersListener{
        void onSuccessCustomnerOrders(OrderSummaryModel orderSummaryModel);
        void onFailureCustomnerOrders(String message);
        void navigateLogin();
    }

    void customerSearch(String headerData,String text,onCustomerSearchListener listener);
    void getCustomerOrders(String headerData,long customerId,onCustomerOrdersListener listener);
}
