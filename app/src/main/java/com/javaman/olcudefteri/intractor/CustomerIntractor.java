package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.presenter.impl.CustomerPresenterImpl;

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

    interface onCustomerUpdateListener{
        void onSuccessCustomnerUpdate(CustomerDetailModel customerDetailModel);
        void onFailureCustomnerUpdate(String message);
        void onNameEmptyError();
        void onPhoneEmptyError();
        void onPhoneFormatError(boolean isMobile,boolean isFixed);
        void navigateLogin();
    }

    void customerSearch(String headerData,String text,onCustomerSearchListener listener);
    void getCustomerOrders(String headerData,long customerId,onCustomerOrdersListener listener);
    void updateCustomer(CustomerDetailModel customerDetailModel, String headerData, onCustomerUpdateListener listener);

}
