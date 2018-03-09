package com.javaman.olcudefteri.orders.intractor;

import com.javaman.olcudefteri.orders.model.AddCustomerModel;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderIntractor {

    interface onSendCustomerListener{
        void onNameEmptyError();
        void onPhoneEmptyError();
        void onSuccess();
        void onFailure(String message);

    }


    void addCustomer(AddCustomerModel addCustomerModel, String headerData, onSendCustomerListener listener);


}
