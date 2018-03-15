package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.model.AddCustomerModel;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.orders.view.AddOrderView;
import com.javaman.olcudefteri.orders.intractor.AddOrderIntractor;
import com.javaman.olcudefteri.orders.intractor.AddOrderIntractorImpl;

/**
 * Created by javaman on 15.02.2018.
 */

public class AddOrderPresenterImpl implements AddOrderPresenter,AddOrderIntractor.onSendCustomerListener {

    AddOrderView mAddOrderView;
    AddOrderIntractor mAddOrderIntractor;

    public AddOrderPresenterImpl(AddOrderView addOrderView){
        this.mAddOrderView=addOrderView;
        mAddOrderIntractor=new AddOrderIntractorImpl();

    }


    @Override
    public void addCustomer(AddCustomerModel addCustomerModel , String headerData) {
        if(mAddOrderView!=null){
            mAddOrderView.showProgress();
            mAddOrderIntractor.addCustomer(addCustomerModel,headerData,this);
        }
    }

    @Override
    public void onDestroy() {
        if(mAddOrderView!=null){
            mAddOrderView=null;
        }
    }

    @Override
    public void onNameEmptyError() {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.setNameEmptyError();
        }
    }

    @Override
    public void onPhoneEmptyError() {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.setPhoneEmptyError();
        }
    }

    @Override
    public void onSuccess(AddCustomerResponse addCustomerResponse) {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.navigateToOrder(addCustomerResponse);
        }
    }

    @Override
    public void onFailure(String message) {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.showAlert(message);
        }
    }
}
