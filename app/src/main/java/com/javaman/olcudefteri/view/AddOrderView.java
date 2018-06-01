package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.CustomerDetailModel;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderView {

    void showProgress();
    void hideProgress();
    void setNameEmptyError();
    void setPhoneEmptyError();
    void setPhoneFormatError(boolean isMobile,boolean isFixed);
    void navigateToOrder(AddCustomerResponse addCustomerResponse);
    void showAlert(String message);
    String getSessionIdFromPref();
    void navigateLogin();
    void updateData(CustomerDetailModel customerDetailModel);
}
