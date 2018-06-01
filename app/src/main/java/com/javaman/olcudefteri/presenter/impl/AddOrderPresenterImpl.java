package com.javaman.olcudefteri.presenter.impl;

import com.javaman.olcudefteri.model.AddCustomerModel;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.presenter.AddOrderPresenter;
import com.javaman.olcudefteri.view.AddOrderView;
import com.javaman.olcudefteri.intractor.AddOrderIntractor;
import com.javaman.olcudefteri.intractor.impl.AddOrderIntractorImpl;
import com.javaman.olcudefteri.view.CalculateView;
import com.javaman.olcudefteri.view.CustomerView;

/**
 * Created by javaman on 15.02.2018.
 */

public class AddOrderPresenterImpl implements AddOrderPresenter,AddOrderIntractor.onSendCustomerListener {

    AddOrderView mAddOrderView;
    CalculateView mCalculateView;
    CustomerView customerView;
    AddOrderIntractor mAddOrderIntractor;

    public AddOrderPresenterImpl(AddOrderView addOrderView){
        this.mAddOrderView=addOrderView;
        mAddOrderIntractor=new AddOrderIntractorImpl();

    }

    public AddOrderPresenterImpl(CustomerView customerView) {
        this.customerView = customerView;
        mAddOrderIntractor=new AddOrderIntractorImpl();
    }

    @Override
    public void addCustomer(AddCustomerModel addCustomerModel , String headerData) {
        if(mAddOrderView!=null){
            mAddOrderView.showProgress();
            mAddOrderIntractor.addNewCustomer(addCustomerModel,headerData,this);
        }else if(customerView!=null){
            mAddOrderIntractor.addOrderToCustomer(addCustomerModel,headerData,this);
        }
    }


    @Override
    public void onDestroy() {
        if(mAddOrderView!=null){
            mAddOrderView=null;
        }else if(customerView!=null){
            customerView=null;
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
    public void onPhoneFormatError(boolean isMobile,boolean isFixed) {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.setPhoneFormatError(isMobile,isFixed);
        }
    }

    @Override
    public void onSuccess(AddCustomerResponse addCustomerResponse) {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.navigateToOrder(addCustomerResponse);
        }else if(customerView!=null){
            customerView.navigateToOrder(addCustomerResponse);
        }
    }

    @Override
    public void onFailure(String message) {
        if(mAddOrderView!=null){
            mAddOrderView.hideProgress();
            mAddOrderView.showAlert(message);
        }else if(customerView!=null){
            customerView.showAlert(message,true);
        }
    }

    @Override
    public void navigateToLogin() {
        if(mAddOrderView!=null){
            mAddOrderView.navigateLogin();
        }else if(customerView!=null){
            customerView.navigateToLogin();
        }
    }
}
