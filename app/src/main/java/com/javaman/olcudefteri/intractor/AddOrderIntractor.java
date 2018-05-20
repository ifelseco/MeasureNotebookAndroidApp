package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.AddCustomerModel;
import com.javaman.olcudefteri.model.AddCustomerResponse;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderIntractor {

    interface onSendCustomerListener{
        void onNameEmptyError();
        void onPhoneEmptyError();
        void onSuccess(AddCustomerResponse addCustomerResponse);
        void onFailure(String message);
        void navigateToLogin();

    }


    void addCustomer(AddCustomerModel addCustomerModel, String headerData, onSendCustomerListener listener);


}
