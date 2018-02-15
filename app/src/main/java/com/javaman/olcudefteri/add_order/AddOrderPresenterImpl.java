package com.javaman.olcudefteri.add_order;

import com.javaman.olcudefteri.model.CustomerDetailModel;

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
    public void addCustomer(CustomerDetailModel customerDetailModel , String headerData) {
        if(mAddOrderView!=null){
            mAddOrderView.showProgress();
            mAddOrderIntractor.addCustomer(customerDetailModel,headerData,this);
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
    public void onSuccess() {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.navigateToOrder();
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
