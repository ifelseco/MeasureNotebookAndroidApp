package com.javaman.olcudefteri.add_order;

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


    void addCustomer(CustomerDetailModel customerDetailModel,String headerData, onSendCustomerListener listener);


}
