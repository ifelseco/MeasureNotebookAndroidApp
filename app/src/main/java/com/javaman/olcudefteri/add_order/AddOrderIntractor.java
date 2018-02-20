package com.javaman.olcudefteri.add_order;

import com.javaman.olcudefteri.model.AddCustomerModel;
import com.javaman.olcudefteri.model.CustomerDetailModel;

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
